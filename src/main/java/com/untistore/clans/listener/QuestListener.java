package com.untistore.clans.listener;

import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.models.Quest;
import com.untistore.clans.utils.ItemStackBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;

public class QuestListener implements Listener {
    private final ClanManager clanManager;

    public QuestListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity().getKiller() == null) return;

        progressQuest(e.getEntity().getKiller(), "KILL", e.getEntity().getType().name());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        progressQuest(e.getPlayer(), "MINE", e.getBlock().getType().name());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        progressQuest(e.getPlayer(), "PLACE", e.getBlock().getType().name());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        if (e.isCancelled()) return;
        progressQuest(e.getPlayer(), "CONSUME", e.getItem().getType().name());
    }

    private void progressQuest(Player player, String type, String targetName) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) return;

        List<Quest> quests = clan.getActiveQuests();
        if (quests.isEmpty()) return;

        ClanPlayer clanPlayer = clanManager.getClanPlayer(player.getUniqueId());
        if (clanPlayer == null) return;

        for (Quest q : quests) handleQuest(player, type, targetName, q, clanPlayer);
    }

    private void handleQuest(Player player, String type, String targetName, Quest q, ClanPlayer clanPlayer) {
        String formattedTarget = targetName.replace("DEEPSLATE_", "");

        if (!q.getType().contains(type)) return;
        if (!q.getTarget().equalsIgnoreCase(formattedTarget)) return;
        if (clanPlayer.hasCompletedQuest(q.getQuestId())) return;

        clanPlayer.incrementQuestProgress(q.getQuestId(), 1);
        if (clanPlayer.getQuestProgress(q.getQuestId()) >= q.getRequiredAmount()) {
            Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
            clanPlayer.completeQuest(q.getQuestId());
            clan.addExperience(q.getRequiredAmount() / 2);

            handleRewards(q, player);
            String quest = q.getDescription() == null ? q.getQuestId() : q.getDescription();
            player.sendMessage(Component.text("Quest " + quest + " completed!"));
        }
    }

    private void handleRewards(Quest quest, Player player) {
        if (quest.getRewardType() == null) return;

        String rewardType = quest.getRewardType();
        switch (rewardType.toLowerCase()) {
            case "command" -> {
                for (String cmd : quest.getRewards()) {
                    String format = cmd.replaceAll("%player%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), format);
                }
            }
            case "item" -> {
                for (String item : quest.getRewards()) {
                    Material mat =  Material.matchMaterial(item);
                    if (mat == null) continue;

                    player.getInventory().addItem(ItemStackBuilder.buildItem(mat, mat.data.getName(), List.of()));
                }
            }
            default -> {}
        }
    }
}