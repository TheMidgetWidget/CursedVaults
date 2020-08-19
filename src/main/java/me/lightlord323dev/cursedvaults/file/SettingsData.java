package me.lightlord323dev.cursedvaults.file;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.util.file.AbstractFile;

/**
 * Created by Luda on 8/19/2020.
 */
public class SettingsData extends AbstractFile {

    private final int AUTOSAVE_PERIOD, STORAGE_MAX, SPEED_MAX, RADIUS_MAX;
    private final double STORAGE_PRICE, SPEED_PRICE, RADIUS_PRICE;

    public SettingsData(Main main) {
        super(main, "settings.yml", true);
        AUTOSAVE_PERIOD = getConfig().getInt("settings.auto-save-period");
        STORAGE_PRICE = getConfig().getDouble("upgrade-prices.storage-increase-price");
        SPEED_PRICE = getConfig().getDouble("upgrade-prices.speed-increase-price");
        RADIUS_PRICE = getConfig().getDouble("upgrade-prices.radius-increase-price");
        STORAGE_MAX = getConfig().getInt("upgrade-settings.max-storage-upgrade");
        SPEED_MAX = getConfig().getInt("upgrade-settings.max-speed-upgrade");
        RADIUS_MAX = getConfig().getInt("upgrade-settings.max-radius-upgrade");
    }

    public int getAutosavePeriod() {
        return AUTOSAVE_PERIOD;
    }

    public double getStoragePrice() {
        return STORAGE_PRICE;
    }

    public double getSpeedPrice() {
        return SPEED_PRICE;
    }

    public double getRadiusPrice() {
        return RADIUS_PRICE;
    }

    public int getMaxSpeed() {
        return SPEED_MAX;
    }

    public int getMaxRadius() {
        return RADIUS_MAX;
    }

    public int getMaxStorage() {
        return STORAGE_MAX;
    }
}
