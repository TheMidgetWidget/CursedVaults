package me.lightlord323dev.cursedvaults.handler;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import me.lightlord323dev.cursedvaults.api.gui.itemmenu.GUIItemMenu;
import me.lightlord323dev.cursedvaults.api.gui.optionmenu.OptionItem;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import me.lightlord323dev.cursedvaults.util.NBTApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVaultInteractHandler implements Handler, Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().getType() == Material.CHEST) {
            NBTApi nbtApi = new NBTApi(e.getItemInHand());
            if (nbtApi.hasKey("vaultUUID")) {
                e.setCancelled(true);
                CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(e.getPlayer());
                if (cursedVault != null) {
                    e.getPlayer().sendMessage(ChatColor.RED + "You already have a vault spawned in.");
                } else {
                    e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    Main.getInstance().getHandlerRegistery().getCursedVaultHandler().loadAndSpawnVault(UUID.fromString(nbtApi.getString("vaultUUID")), e.getBlock().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand && e.getRightClicked().hasMetadata("cursedVault")) {
            e.setCancelled(true);
            UUID owner = UUID.fromString(e.getRightClicked().getMetadata("cursedVault").get(0).asString());
            if (owner.toString().equalsIgnoreCase(e.getPlayer().getUniqueId().toString())) {
                CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(Bukkit.getPlayer(owner));
                e.getPlayer().setMetadata("cvInv", new FixedMetadataValue(Main.getInstance(), 1));
                Inventory inventory = new GUIItemMenu(cursedVault.getUniqueId(), ChatColor.translateAlternateColorCodes('&', cursedVault.getDisplayName()), 54, cursedVault.getSize(), 1, cursedVault.getItems()).getInventory();
                inventory.setItem(4, new OptionItem(Material.CHEST, ChatColor.GOLD + "Options", ChatColor.GRAY + "Click to view vault options", 0, "menu").getItemStack());
                e.getPlayer().openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand && e.getEntity().hasMetadata("cursedVault"))
            e.setCancelled(true);
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
    }
}
