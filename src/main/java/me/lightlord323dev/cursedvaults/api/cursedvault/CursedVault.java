package me.lightlord323dev.cursedvaults.api.cursedvault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

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

    public CursedVault(UUID owner, int size, int pickupRadius, float speed, Location spawnLocation) {
        this.owner = owner;
        this.size = size;
        this.pickupRadius = pickupRadius;
        this.speed = speed;
        this.items = new ArrayList<>();
        spawnDisplay(spawnLocation);
    }

    public CursedVault(UUID owner, int size, int pickupRadius, float speed, List<ItemStack> items, Location spawnLocation) {
        this.owner = owner;
        this.size = size;
        this.pickupRadius = pickupRadius;
        this.speed = speed;
        this.items = items;
        spawnDisplay(spawnLocation);
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
                    item.remove();
                } else {
                    itemToPickup.setAmount(excessAmount);
                    ((Item) item).setItemStack(itemToPickup);
                }
            } else {
                if (items.size() < this.size) {
                    items.add(itemToPickup);
                    item.remove();
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

    private void spawnDisplay(Location location) {
        display = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        display.getEquipment().setHelmet(new ItemStack(Material.CHEST));
        // have to play around with rotations to achieve wanted effect
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
}
