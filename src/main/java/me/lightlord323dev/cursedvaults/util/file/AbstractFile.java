package me.lightlord323dev.cursedvaults.util.file;

import me.lightlord323dev.cursedvaults.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Luda on 7/17/2020.
 */
public class AbstractFile {

    protected Main main;
    protected File f;
    protected FileConfiguration c;

    public AbstractFile(Main main, String name, boolean yml) {
        this.main = main;

        if (!main.getDataFolder().exists())
            main.getDataFolder().mkdir();

        this.f = new File(main.getDataFolder(), name);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (yml)
            c = YamlConfiguration.loadConfiguration(f);
    }

    public AbstractFile(Main main, String name, String path, boolean yml) {
        this.main = main;

        File dir = new File(main.getDataFolder() + File.separator + path);
        if (!dir.exists())
            dir.mkdirs();

        this.f = new File(main.getDataFolder() + File.separator + path, name);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (yml)
            c = YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getConfig() {
        return c;
    }

    public File getFile() {
        return this.f;
    }

    public void save() {
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
