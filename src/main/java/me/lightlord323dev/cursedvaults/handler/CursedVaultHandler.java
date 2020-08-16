package me.lightlord323dev.cursedvaults.handler;

import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 8/16/2020.
 */
public class CursedVaultHandler implements Handler {

    private List<CursedVault> cursedVaults;

    @Override
    public void onLoad() {
        cursedVaults = new ArrayList<>();
    }

    @Override
    public void onUnload() {
        // TODO save loaded vaults
    }

    public void registerCursedVault(CursedVault cursedVault) {
        this.cursedVaults.add(cursedVault);
    }

    public void unregisterCursedVault(CursedVault cursedVault) {
        if (this.cursedVaults.contains(cursedVault))
            this.cursedVaults.remove(cursedVault);
    }

    public CursedVault getCursedVault(Player player) {
        return cursedVaults.stream().filter(cursedVault -> cursedVault.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())).findAny().orElse(null);
    }

    public CursedVault getCursedVault(UUID uuid) {
        return cursedVaults.stream().filter(cursedVault -> cursedVault.getOwner().toString().equalsIgnoreCase(uuid.toString())).findAny().orElse(null);
    }
}
