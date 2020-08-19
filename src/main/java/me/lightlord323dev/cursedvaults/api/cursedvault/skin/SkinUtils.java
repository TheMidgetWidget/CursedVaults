package me.lightlord323dev.cursedvaults.api.cursedvault.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by Luda on 8/19/2020.
 */
public class SkinUtils {

    public static ItemStack getCustomTextureHead(String value) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField = null;


        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);

            Field name = profile.getClass().getDeclaredField("name");
            name.setAccessible(true);
            name.set(profile, "x");

            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public static boolean isValidSkin(ItemStack itemStack) {
        return itemStack.getType().isBlock() || itemStack.getType() == Material.SKULL_ITEM;
    }

}
