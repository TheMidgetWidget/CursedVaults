package me.lightlord323dev.cursedvaults.util;


import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

/**
 * Created by Luda on 8/17/2020.
 */
public class NBTApi {

    private ItemStack itemStack;

    public NBTApi(org.bukkit.inventory.ItemStack itemStack) {
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!this.itemStack.hasTag())
            this.itemStack.setTag(new NBTTagCompound());
    }

    public NBTApi setInt(String key, int value) {
        itemStack.getTag().setInt(key, value);
        return this;
    }

    public NBTApi setDouble(String key, double value) {
        itemStack.getTag().setDouble(key, value);
        return this;
    }

    public NBTApi setBoolean(String key, boolean value) {
        itemStack.getTag().setBoolean(key, value);
        return this;
    }

    public NBTApi setString(String key, String value) {
        itemStack.getTag().setString(key, value);
        return this;
    }

    public int getInt(String key) {
        return itemStack.getTag().getInt(key);
    }

    public double getDouble(String key) {
        return itemStack.getTag().getDouble(key);
    }

    public String getString(String key) {
        return itemStack.getTag().getString(key);
    }

    public boolean hasKey(String key) {
        return itemStack.getTag().hasKey(key);
    }

    public NBTApi removeKey(String key) {
        itemStack.getTag().remove(key);
        return this;
    }

    public org.bukkit.inventory.ItemStack getItemStack() {
        return CraftItemStack.asBukkitCopy(itemStack);
    }
}
