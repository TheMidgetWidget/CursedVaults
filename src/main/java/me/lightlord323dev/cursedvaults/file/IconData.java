package me.lightlord323dev.cursedvaults.file;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.gui.optionmenu.OptionItem;
import me.lightlord323dev.cursedvaults.util.file.AbstractFile;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Luda on 8/19/2020.
 */
public class IconData extends AbstractFile {

    private final OptionItem NAME_CHANGE, VAULT_UPGRADE, VAULT_SETTINGS;

    public IconData(Main main) {
        super(main, "icons.yml", true);
        this.NAME_CHANGE = new OptionItem(Material.matchMaterial(getConfig().getString("name-change.material")), ChatColor.translateAlternateColorCodes('&', getConfig().getString("name-change.name")), ChatColor.translateAlternateColorCodes('&', getConfig().getString("name-change.lore")), getConfig().getInt("name-change.slot"), "nameChange");
        this.VAULT_UPGRADE = new OptionItem(Material.matchMaterial(getConfig().getString("vault-upgrade.material")), ChatColor.translateAlternateColorCodes('&', getConfig().getString("vault-upgrade.name")), ChatColor.translateAlternateColorCodes('&', getConfig().getString("vault-upgrade.lore")), getConfig().getInt("vault-upgrade.slot"), "vaultUpgrade");
        this.VAULT_SETTINGS = new OptionItem(Material.matchMaterial(getConfig().getString("vault-settings.material")), ChatColor.translateAlternateColorCodes('&', getConfig().getString("vault-settings.name")), ChatColor.translateAlternateColorCodes('&', getConfig().getString("vault-settings.lore")), getConfig().getInt("vault-settings.slot"), "vaultSettings");
    }

    public List<OptionItem> getMainIcons() {
        return Arrays.asList(NAME_CHANGE, VAULT_UPGRADE, VAULT_SETTINGS);
    }

    public OptionItem getNameChange() {
        return NAME_CHANGE;
    }

    public OptionItem getVaultUpgrade() {
        return VAULT_UPGRADE;
    }

    public OptionItem getVaultSettings() {
        return VAULT_SETTINGS;
    }
}
