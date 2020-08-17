package me.lightlord323dev.cursedvaults.api.cursedvault;

import me.lightlord323dev.cursedvaults.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVaultMoveTask {

    private CursedVault cursedVault;
    private int counter;

    private static final List<Material> PROBLEMATIC = Arrays.asList(
            Material.AIR,
            Material.SNOW,
            Material.CARPET,
            Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM,
            Material.LONG_GRASS,
            Material.YELLOW_FLOWER,
            Material.RED_ROSE
    );

    public CursedVaultMoveTask(CursedVault cursedVault) {
        this.cursedVault = cursedVault;
        this.counter = 0;

        Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
            Player player = Bukkit.getPlayer(cursedVault.getOwner());
            if (cursedVault.getDisplay().getLocation().distanceSquared(player.getLocation()) >= 1000) {
                Location blockLoc = player.getLocation().clone().add(0, -1, 0);
                if (blockLoc.getBlock().getType() != Material.AIR) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        cursedVault.getDisplay().teleport(blockLoc);
                    });
                }
            } else {
                Location playerLoc = player.getLocation().clone(), targetLoc;
                if (cursedVault.getDisplay().getLocation().getBlockX() == playerLoc.getBlockX() && cursedVault.getDisplay().getLocation().getBlockZ() == playerLoc.getBlockZ()) {
                    return;
                } else {
                    targetLoc = cursedVault.getDisplay().getLocation().clone().add(playerLoc.toVector().subtract(cursedVault.getDisplay().getLocation().toVector()).normalize().multiply(0.2));
                    if (targetLoc.distanceSquared(playerLoc) <= 4)
                        return;
                }

                // making vault look in the same dir
                targetLoc.setDirection(playerLoc.subtract(targetLoc).toVector());

                Location targetClone = targetLoc.clone();

                for (int i = -2; i < 2; i++) {
                    if (isValidBottomBlock(targetClone.clone().add(0, i, 0)) && isValidTopBlock(targetClone.clone().add(0, i + 1, 0))) {
                        targetLoc = targetClone.clone().add(0, i, 0);
                    }
                }

                if (targetLoc.getBlock().getType() != Material.AIR) {
                    targetLoc.setY(targetLoc.getBlockY());
                    Location tempLoc = targetLoc;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        cursedVault.getDisplay().teleport(tempLoc);
                    });
                }
                if (counter == 0) {
                    cursedVault.tryToPickup();
                    counter++;
                } else if (counter > 0) {
                    counter++;
                    if (counter >= 25)
                        counter = 0;
                }

            }
        }, 0, 10L, TimeUnit.MILLISECONDS);

    }

    private boolean isValidBottomBlock(Location location) {
        if (PROBLEMATIC.contains(location.getBlock().getType()) || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.LAVA)
            return false;
        return true;
    }

    private boolean isValidTopBlock(Location location) {
        return PROBLEMATIC.contains(location.getBlock().getType());
    }

}