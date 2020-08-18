package me.lightlord323dev.cursedvaults.handler;

import me.lightlord323dev.cursedvaults.Main;
import me.lightlord323dev.cursedvaults.api.cursedvault.CursedVault;
import me.lightlord323dev.cursedvaults.api.cursedvault.FilterMode;
import me.lightlord323dev.cursedvaults.api.gui.itemmenu.GUIItemMenu;
import me.lightlord323dev.cursedvaults.api.gui.optionmenu.GUIOptionMenu;
import me.lightlord323dev.cursedvaults.api.gui.optionmenu.OptionItem;
import me.lightlord323dev.cursedvaults.api.handler.Handler;
import me.lightlord323dev.cursedvaults.util.ItemBuilder;
import me.lightlord323dev.cursedvaults.util.NBTApi;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 8/17/2020.
 */
public class CursedVaultInventoryHandler implements Handler, Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getInventory() != null && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            NBTApi nbtApi = new NBTApi(e.getCurrentItem());
            if (nbtApi.hasKey("cvClickable")) {
                e.setCancelled(true);

                if (nbtApi.hasKey("cvUUID")) {
                    CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(UUID.fromString(nbtApi.getString("cvUUID")));
                    if (cursedVault != null) {
                        int page = 1;
                        if (nbtApi.hasKey("cvPage")) {
                            page = nbtApi.getInt("cvPage");
                        }
                        Inventory inventory = new GUIItemMenu(cursedVault.getUniqueId(), ChatColor.translateAlternateColorCodes('&', cursedVault.getDisplayName()), 54, cursedVault.getSize(), page, cursedVault.getItems()).getInventory();
                        inventory.setItem(4, new OptionItem(Material.CHEST, ChatColor.GOLD + "Options", ChatColor.GRAY + "Click to view vault options", 0, "menu").getItemStack());
                        e.getWhoClicked().openInventory(inventory);
                        e.getWhoClicked().setMetadata("cvInv", new FixedMetadataValue(Main.getInstance(), page));
                    }
                }

                // OPTIONS 21 23 25
                if (nbtApi.hasKey("cvOption")) {
                    CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault(((Player) e.getWhoClicked()));
                    switch (nbtApi.getString("cvOption")) {
                        case "menu": {
                            Inventory inventory = new GUIOptionMenu(ChatColor.BLUE + "Vault options", 54, Arrays.asList(
                                    new OptionItem(Material.NAME_TAG, ChatColor.GREEN + "Change vault name", ChatColor.GRAY + "Click to change the name which appears above your vault", 20, "nameChange"),
                                    new OptionItem(Material.SAPLING, ChatColor.BLUE + "View upgrades", ChatColor.GRAY + "Click to view available upgrades for your vault", 22, "vaultUpgrade"),
                                    new OptionItem(Material.NETHER_STAR, ChatColor.RED + "Vault settings", ChatColor.GRAY + "Click to modify your vault's settings", 24, "vaultSettings")
                            )).getInventory();
                            e.getWhoClicked().openInventory(inventory);
                            break;
                        }
                        // MAIN MENU
                        case "nameChange":
                            if (cursedVault != null) {
                                new AnvilGUI.Builder().onComplete((player, name) -> {
                                    if (name.length() < 3) {
                                        player.sendMessage(ChatColor.RED + "The display name must be longer than that.");
                                        return AnvilGUI.Response.close();
                                    }
                                    cursedVault.setDisplayName(name);
                                    return AnvilGUI.Response.close();
                                }).text(cursedVault.getDisplayName()).plugin(Main.getInstance()).open(((Player) e.getWhoClicked()));
                            }
                            break;
                        case "vaultUpgrade":
                            Inventory upgradesInv = new GUIOptionMenu(ChatColor.BLUE + "Vault upgrades", 54, Arrays.asList(
                                    new OptionItem(Material.STONE, 20, "cvSizeUp", ChatColor.GREEN + "Add storage slot", ChatColor.GRAY + "Click to add a storage slot to your vault", " ", ChatColor.GRAY + "Current size: " + ChatColor.GOLD + cursedVault.getSize(), " ", " "),
                                    new OptionItem(Material.FEATHER, 22, "cvSpeedUp", ChatColor.BLUE + "Speed upgrade", ChatColor.GRAY + "Click to increase your vault's speed", " ", ChatColor.GRAY + "Current speed: " + ChatColor.GOLD + cursedVault.getSpeed() + ChatColor.GRAY + " (Max speed = 5)", " ", " "),
                                    new OptionItem(Material.COMPASS, 24, "cvRadiusUp", ChatColor.RED + "Pickup radius", ChatColor.GRAY + "Click to increase your vault's pickup radius", " ", ChatColor.GRAY + "Current radius: " + ChatColor.GOLD + cursedVault.getPickupRadius() + ChatColor.GRAY + " (Max radius = 4)", " ", " ")
                            )).getInventory();
                            e.getWhoClicked().openInventory(upgradesInv);
                            break;
                        case "vaultSettings": {
                            String autoPickupLore = "This vault auto-pickup is ";
                            if (cursedVault.getCanPickUp())
                                autoPickupLore = autoPickupLore + ChatColor.GREEN + "Enabled";
                            else
                                autoPickupLore = autoPickupLore + ChatColor.RED + "Disabled";

                            String haltLore = ChatColor.GRAY + "This vault's movement is ";
                            if (cursedVault.isCanMove())
                                haltLore = haltLore + ChatColor.GREEN + "not halted";
                            else
                                haltLore = haltLore + ChatColor.RED + "halted";

                            Inventory inventory = new GUIOptionMenu(ChatColor.BLUE + "Vault options", 54, Arrays.asList(
                                    new OptionItem(Material.CHEST, ChatColor.GREEN + "Pickup vault", ChatColor.GRAY + "Click to pickup your vault", 20, "vaultPickup"),
                                    new OptionItem(Material.BARRIER, ChatColor.BLUE + "Toggle vault auto-pickup", ChatColor.GRAY + autoPickupLore, 22, "vaultToggle"),
                                    new OptionItem(Material.CAULDRON_ITEM, ChatColor.RED + "Halt", haltLore, 24, "vaultHalt"),
                                    new OptionItem(Material.CAULDRON_ITEM, ChatColor.DARK_PURPLE + "Filter mode", ChatColor.GRAY + "Current filter mode: " + ChatColor.GOLD + cursedVault.getFilterMode().toString(), 28, "vaultFilter")
                            )).getInventory();
                            e.getWhoClicked().openInventory(inventory);
                            break;
                        }
                        // SETTINGS MENU
                        case "vaultPickup":
                            // TODO SAVE VAULT CONTENTS ON DISK
                            if (e.getWhoClicked().getInventory().firstEmpty() == -1) {
                                e.getWhoClicked().sendMessage(ChatColor.RED + "Your Inventory is full.");
                                e.getWhoClicked().closeInventory();
                            } else {
                                ItemStack vaultItem = new NBTApi(new ItemBuilder(Material.CHEST).setDisplayName(ChatColor.translateAlternateColorCodes('&', cursedVault.getDisplayName())).setLore(ChatColor.GRAY + "Place to spawn this vault").build()).setString("vaultUUID", cursedVault.getUniqueId().toString()).getItemStack();
                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().getInventory().addItem(vaultItem);
                                Main.getInstance().getHandlerRegistery().getCursedVaultHandler().saveAndUnregisterVault(cursedVault);
                            }
                            break;
                        case "vaultToggle":
                            cursedVault.setCanPickUp(!cursedVault.getCanPickUp());
                            String autoPickupLore = ChatColor.GRAY + "This vault auto-pickup is ";
                            if (cursedVault.getCanPickUp())
                                autoPickupLore = autoPickupLore + ChatColor.GREEN + "Enabled";
                            else
                                autoPickupLore = autoPickupLore + ChatColor.RED + "Disabled";
                            e.setCurrentItem(new ItemBuilder(e.getCurrentItem()).setLore(autoPickupLore).build());
                            break;
                        case "vaultHalt":
                            cursedVault.setCanMove(!cursedVault.isCanMove());
                            String haltLore = ChatColor.GRAY + "This vault's movement is ";
                            if (cursedVault.isCanMove())
                                haltLore = haltLore + ChatColor.GREEN + "not halted";
                            else
                                haltLore = haltLore + ChatColor.RED + "halted";
                            e.setCurrentItem(new ItemBuilder(e.getCurrentItem()).setLore(haltLore).build());
                            break;
                        case "vaultFilter":
                            switch (cursedVault.getFilterMode()) {
                                case NONE:
                                    cursedVault.setFilterMode(FilterMode.BLACKLIST);
                                    break;
                                case BLACKLIST:
                                    cursedVault.setFilterMode(FilterMode.WHITELIST);
                                    break;
                                case WHITELIST:
                                    cursedVault.setFilterMode(FilterMode.NONE);
                                    break;
                            }
                            e.setCurrentItem(new ItemBuilder(e.getCurrentItem()).setLore(ChatColor.GRAY + "Current filter mode: " + ChatColor.GOLD + cursedVault.getFilterMode().toString()).build());
                            break;
                        // UPGRADES MENU
                        case "cvSizeUp":
                            cursedVault.setSize(cursedVault.getSize() + 1);
                            e.setCurrentItem(new ItemBuilder(e.getCurrentItem()).setLore(ChatColor.GRAY + "Click to add a storage slot to your vault", " ", ChatColor.GRAY + "Current size: " + ChatColor.GOLD + cursedVault.getSize(), " ", " ").build());
                            break;
                        case "cvSpeedUp":
                            if (cursedVault.getSpeed() < 5) {
                                cursedVault.setSpeed(cursedVault.getSpeed() + 1);
                                e.setCurrentItem(new ItemBuilder(e.getCurrentItem()).setLore(ChatColor.GRAY + "Click to increase your vault's speed", " ", ChatColor.GRAY + "Current speed: " + ChatColor.GOLD + cursedVault.getSpeed() + ChatColor.GRAY + " (Max speed = 5)", " ", " ").build());
                            }
                            break;
                        case "cvRadiusUp":
                            if (cursedVault.getPickupRadius() < 4) {
                                cursedVault.setPickupRadius(cursedVault.getPickupRadius() + 1);
                                e.setCurrentItem(new ItemBuilder(e.getCurrentItem()).setLore(ChatColor.GRAY + "Click to increase your vault's pickup radius", " ", ChatColor.GRAY + "Current radius: " + ChatColor.GOLD + cursedVault.getPickupRadius() + ChatColor.GRAY + " (Max radius = 4)", " ", " ").build());
                            }
                            break;
                    }
                }

            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player))
            return;
        if (e.getPlayer().hasMetadata("cvInv")) {
            CursedVault cursedVault = Main.getInstance().getHandlerRegistery().getCursedVaultHandler().getCursedVault((Player) e.getPlayer());
            if (cursedVault != null) {
                List<ItemStack> items = new ArrayList<>();
                for (ItemStack itemStack : e.getInventory().getContents()) {
                    if (itemStack == null || itemStack.getType() == Material.AIR) {
                        items.add(new ItemStack(Material.AIR));
                    } else if (!(new NBTApi(itemStack).hasKey("cvClickable"))) {
                        items.add(itemStack);
                    }
                }

                int page = Integer.parseInt(e.getPlayer().getMetadata("cvInv").get(0).asString());

                List<ItemStack> vaultItems = cursedVault.getItems();
                int startIndex = 28 * (page - 1);
                int max = cursedVault.getSize() < 28 ? cursedVault.getSize() : 28;
                for (int i = 0; i < max; i++) {
                    int actualIndex = startIndex + i;
                    if (actualIndex >= cursedVault.getSize())
                        break;
                    if (items.get(i) == null || items.get(i).getType() == Material.AIR)
                        vaultItems.set(actualIndex, new ItemStack(Material.AIR));
                    else {
                        vaultItems.set(actualIndex, items.get(i));
                    }
                }

                e.getPlayer().removeMetadata("cvInv", Main.getInstance());
            }
        }
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
    }

}
