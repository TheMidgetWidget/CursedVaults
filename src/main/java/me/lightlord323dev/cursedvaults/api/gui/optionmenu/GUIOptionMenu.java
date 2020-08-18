package me.lightlord323dev.cursedvaults.api.gui.optionmenu;

import me.lightlord323dev.cursedvaults.api.gui.InventoryFill;
import me.lightlord323dev.cursedvaults.util.NBTApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by Luda on 8/17/2020.
 */
public class GUIOptionMenu {

    private Inventory inventory;

    public GUIOptionMenu(String name, int size, List<OptionItem> optionItems) {
        this.inventory = Bukkit.createInventory(null, size, name);

        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(" ");
        filler.setItemMeta(meta);
        filler = new NBTApi(filler).setBoolean("cvClickable", true).getItemStack();

        InventoryFill inventoryFill = new InventoryFill(inventory);
        ItemStack tempFiller = filler;
        inventoryFill.getNonSideSlots().forEach(index -> inventory.setItem(index, tempFiller));
        inventoryFill.fillSidesWithItem(new ItemStack(Material.AIR));

        optionItems.forEach(optionItem -> {
            if (optionItem.getIndex() >= 0 && optionItem.getIndex() < 54) {
                inventory.setItem(optionItem.getIndex(), optionItem.getItemStack());
            }
        });
    }

    public Inventory getInventory() {
        return inventory;
    }
}
