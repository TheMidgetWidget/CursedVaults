package me.lightlord323dev.cursedvaults.api.gui.optionmenu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by Luda on 8/17/2020.
 */
public class OptionItem {

    private ItemStack item;
    private int index;
    private Runnable runnable;

    public OptionItem(ItemStack item, int index) {
        this.item = item;
        this.index = index;
    }

    public OptionItem(Material material, String name, String lore, int index) {
        this.index = index;

        this.item = new ItemStack(material);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        this.item.setItemMeta(meta);
    }

    public ItemStack getItemStack() {
        return item;
    }

    public int getIndex() {
        return index;
    }
}
