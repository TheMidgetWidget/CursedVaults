package me.lightlord323dev.cursedvaults.api.gui.optionmenu;

import com.google.common.collect.Lists;
import me.lightlord323dev.cursedvaults.util.NBTApi;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Luda on 8/17/2020.
 */
public class OptionItem {

    private ItemStack item;
    private int index;

    public OptionItem(ItemStack item, int index, String nbtIdentifier) {
        this.item = item;
        this.index = index;
        this.item = new NBTApi(item).setString("cvOption", nbtIdentifier).getItemStack();
    }

    public OptionItem(Material material, String name, String lore, int index, String nbtIdentifier) {
        this.index = index;

        this.item = new ItemStack(material);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Lists.newArrayList(" ", " ", lore, " ", " "));
        this.item.setItemMeta(meta);
        this.item = new NBTApi(item).setBoolean("cvClickable", true).setString("cvOption", nbtIdentifier).getItemStack();
    }

    public OptionItem(Material material, int index, String nbtIdentifier, String name, String... lore) {
        this.index = index;

        this.item = new ItemStack(material);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Lists.asList(" ", " ", lore));
        this.item.setItemMeta(meta);
        this.item = new NBTApi(item).setBoolean("cvClickable", true).setString("cvOption", nbtIdentifier).getItemStack();
    }

    public ItemStack getItemStack() {
        return item;
    }

    public int getIndex() {
        return index;
    }
}
