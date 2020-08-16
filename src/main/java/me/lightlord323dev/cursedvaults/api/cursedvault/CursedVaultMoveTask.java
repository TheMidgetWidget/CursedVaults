package me.lightlord323dev.cursedvaults.api.cursedvault;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVaultMoveTask extends BukkitRunnable {

    private UUID owner;
    private ArmorStand armorStand;

    public CursedVaultMoveTask(UUID owner, ArmorStand armorStand) {
        this.owner = owner;
        this.armorStand = armorStand;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(owner);
        if (armorStand.getLocation().distanceSquared(player.getLocation()) >= 100) {
            Location blockLoc = player.getLocation().clone().add(0, -1, 0);
            if (blockLoc.getBlock().getType() != Material.AIR)
                armorStand.teleport(blockLoc);
        } else {
            Location playerLoc = player.getLocation().clone(), targetLoc;
            if (armorStand.getLocation().getBlockX() == playerLoc.getBlockX() && armorStand.getLocation().getBlockZ() == playerLoc.getBlockZ()) {
                return;
            } else {
                targetLoc = armorStand.getLocation().clone().add(playerLoc.toVector().subtract(armorStand.getLocation().toVector()).normalize());
                if (targetLoc.distanceSquared(playerLoc) <= 9)
                    return;
            }

            // making vault look in the same dir
            targetLoc.setDirection(playerLoc.subtract(targetLoc).toVector());

            for (int i = -3; i < 3; i++) {
                Location tempLoc = targetLoc.clone().add(0, i, 0);
                if (isValidBottomBlock(tempLoc) && isValidTopBlock(tempLoc.clone().add(0, 1, 0))) {
                    targetLoc = tempLoc;
                }
            }

//            if (isValidTopBlock(targetLoc))
//                targetLoc.subtract(0, 1, 0);

            targetLoc.setY(targetLoc.getBlockY());

            if (isValidBottomBlock(targetLoc))
                armorStand.teleport(targetLoc);
        }
    }

    private boolean isValidBottomBlock(Location location) {
        return location.getBlock().getType() != Material.AIR && location.getBlock().getType() != Material.SNOW && location.getBlock().getType() != Material.CARPET && location.getBlock().getType() != Material.BROWN_MUSHROOM && location.getBlock().getType() != Material.RED_MUSHROOM && location.getBlock().getType() != Material.LONG_GRASS && location.getBlock().getType() != Material.YELLOW_FLOWER && location.getBlock().getType() != Material.RED_ROSE;
    }

    private boolean isValidTopBlock(Location location) {
        return location.getBlock().getType() == Material.AIR || location.getBlock().getType() == Material.SNOW || location.getBlock().getType() == Material.CARPET || location.getBlock().getType() == Material.BROWN_MUSHROOM || location.getBlock().getType() == Material.RED_MUSHROOM || location.getBlock().getType() == Material.LONG_GRASS || location.getBlock().getType() == Material.YELLOW_FLOWER || location.getBlock().getType() == Material.RED_ROSE;
    }

}