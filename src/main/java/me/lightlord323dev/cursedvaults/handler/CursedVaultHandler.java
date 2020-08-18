package me.lightlord323dev.cursedvaults.handler;

import com.google.gson.reflect.TypeToken;
import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import me.lightlord323dev.cursedvaults.util.ItemBuilder;
import me.lightlord323dev.cursedvaults.util.NBTApi;
import me.lightlord323dev.cursedvaults.util.file.AbstractFile;
import me.lightlord323dev.cursedvaults.util.file.GsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    private long saveTimer;

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
        saveTimer = 0;
        Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
            cursedVaults.forEach(cursedVault -> {
                Player player = Bukkit.getPlayer(cursedVault.getOwner());

                if (player == null || !player.isOnline())
                    return;

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

            // AUTO SAVE
            if (saveTimer >= 30000) {
                System.out.println("[Cursed Vaults] Attempting to save data...");
                cursedVaults.forEach(cursedVault -> saveCursedVaultData(cursedVault));
                Main.getInstance().getHandlerRegistery().getCursedVaultPlayerHandler().onUnload();
                System.out.println("[Cursed Vaults] Data successfully saved");
                saveTimer = 0;
            } else
                saveTimer++;

        }, 0, 10L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onUnload() {
//        Main.getInstance().getExecutorService().schedule(() -> cursedVaults.forEach(cursedVault -> saveCursedVaultData(cursedVault)), 0, TimeUnit.MILLISECONDS);
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

    public ItemStack createVaultItem(Player owner) {
        CursedVault cursedVault = new CursedVault(owner.getUniqueId(), 7, 1, 1f);
        ItemStack vaultItem = new NBTApi(new ItemBuilder(Material.CHEST).setDisplayName(ChatColor.translateAlternateColorCodes('&', cursedVault.getDisplayName())).setLore(ChatColor.GRAY + "Place to spawn this vault").build()).setString("vaultUUID", cursedVault.getUniqueId().toString()).getItemStack();
        Main.getInstance().getExecutorService().schedule(() -> saveCursedVaultData(cursedVault), 0, TimeUnit.MILLISECONDS);
        return vaultItem;
    }

    public void saveAndUnregisterVault(CursedVault cursedVault) {
        Main.getInstance().getExecutorService().schedule(() -> {
            AbstractFile vaultFile = new AbstractFile(Main.getInstance(), cursedVault.getUniqueId().toString() + ".json", "vaultdata", false);
            GsonUtil.saveObject(cursedVault.serialize(), vaultFile.getFile());
            unregisterCursedVault(cursedVault);
        }, 0, TimeUnit.MILLISECONDS);
    }

    public void loadAndSpawnVault(UUID uuid, Location spawnLocation) {
        Main.getInstance().getExecutorService().schedule(() -> {
            AbstractFile vaultFile = new AbstractFile(Main.getInstance(), uuid.toString() + ".json", "vaultdata", false);
            CursedVault cursedVault = GsonUtil.loadObject(new TypeToken<CursedVault>() {
            }, vaultFile.getFile());
            if (cursedVault != null) {
                cursedVault.load();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                    registerCursedVault(cursedVault);
                    if (spawnLocation == null)
                        cursedVault.spawnDisplay(cursedVault.getLastSeenLocation());
                    else
                        cursedVault.spawnDisplay(spawnLocation);
                });
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    private void saveCursedVaultData(CursedVault cursedVault) {
        AbstractFile vaultFile = new AbstractFile(Main.getInstance(), cursedVault.getUniqueId().toString() + ".json", "vaultdata", false);
        GsonUtil.saveObject(cursedVault.serialize(), vaultFile.getFile());
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
