package me.lightlord323dev.cursedvaults.api.handler;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.handler.CursedVaultHandler;
import me.lightlord323dev.cursedvaults.handler.CursedVaultInteractHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/16/2020.
 */
public class HandlerRegistery {

    private List<Handler> handlers;

    // handlers
    private CursedVaultHandler cursedVaultHandler;

    public void loadHanders() {
        handlers = new ArrayList<>();
        // REGISTER HANDLERS
        handlers.addAll(Arrays.asList(
                cursedVaultHandler = new CursedVaultHandler(),
                new CursedVaultInteractHandler()
        ));
        // call onLoad method
        handlers.forEach(handler -> {
            handler.onLoad();
            if (handler instanceof Listener) {
                Bukkit.getPluginManager().registerEvents(((Listener) handler), Main.getInstance());
            }
        });
    }

    public void unloadHandlers() {
        handlers.forEach(Handler::onUnload);
    }

    public CursedVaultHandler getCursedVaultHandler() {
        return cursedVaultHandler;
    }
}
