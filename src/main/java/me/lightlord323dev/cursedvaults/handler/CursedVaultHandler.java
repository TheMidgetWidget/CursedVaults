package me.lightlord323dev.cursedvaults.handler;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVaultHandler implements Handler {

    private List<CursedVault> cursedVaults;

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


    @Override
    public void onLoad() {
        cursedVaults = new ArrayList<>();
        Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
            cursedVaults.forEach(cursedVault -> {
                Player player = Bukkit.getPlayer(cursedVault.getOwner());

                if (!cursedVault.isCanMove()) {
                    if (!cursedVault.getLocation().getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
                        teleportToPlayer(player, cursedVault);
                        cursedVault.setCanMove(true);
                    }
                    return;
                }

                if (!cursedVault.getLocation().getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
                    teleportToPlayer(player, cursedVault);
                } else if (cursedVault.getDisplay().getLocation().distanceSquared(player.getLocation()) >= 1000) {
                    teleportToPlayer(player, cursedVault);
                } else {
                    Location playerLoc = player.getLocation().clone(), targetLoc;
                    if (cursedVault.getDisplay().getLocation().getBlockX() == playerLoc.getBlockX() && cursedVault.getDisplay().getLocation().getBlockZ() == playerLoc.getBlockZ()) {
                        return;
                    } else {
                        targetLoc = cursedVault.getDisplay().getLocation().clone().add(playerLoc.toVector().subtract(cursedVault.getDisplay().getLocation().toVector()).normalize().multiply(0.1 + (cursedVault.getSpeed()) * 0.05));
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
                    if (cursedVault.getCounter() == 0) {
                        cursedVault.tryToPickup();
                        cursedVault.setCounter(cursedVault.getCounter() + 1);
                    } else if (cursedVault.getCounter() > 0) {
                        cursedVault.setCounter(cursedVault.getCounter() + 1);
                        if (cursedVault.getCounter() >= 25)
                            cursedVault.setCounter(0);
                    }

                }
            });
        }, 0, 10L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onUnload() {
        // TODO save loaded vaults
    }

    public void registerCursedVault(CursedVault cursedVault) {
        this.cursedVaults.add(cursedVault);
    }

    public void unregisterCursedVault(CursedVault cursedVault) {
        if (this.cursedVaults.contains(cursedVault)) {
            cursedVault.getDisplay().remove();
            this.cursedVaults.remove(cursedVault);
        }
    }

    public CursedVault getCursedVault(Player player) {
        return cursedVaults.stream().filter(cursedVault -> cursedVault.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())).findAny().orElse(null);
    }

    public CursedVault getCursedVault(UUID uuid) {
        return cursedVaults.stream().filter(cursedVault -> cursedVault.getUniqueId().toString().equalsIgnoreCase(uuid.toString())).findAny().orElse(null);
    }

    private void teleportToPlayer(Player player, CursedVault cursedVault) {
        Location blockLoc = player.getLocation().clone().add(0, -1, 0);
        if (blockLoc.getBlock().getType() != Material.AIR) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                cursedVault.getDisplay().teleport(blockLoc);
            });
        }
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
