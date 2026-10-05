package com.untistore.clans.inventorys.clangui;

import com.untistore.clans.inventorys.IInventory;
import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.managers.QuestManager;
import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.models.Quest;
import com.untistore.clans.utils.ComponentUtil;
import com.untistore.clans.utils.InventoryUtil;
import com.untistore.clans.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClanQuestMenu extends ClanGUI {
    private ClanManager clanManager;
    private QuestManager questManager;
    private ClanGUI previousMenu;

    private Player player;

    public ClanQuestMenu(Player player, ClanManager clanManager, QuestManager questManager, ClanGUI previousMenu) {
        super(player, 9*3, "");

        this.clanManager = clanManager;
        this.questManager = questManager;
        this.previousMenu = previousMenu;
        this.player = player;
    }


    public void create() {
        InventoryUtil.fillInventory(inventory, ItemStackBuilder.buildItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of()));

        int startIndex = 10;
        var quests = clanManager.getClanByPlayer(player.getUniqueId()).getActiveQuests();
        ClanPlayer clanPlayer = clanManager.getClanPlayer(player.getUniqueId());

        for (int i = 0; i < startIndex; i++) {
            if (i < quests.size()) {
                Quest quest = quests.get(i);
                String name = "&f" + quest.getType() + " " + quest.getRequiredAmount() + " " + quest.getTarget();
                Material displayItem = quest.getDisplayItem() == null ? Material.PAPER : quest.getDisplayItem();
                ItemStack item = ItemStackBuilder.buildItem(displayItem, name, List.of(
                        ComponentUtil.create(quest.getDescription(), true),
                        ComponentUtil.create("&f" + clanPlayer.getQuestProgress(quest.getQuestId()) + " / " + quest.getRequiredAmount(), true)
                ));

                inventory.setItem(startIndex + i, item);
            }
        }

        inventory.setItem(22, ItemStackBuilder.buildItem(Material.BARRIER, "&cClose", List.of(
                ComponentUtil.create("&fClosing will go to - Main menu", true)
        )));
    }

    private int calculateRows() {
        int quests = clanManager.getClanByPlayer(player.getUniqueId()).getActiveQuests().size();

        return Math.ceilDiv(quests, 9);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            e.getInventory().close();
            return;
        }

        e.titleOverride(ComponentUtil.create("&f[" + clanManager.getClanByPlayer(player.getUniqueId()).getName() + "] - Quest Menu", true));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        int slot = e.getSlot();
        switch (slot) {
            case 22 -> {
                previousMenu.showPlayer(player);
            }
        }
    }


    @Override
    public void showPlayer(Player player) {
        this.player = player;
        create();
        player.openInventory(inventory);
    }
}
