package me.lightlord323dev.cursedvaults.handler;

import com.google.gson.reflect.TypeToken;
import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import me.lightlord323dev.cursedvaults.api.user.CursedVaultUser;
import me.lightlord323dev.cursedvaults.util.LocationUtils;
import me.lightlord323dev.cursedvaults.util.file.GsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 8/18/2020.
 */
public class CursedVaultPlayerHandler implements Handler, Listener {

    private List<CursedVaultUser> users;

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(e.getPlayer());
        if (cursedVault != null) {
            cursedVault.setLastSeenLocation(LocationUtils.serializeLocation(cursedVault.getDisplay().getLocation()));
            cursedVault.getDisplay().remove();
            Main.getInstance().getHandlerRegistery().getCursedVaultHandler().saveAndUnregisterVault(cursedVault);
            users.add(new CursedVaultUser(e.getPlayer().getUniqueId(), cursedVault.getUniqueId()));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CursedVaultUser user = getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            Main.getInstance().getHandlerRegistery().getCursedVaultHandler().loadAndSpawnVault(user.getVault(), null);
            users.remove(user);
        }
    }

    @Override
    public void onLoad() {
        users = GsonUtil.loadObject(new TypeToken<List<CursedVaultUser>>() {
        }, Main.getInstance().getUserData().getFile());
        if (users == null)
            users = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            CursedVaultUser user = getUser(player.getUniqueId());
            if (user != null) {
                Main.getInstance().getHandlerRegistery().getCursedVaultHandler().loadAndSpawnVault(user.getVault(), null);
                users.remove(user);
            }
        });
    }

    @Override
    public void onUnload() {
        this.users.addAll(Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getUserData());
        GsonUtil.saveObject(users, Main.getInstance().getUserData().getFile());
    }

    private CursedVaultUser getUser(UUID uuid) {
        return users.stream().filter(cursedVaultUser -> cursedVaultUser.getUser().toString().equalsIgnoreCase(uuid.toString())).findAny().orElse(null);
    }
}
