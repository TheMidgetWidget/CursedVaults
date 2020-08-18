package me.lightlord323dev.cursedvaults.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khalid on 2/2/2018.
 */
public class LocationUtils {

    public static String serializeLocation(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }

    public static Location deserializeLocation(String s) {
        String[] info = s.split(";");
        return new Location(Bukkit.getWorld(info[0]), Integer.valueOf(info[1]), Integer.valueOf(info[2]), Integer.valueOf(info[3]), Float.valueOf(info[4]), Float.valueOf(info[5]));
    }
}
