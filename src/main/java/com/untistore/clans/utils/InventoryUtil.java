package com.untistore.clans.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    public static void fillInventory(Inventory inv, ItemStack stack) {
        int slots = inv.getSize();
        for (int i = 0; i < slots; i++) {
            inv.setItem(i, stack);
        }

    }
}
