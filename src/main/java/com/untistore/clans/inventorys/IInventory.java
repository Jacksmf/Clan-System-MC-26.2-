package com.untistore.clans.inventorys;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface IInventory extends InventoryHolder {

    public void onClick(InventoryClickEvent e);
    default void onRightClick(InventoryClickEvent e) {
        onClick(e);
    }

    default void onClose(InventoryClickEvent e) {}

    default void onOpen(InventoryClickEvent e) {}

    default void showPlayer(Player player) {
        player.openInventory(getInventory());
    }
}
