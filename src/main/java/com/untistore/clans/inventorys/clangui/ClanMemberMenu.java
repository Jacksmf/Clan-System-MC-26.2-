package com.untistore.clans.inventorys.clangui;

import com.untistore.clans.inventorys.IInventory;
import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.utils.ComponentUtil;
import com.untistore.clans.utils.InventoryUtil;
import com.untistore.clans.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.List;
import java.util.stream.Collectors;

public class ClanMemberMenu extends ClanGUI {
    private ClanManager clanManager;
    private IInventory previousInventory;

    public ClanMemberMenu(Player player, ClanManager clanManager, IInventory previousInventory) {
        super(player, 9*5, "");
        this.clanManager = clanManager;
        this.previousInventory = previousInventory;
    }

    private void create() {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if(clan == null) return;

        InventoryUtil.fillInventory(inventory, ItemStackBuilder.buildItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of()));

        List<String> members = new java.util.ArrayList<>(clan.getMembers().stream()
                .map(ClanPlayer::getName)
                .toList());

        members.add(Bukkit.getOfflinePlayer(clan.getOwner()).getName());

        int slot = 10;


        for (String s : members) {
            if (slot >= inventory.getSize()) return;


            inventory.setItem(slot, ItemStackBuilder.buildSkullItem(s, "&f" + s, List.of(
                    ComponentUtil.create("", true)
            )));

            if (slot % 9 == 7) {
                slot += 3;
            } else {
                slot++;
            }
        }
    }


    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        previousInventory.showPlayer(player);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            e.getInventory().close();
            return;
        }

        int memberCount = clan.getMembers().size() + 1;
        e.titleOverride(ComponentUtil.create("&f[" + clan.getName() + "] - Clan Members (" + memberCount + ")", true));
    }

    @Override
    public void showPlayer(Player player) {
        create();
        player.openInventory(inventory);
    }
}
