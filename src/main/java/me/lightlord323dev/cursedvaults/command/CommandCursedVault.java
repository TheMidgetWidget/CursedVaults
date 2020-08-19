package me.lightlord323dev.cursedvaults.command;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import me.lightlord323dev.cursedvaults.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by Luda on 8/16/2020.
 */
public class CommandCursedVault implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender.hasPermission("cursedvaults.admin")) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        MessageUtil.error(sender, "Player not found.");
                        return true;
                    }
                    ItemStack vaultItem = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().createVaultItem(target);
                    MessageUtil.success(target, "You have been granted a vault.");
                    MessageUtil.success(sender, args[1] + " has been granted a vault.");
                    if (target.getInventory().firstEmpty() == -1) {
                        MessageUtil.error(target, "Your inventory is full. The vault has been dropped on the ground.");
                        target.getWorld().dropItemNaturally(target.getLocation(), vaultItem);
                    } else {
                        target.getInventory().addItem(vaultItem);
                    }
                    return true;
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("giveskin")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        MessageUtil.error(sender, "Player not found.");
                        return true;
                    }
                    if (!isInt(args[2])) {
                        MessageUtil.error(sender, "You must enter a valid number for the id of the skin.");
                        return true;
                    }
                    int id = Integer.parseInt(args[2]);

                    if (!Main.getInstance().getHandlerRegistery().getSkinHandler().isValidId(id)) {
                        MessageUtil.error(sender, "Invalid id.");
                        return true;
                    }

                    ItemStack skin = Main.getInstance().getHandlerRegistery().getSkinHandler().getSkin(id);
                    MessageUtil.success(target, "You have been given a vault skin.");
                    MessageUtil.success(sender, args[1] + " has been given a vault skin.");
                    if (target.getInventory().firstEmpty() == -1) {
                        MessageUtil.error(target, "Your inventory is full. The skin has been dropped on the ground.");
                        target.getWorld().dropItemNaturally(target.getLocation(), skin);
                    } else {
                        target.getInventory().addItem(skin);
                    }
                    return true;
                }
            }
        }


        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("filter")) {
                CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(player);
                if (cursedVault == null) {
                    MessageUtil.error(player, "You must have a vault spawned in to add an item filter.");
                    return true;
                }
                if (args[1].equalsIgnoreCase("list")) {
                    MessageUtil.info(player, ChatColor.GOLD + "Item filter list:");
                    cursedVault.getFilterList().forEach(material -> player.sendMessage(ChatColor.RED + material.toString()));
                    return true;
                }
                if (args[1].equalsIgnoreCase("clear")) {
                    cursedVault.setFilterList(new ArrayList<>());
                    MessageUtil.success(player, "Filter list cleared.");
                    return true;
                }
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("filter")) {
                CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(player);
                if (cursedVault == null) {
                    MessageUtil.error(player, "You must have a vault spawned in to add an item filter.");
                    return true;
                }

                Material material = Material.matchMaterial(args[2]);

                if (material == null) {
                    MessageUtil.error(player, "The specified material is not valid.");
                    return true;
                }

                if (args[1].equalsIgnoreCase("add")) {
                    if (cursedVault.getFilterList().contains(material)) {
                        MessageUtil.error(player, "The specified material is already in the filter list.");
                        return true;
                    }
                    cursedVault.addFilterItem(material);
                    MessageUtil.success(player, material.toString() + " was added to the filter list.");
                    return true;
                } else if (args[1].equalsIgnoreCase("remove")) {
                    if (!cursedVault.getFilterList().contains(material)) {
                        MessageUtil.error(player, "The specified material is not in the filter list.");
                        return true;
                    }
                    cursedVault.removeFilterItem(material);
                    MessageUtil.success(player, "The specified material was removed from the filter list.");
                    return true;
                }
            }
        }

        return true;
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
