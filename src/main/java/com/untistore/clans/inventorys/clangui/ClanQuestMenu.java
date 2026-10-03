package com.untistore.clans.inventorys.clangui;

import com.untistore.clans.inventorys.IInventory;
import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.managers.QuestManager;
import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.models.Quest;
import com.untistore.clans.utils.ComponentUtil;
import com.untistore.clans.utils.InventoryUtil;
import com.untistore.clans.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClanQuestMenu implements IInventory {
    private Inventory inv;
    private ClanManager clanManager;
    private QuestManager questManager;

    private Player player;

    public ClanQuestMenu(ClanManager clanManager, QuestManager questManager) {
        this.clanManager = clanManager;
        this.questManager = questManager;
    }

    public void create() {
        int rows = Math.max(calculateRows(), 3);
        inv = Bukkit.createInventory(this, 9*rows, ComponentUtil.create("&f[" + clanManager.getClanByPlayer(player.getUniqueId()).getName() + "] - Quest Menu",true));

        InventoryUtil.fillInventory(inv, ItemStackBuilder.buildItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of()));

        int startIndex = 10;
        var quests = clanManager.getClanByPlayer(player.getUniqueId()).getActiveQuests();
        ClanPlayer clanPlayer = clanManager.getClanPlayer(player.getUniqueId());

        System.out.println("Number of quests: " + quests.size());
        for (int i = 0; i < startIndex; i++) {
            if (i < quests.size()) {
                Quest quest = quests.get(i);
                String name = "&f" + quest.getType() + " " + quest.getRequiredAmount() + " " + quest.getTarget();
                ItemStack item = ItemStackBuilder.buildItem(Material.PAPER, name, List.of(
                        ComponentUtil.create("&f" + clanPlayer.getQuestProgress(quest.getQuestId()) + " / " + quest.getRequiredAmount(), true)
                ));

                inv.setItem(startIndex + i, item);
            }
        }

        inv.setItem(22, ItemStackBuilder.buildItem(Material.BARRIER, "&cClose", List.of(
                ComponentUtil.create("&fClosing will go to - Main menu", true)
        )));
    }

    private int calculateRows() {
        int quests = clanManager.getClanByPlayer(player.getUniqueId()).getActiveQuests().size();

        return Math.ceilDiv(quests, 9);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        int slot = e.getSlot();
        switch (slot) {
            case 22 -> {
                new ClanMenu(clanManager, questManager).showPlayer(player);
            }
        }
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
