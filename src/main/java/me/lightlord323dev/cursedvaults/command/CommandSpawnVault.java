package me.lightlord323dev.cursedvaults.command;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Luda on 8/16/2020.
 */
public class CommandSpawnVault implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;

        if (args.length == 0) {
            CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(player);
            if (cursedVault != null) {
                player.sendMessage(ChatColor.RED + "You already have a vault spawned in.");
                return true;
            } else {
                cursedVault = new CursedVault(player.getUniqueId(), 9, 3, 1.0f, player.getLocation());
                Main.getInstance().getHandlerRegistery().getCursedVaultHandler().registerCursedVault(cursedVault);
            }
        }

        return true;
    }
}
