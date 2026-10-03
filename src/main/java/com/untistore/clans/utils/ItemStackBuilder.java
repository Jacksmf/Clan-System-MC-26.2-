package com.untistore.clans.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemStackBuilder {
    public static ItemStack buildItem(Material mat, String name, List<Component> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ComponentUtil.create("&f" + name, true));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack buildSkullItem(String skullName, String name, List<Component> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(skullName));
        meta.displayName(ComponentUtil.create(name, true));
        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
