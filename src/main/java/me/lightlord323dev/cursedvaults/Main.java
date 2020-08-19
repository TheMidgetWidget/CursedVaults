package me.lightlord323dev.cursedvaults;

import me.lightlord323dev.cursedvaults.api.handler.HandlerRegistery;
import me.lightlord323dev.cursedvaults.command.CommandCursedVault;
import me.lightlord323dev.cursedvaults.file.IconData;
import me.lightlord323dev.cursedvaults.file.SettingsData;
import me.lightlord323dev.cursedvaults.util.file.AbstractFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    private SettingsData settingsData;
    private IconData iconData;

    // vault
    private Economy econ = null;

    @Override
    public void onEnable() {
        instance = this;

        this.executorService = Executors.newScheduledThreadPool(4);

        //initializing files
        initFiles();

        // initializing vault
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // registering commands
        getCommand("cursedvault").setExecutor(new CommandCursedVault());

        // loading handlers
        handlerRegistery = new HandlerRegistery();
        handlerRegistery.loadHanders();
    }

    @Override
    public void onDisable() {
        if (handlerRegistery != null)
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
        // SETTINGS DATA
        saveResource("settings.yml", false);
        this.settingsData = new SettingsData(this);
        // ICON DATA
        saveResource("icons.yml", false);
        this.iconData = new IconData(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Main getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return econ;
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

    public SettingsData getSettingsData() {
        return settingsData;
    }

    public IconData getIconData() {
        return iconData;
    }
}
