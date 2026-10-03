package com.untistore.clans.inventorys.clangui;

import com.untistore.clans.inventorys.IInventory;
import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.managers.QuestManager;
import com.untistore.clans.models.Clan;
import com.untistore.clans.utils.ComponentUtil;
import com.untistore.clans.utils.InventoryUtil;
import com.untistore.clans.utils.ItemStackBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClanMenu implements IInventory {
    private Inventory inv;
    private ClanManager clanManager;
    private QuestManager questManager;
    private Player player;

    public ClanMenu(ClanManager clanManager, QuestManager questManager) {
        this.clanManager = clanManager;
        this.questManager = questManager;
    }

    public void create() {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(ComponentUtil.create("&c[!] You are not in a clan.", true));
            return;
        }

        inv = Bukkit.createInventory(this, 9*3, Component.text("[" + clan.getName() + "] - Main Menu"));

        InventoryUtil.fillInventory(inv, ItemStackBuilder.buildItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of()));
        inv.setItem(4, ItemStackBuilder.buildSkullItem(player.getName(), "&f" + clan.getName(), List.of(
                ComponentUtil.create("&fLevel" + clan.getLevel() + " (progress/required)", true),
                ComponentUtil.create("&fMembers " + clan.getMembers().size(), true)
        )));
        inv.setItem(11, ItemStackBuilder.buildItem(Material.WRITABLE_BOOK, "&fQuests", List.of(
                ComponentUtil.create("&f[Click] to view the clans quests", true),
                ComponentUtil.create("&f*Resets once every 24hrs*", true)
        )));
        inv.setItem(15, ItemStackBuilder.buildSkullItem(player.getName(), "&fMembers", List.of(
                ComponentUtil.create("&f[Click] to view the clans members", true)
        )));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        int slot  = e.getRawSlot();
        System.out.println("Slot " + slot + " clicked!");
        switch (slot) {
            case 11 -> {
                new ClanQuestMenu(clanManager, questManager).showPlayer(player);
            }
        }
    }

    @Override
    public void onClose(InventoryClickEvent e) {

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    @Override
    public void showPlayer(Player player) {
        this.player = player;
        create();
        player.openInventory(inv);
    }
}
