package com.untistore.clans.inventorys.clangui;

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
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClanMenu extends ClanGUI {
    private ClanManager clanManager;
    private QuestManager questManager;

    private Player player;

    public ClanMenu(Player player, ClanManager clanManager, QuestManager questManager) {
        super(player, 9*3, "");

        this.player = player;
        this.clanManager = clanManager;
        this.questManager = questManager;

        create();
    }

    public void create() {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(ComponentUtil.create("&c[!] You are not in a clan.", true));
            return;
        }

        InventoryUtil.fillInventory(inventory, ItemStackBuilder.buildItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of()));
        inventory.setItem(4, ItemStackBuilder.buildSkullItem(player.getName(), "&f" + clan.getName(), List.of(
                ComponentUtil.create("&fLevel " + clan.getLevel() + " (" + clan.getLevelProgress() + ")", true),
                ComponentUtil.create("&fMembers " + clan.getMembers().size(), true)
        )));
        inventory.setItem(11, ItemStackBuilder.buildItem(Material.WRITABLE_BOOK, "&fQuests", List.of(
                ComponentUtil.create("&f[Click] to view the clans quests", true),
                ComponentUtil.create("&f*Resets once every 24hrs*", true)
        )));
        inventory.setItem(15, ItemStackBuilder.buildSkullItem(player.getName(), "&fMembers", List.of(
                ComponentUtil.create("&f[Click] to view the clans members", true)
        )));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        int slot  = e.getRawSlot();
        switch (slot) {
            case 11 -> {
                new ClanQuestMenu(player, clanManager, questManager, this).showPlayer(player);
            }
            case 15 -> {
                new ClanMemberMenu(player, clanManager, this).showPlayer(player);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        Clan clan =  clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            e.getInventory().close();
            return;
        }

        e.titleOverride(ComponentUtil.create("&f[" + clan.getName() + "] - Main Menu", true));
    }

    @Override
    public void showPlayer(Player player) {
        create();
        player.openInventory(inventory);
    }
}
