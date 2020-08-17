package me.lightlord323dev.cursedvaults.api.cursedvault;

import me.lightlord323dev.cursedvaults.Main;
import org.bukkit.Bukkit;
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

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVault {

    private UUID owner;
    private int size, pickupRadius;
    private float speed;
    private List<ItemStack> items;
    private ArmorStand display;
    private CursedVaultMoveTask task;

    public CursedVault(UUID owner, int size, int pickupRadius, float speed, Location spawnLocation) {
        this.owner = owner;
        this.size = size;
        this.pickupRadius = pickupRadius;
        this.speed = speed;
        this.items = new ArrayList<>();
        spawnDisplay(spawnLocation);
        this.task = new CursedVaultMoveTask(this);
//        this.task.runTaskTimer(Main.getInstance(), 0, 2);
    }

    public CursedVault(UUID owner, int size, int pickupRadius, float speed, List<ItemStack> items, Location spawnLocation) {
        this.owner = owner;
        this.size = size;
        this.pickupRadius = pickupRadius;
        this.speed = speed;
        this.items = items;
        spawnDisplay(spawnLocation);
        this.task = new CursedVaultMoveTask(this);
//        this.task.runTaskTimer(Main.getInstance(), 0, 2);
    }

    public void tryToPickup() {
        display.getLocation().getWorld().getNearbyEntities(display.getLocation(), pickupRadius, pickupRadius, pickupRadius).stream().filter(entity -> entity instanceof Item).forEach(item -> {
            ItemStack itemToPickup = ((Item) item).getItemStack();
            ItemStack storedItem = this.items.stream().filter(itemStack -> itemStack.isSimilar(itemToPickup)).findAny().orElse(null);
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
                if (items.size() < this.size) {
                    items.add(itemToPickup);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> item.remove());
                }
            }
        });
    }

    public boolean canPickupItem(ItemStack item) {
        ItemStack storedItem = this.items.stream().filter(itemStack -> itemStack.isSimilar(item)).findAny().orElse(null);
        if (storedItem != null && storedItem.getAmount() < storedItem.getMaxStackSize())
            return true;
        else
            return items.size() < this.size;
    }

    public void move(Location location) {
        this.display.teleport(location);
    }

    private void spawnDisplay(Location location) {
        // ARMORSTAND
        display = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, -1, 0), EntityType.ARMOR_STAND);
        display.setCustomName(Bukkit.getPlayer(owner).getName() + "'s Cursed Vault");
        display.setCustomNameVisible(true);
        display.getEquipment().setHelmet(new ItemStack(Material.CHEST));
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
        this.size = size;
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

    public ArmorStand getDisplay() {
        return display;
    }
}
