package me.lightlord323dev.cursedvaults.api.gui.itemmenu;

import me.lightlord323dev.cursedvaults.api.gui.InventoryFill;
import me.lightlord323dev.cursedvaults.util.ItemBuilder;
import me.lightlord323dev.cursedvaults.util.NBTApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 8/17/2020.
 */
public class GUIItemMenu {

    private Inventory inventory;

    public GUIItemMenu(UUID vaultUUID, String name, int size, int availableSize, int page, List<ItemStack> itemStacks) {
        this.inventory = Bukkit.createInventory(null, size, name);

        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(" ");
        filler.setItemMeta(meta);
        filler = new NBTApi(filler).setBoolean("cvClickable", true).getItemStack();

        ItemStack sidesFiller = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        ItemMeta sideMeta = sidesFiller.getItemMeta();
        sideMeta.setDisplayName(" ");
        sidesFiller.setItemMeta(sideMeta);
        sidesFiller = new NBTApi(sidesFiller).setBoolean("cvClickable", true).getItemStack();

        InventoryFill inventoryFill = new InventoryFill(inventory);
        inventoryFill.fillSidesWithItem(sidesFiller);

        int counter = 0, openSlots = availableSize - ((page - 1) * 28);
        if (openSlots < 0)
            openSlots = availableSize;
        if (openSlots > 28) {
            openSlots = 28;
            inventory.setItem(53, new NBTApi(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5)).setDisplayName(ChatColor.GREEN + "Next page").build()).setBoolean("cvClickable", true).setInt("cvPage", page + 1).setString("cvUUID", vaultUUID.toString()).getItemStack());
        }
        if (page > 1)
            inventory.setItem(45, new NBTApi(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14)).setDisplayName(ChatColor.RED + "Previous page").build()).setBoolean("cvClickable", true).setInt("cvPage", page - 1).setString("cvUUID", vaultUUID.toString()).getItemStack());
        for (Integer index : inventoryFill.getNonSideSlots()) {
            if (counter < openSlots) {
                counter++;
            } else {
                inventory.setItem(index, filler);
            }
        }

        int startIndex = ((page - 1) * 28);

        for (int i = 0; i < openSlots; i++) {
            int actualIndex = startIndex + i;
            if (actualIndex >= itemStacks.size())
                break;
            else {
                ItemStack itemStack = itemStacks.get(actualIndex);
                if (itemStack != null && itemStack.getType() != Material.AIR)
                    inventory.addItem(itemStacks.get(actualIndex));
            }
        }

    }

    public Inventory getInventory() {
        return inventory;
    }

}
