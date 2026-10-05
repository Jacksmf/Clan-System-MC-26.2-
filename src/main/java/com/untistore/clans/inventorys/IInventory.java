package com.untistore.clans.inventorys;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public interface IInventory extends InventoryHolder {
    Player getPlayer();

    public void onClick(InventoryClickEvent e);
    default void onRightClick(InventoryClickEvent e) {
        onClick(e);
    }

    default void onClose(InventoryCloseEvent e) {}

    default void onOpen(InventoryOpenEvent e) {}

    default void showPlayer(Player player) {

        player.openInventory(getInventory());
    }
}
