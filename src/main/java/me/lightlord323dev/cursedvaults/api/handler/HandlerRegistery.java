package me.lightlord323dev.cursedvaults.api.handler;

import me.lightlord323dev.cursedvaults.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 8/16/2020.
 */
public class HandlerRegistery {

    private List<Handler> handlers;

    public void loadHanders() {
        handlers = new ArrayList<>();
        // REGISTER HANDLERS
        handlers.forEach(handler -> {
            handler.onLoad();
            if (handler instanceof Listener)
                Bukkit.getPluginManager().registerEvents(((Listener)handler), Main.getInstance());
        });
    }

    public void unloadHandlers() {
        handlers.forEach(Handler::onUnload);
    }

}
