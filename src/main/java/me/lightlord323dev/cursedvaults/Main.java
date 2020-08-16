package me.lightlord323dev.cursedvaults;

import me.lightlord323dev.cursedvaults.api.handler.HandlerRegistery;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Luda on 8/16/2020.
 */
public class Main extends JavaPlugin{

    private static Main instance;

    // handler registery
    HandlerRegistery handlerRegistery;

    @Override
    public void onEnable() {
        instance = this;

        // loading handlers
        handlerRegistery = new HandlerRegistery();
        handlerRegistery.loadHanders();
    }

    @Override
    public void onDisable() {
        handlerRegistery.unloadHandlers();
    }

    public static Main getInstance() {
        return instance;
    }
}
