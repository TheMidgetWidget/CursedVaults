package me.lightlord323dev.cursedvaults.handler;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

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
            Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(owner).tryToPickup();
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
