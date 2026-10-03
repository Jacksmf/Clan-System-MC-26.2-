package com.untistore.clans.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    public static void fillInventory(Inventory inv, ItemStack stack) {
        int slots = inv.getSize();
        for (int i = 0; i < slots; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                inv.setItem(i, stack);
            }
        }

    }
}
