package me.lightlord323dev.cursedvaults.api.cursedvault;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.util.ItemSerializer;
import me.lightlord323dev.cursedvaults.util.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVault {

    private UUID uniqueId, owner;
    private String displayName;
    private transient ItemStack skin;
    private String serializedSkin;
    private int size, pickupRadius;
    private float speed;
    private transient List<ItemStack> items;
    private List<Material> filter;
    private FilterMode filterMode;
    private List<String> serializedItems;
    private String lastSeenLocation;
    private transient ArmorStand display;
    private int counter; // pick up delay
    private boolean canPickUp, canMove;

    public CursedVault(UUID owner, int size, int pickupRadius, float speed) {
        this.uniqueId = UUID.randomUUID();
        this.owner = owner;
        this.displayName = Bukkit.getPlayer(owner).getName() + "'s Cursed Vault";
        this.skin = new ItemStack(Material.CHEST);
        this.size = size;
        this.pickupRadius = pickupRadius;
        this.speed = speed;
        this.items = new ArrayList<>();
        this.filter = new ArrayList<>();
        this.filterMode = FilterMode.NONE;
        for (int i = 0; i < size; i++)
            items.add(new ItemStack(Material.AIR));
        this.counter = 0;
        this.canPickUp = true;
        this.canMove = true;
    }

    public CursedVault serialize() {
        this.serializedItems = new ArrayList<>();
        this.items.forEach(itemStack -> serializedItems.add(ItemSerializer.itemStackToBase64(itemStack)));
        this.serializedSkin = ItemSerializer.itemStackToBase64(this.skin);
        return this;
    }

    public void load() {
        this.items = new ArrayList<>();
        this.serializedItems.forEach(item -> items.add(ItemSerializer.itemStackFromBase64(item)));
        this.skin = ItemSerializer.itemStackFromBase64(this.serializedSkin);
        this.serializedItems = null;
        this.serializedSkin = null;
    }

    public void tryToPickup() {
        if (!canPickUp)
            return;
        display.getLocation().getWorld().getNearbyEntities(display.getLocation().clone().add(0, 1, 0), pickupRadius, pickupRadius, pickupRadius).stream().filter(entity -> entity instanceof Item).forEach(item -> {
            ItemStack itemToPickup = ((Item) item).getItemStack();
            if (!canPickupItem(itemToPickup))
                return;
            ItemStack storedItem = this.items.stream().filter(itemStack -> itemStack != null && itemStack.isSimilar(itemToPickup)).findAny().orElse(null);
            if (storedItem != null && storedItem.getAmount() < storedItem.getMaxStackSize()) {
                int index = items.indexOf(storedItem), amountToFill = storedItem.getMaxStackSize() - storedItem.getAmount(), excessAmount = itemToPickup.getAmount() >= amountToFill ? itemToPickup.getAmount() - amountToFill : 0;
                storedItem.setAmount(storedItem.getAmount() + itemToPickup.getAmount() - excessAmount);
                items.set(index, storedItem);
                if (excessAmount == 0) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> item.remove());
                } else {
                    itemToPickup.setAmount(excessAmount);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> ((Item) item).setItemStack(itemToPickup));
                }
            } else {
                int index = IntStream.range(0, items.size())
                        .filter(i -> items.get(i).getType() == Material.AIR)
                        .findFirst()
                        .orElse(-1);
                if (index != -1) {
                    items.set(index, itemToPickup);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> item.remove());
                }
            }
        });
    }

    public void updateSkin() {
        this.display.setHelmet(this.skin);
    }

    private boolean canPickupItem(ItemStack itemStack) {
        switch (this.filterMode) {
            case NONE:
                return true;
            case BLACKLIST:
                return !this.filter.contains(itemStack.getType());
            case WHITELIST:
                return this.filter.contains(itemStack.getType());
            default:
                return true;
        }
    }

    public void spawnDisplay(Location location) {
        display = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, -1, 0), EntityType.ARMOR_STAND);
        display.setCustomName(ChatColor.translateAlternateColorCodes('&', this.displayName));
        display.setCustomNameVisible(true);
        display.getEquipment().setHelmet(this.skin);
        display.setVisible(false);
        display.setCanPickupItems(false);
        display.setGravity(false);

        display.setMetadata("cursedVault", new FixedMetadataValue(Main.getInstance(), owner.toString()));
    }

    public UUID getOwner() {
        return owner;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size > this.size) {
            int diff = size - this.size;
            for (int i = 0; i < diff; i++) {
                this.items.add(new ItemStack(Material.AIR));
            }
        }
        this.size = size;
    }

    public Location getLocation() {
        return this.display.getLocation();
    }

    public int getPickupRadius() {
        return pickupRadius;
    }

    public void setPickupRadius(int pickupRadius) {
        this.pickupRadius = pickupRadius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public void setItem(int index, ItemStack item) {
        if (index < this.items.size())
            this.items.set(index, item);
    }

    public ArmorStand getDisplay() {
        return display;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.display.setCustomName(ChatColor.translateAlternateColorCodes('&', this.displayName));
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean getCanPickUp() {
        return canPickUp;
    }

    public void setCanPickUp(boolean canPickUp) {
        this.canPickUp = canPickUp;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public Location getLastSeenLocation() {
        return LocationUtils.deserializeLocation(lastSeenLocation);
    }

    public void setLastSeenLocation(String lastSeenLocation) {
        this.lastSeenLocation = lastSeenLocation;
    }

    public List<Material> getFilterList() {
        return filter;
    }

    public void setFilterList(List<Material> filter) {
        this.filter = filter;
    }

    public void addFilterItem(Material material) {
        this.filter.add(material);
    }

    public void removeFilterItem(Material material) {
        this.filter.remove(material);
    }

    public FilterMode getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    public ItemStack getSkin() {
        return skin;
    }

    public void setSkin(ItemStack skin) {
        this.skin = skin;
    }
}
