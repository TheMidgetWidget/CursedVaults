package me.lightlord323dev.cursedvaults;

import me.lightlord323dev.cursedvaults.api.handler.HandlerRegistery;
import me.lightlord323dev.cursedvaults.command.CommandSpawnVault;
import me.lightlord323dev.cursedvaults.util.file.AbstractFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Luda on 8/16/2020.
 */
public class Main extends JavaPlugin {

    private static Main instance;

    // handler registery
    private HandlerRegistery handlerRegistery;

    private ScheduledExecutorService executorService;

    // files
    private AbstractFile userData;

    @Override
    public void onEnable() {
        instance = this;

        this.executorService = Executors.newScheduledThreadPool(4);

        //initializing files
        initFiles();

        // registering commands
        getCommand("cursedvault").setExecutor(new CommandSpawnVault());

        // loading handlers
        handlerRegistery = new HandlerRegistery();
        handlerRegistery.loadHanders();
    }

    @Override
    public void onDisable() {
        handlerRegistery.unloadHandlers();
    }

    private void initFiles() {
        userData = new AbstractFile(this, "userdata.json", false);
    }

    public static Main getInstance() {
        return instance;
    }

    public HandlerRegistery getHandlerRegistery() {
        return handlerRegistery;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public AbstractFile getUserData() {
        return userData;
    }
}
