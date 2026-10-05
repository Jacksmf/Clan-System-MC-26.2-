package com.untistore.clans.inventorys.clangui;

import com.untistore.clans.inventorys.IInventory;
import com.untistore.clans.utils.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;

public abstract class ClanGUI implements IInventory {
    protected Inventory inventory;
    protected Player player;

    public ClanGUI(Player player, int size, String title) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, size, ComponentUtil.create(title, true));
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public @NonNull Inventory getInventory() {
        return inventory;
    }

}
