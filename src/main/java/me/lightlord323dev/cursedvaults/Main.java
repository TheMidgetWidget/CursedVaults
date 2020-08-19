package me.lightlord323dev.cursedvaults;

import me.lightlord323dev.cursedvaults.api.handler.HandlerRegistery;
import me.lightlord323dev.cursedvaults.command.CommandCursedVault;
import me.lightlord323dev.cursedvaults.util.file.AbstractFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
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
    private AbstractFile skinData;

    @Override
    public void onEnable() {
        instance = this;

        this.executorService = Executors.newScheduledThreadPool(4);

        //initializing files
        initFiles();

        // registering commands
        getCommand("cursedvault").setExecutor(new CommandCursedVault());

        // loading handlers
        handlerRegistery = new HandlerRegistery();
        handlerRegistery.loadHanders();
    }

    @Override
    public void onDisable() {
        handlerRegistery.unloadHandlers();
    }

    private void initFiles() {
        // USER DATA
        this.userData = new AbstractFile(this, "userdata.json", false);
        // SKIN DATA
        this.skinData = new AbstractFile(this, "skindata.yml", true);
        if (this.skinData.getConfig().getConfigurationSection("skins") == null) {
            this.skinData.getConfig().set("skins", Arrays.asList("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTczNzY3Y2QzMmU2N2UwM2NkNzg0ODhjMDExZTQwYTEwNDg3Y2IwYjdmNTcyMWEyYjgxMDFlMmQxN2FhNTNkZCJ9fX0="));
            this.skinData.save();
        }
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

    public AbstractFile getSkinData() {
        return skinData;
    }
}
