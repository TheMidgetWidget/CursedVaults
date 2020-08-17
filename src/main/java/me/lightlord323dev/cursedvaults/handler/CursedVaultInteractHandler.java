package me.lightlord323dev.cursedvaults.handler;

import me.lightlord323dev.cursedvaults.api.gui.optionmenu.GUIOptionMenu;
import me.lightlord323dev.cursedvaults.api.gui.optionmenu.OptionItem;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVaultInteractHandler implements Handler, Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand && e.getRightClicked().hasMetadata("cursedVault")) {
            e.setCancelled(true);
            UUID owner = UUID.fromString(e.getRightClicked().getMetadata("cursedVault").get(0).asString());
            e.getPlayer().openInventory(new GUIOptionMenu(ChatColor.RED + "TEST", 54, Arrays.asList(new OptionItem(Material.NAME_TAG, ChatColor.GREEN + "CLICK ME", ChatColor.GRAY + "Clicker", 25))).getInventory());
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
