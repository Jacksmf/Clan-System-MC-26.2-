package com.untistore.clans.listener;

import com.untistore.clans.inventorys.IInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof IInventory iInventory)) return;

        if (e.isLeftClick()) {
            iInventory.onClick(e);
        } else if (e.isRightClick()) {
            iInventory.onRightClick(e);
        }
    }
}
