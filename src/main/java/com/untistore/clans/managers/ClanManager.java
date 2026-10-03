package com.untistore.clans.managers;

import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.models.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ClanManager {
    private Plugin plugin;

    private Map<String, Clan> activeClans = new HashMap<>();
    private Map<UUID, ClanPlayer> clanPlayers = new HashMap<>();

    private final QuestManager questManager;

    public ClanManager(Plugin plugin, QuestManager questManager) {
        this.plugin = plugin;
        this.questManager = questManager;
    }

    public Clan getClan(String clanName) {
        return activeClans.get(clanName);
    }

    public ClanPlayer getClanPlayer(@NotNull UUID uuid) {
        return clanPlayers.computeIfAbsent(uuid, k -> new ClanPlayer(uuid));
    }

    public Collection<Clan> getTopClans(int limit) {
        return activeClans.values().stream()
                .sorted((c1, c2) -> Integer.compare(c2.getLevel(), c1.getLevel()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Clan getClanByPlayer(@NotNull UUID uuid) {
        for (Clan clan : activeClans.values()) {
            if (clan.getOwner().equals(uuid)) return clan;
            if (clan.getMembers().contains(uuid)) return clan;
        }
        return null;
    }

    public boolean createClan(@NotNull UUID ownerId, String clanName) {
        String newClanId = UUID.randomUUID().toString();
        Clan clan = new Clan(newClanId, clanName, ownerId);

        List<Quest> quests = questManager.getRandomQuests(3);
        for (Quest q : quests) {
            clan.addActiveQuest(q);
        }

        activeClans.put(clan.getName(), clan);

        ClanPlayer ownerProfile = getClanPlayer(ownerId);
        ownerProfile.setClanId(newClanId);

        return true;
    }

    public void renewClanQuest(String clanName, int amount) {
        if (clanName == null || amount <= 0) return;

        Clan clan = activeClans.get(clanName);
        if (clan == null) return;

        var quests = clan.getActiveQuests();
        if (quests.isEmpty()) return;

        quests.clear();
        var newQuests = questManager.getRandomQuests(amount);
        for (Quest q : newQuests) {
            clan.addActiveQuest(q);
        }
    }

    public void ensureClansHaveQuests() {
        for (Clan clan : activeClans.values()) {
            if (clan.getActiveQuests().isEmpty()) {
                List<Quest> quests = questManager.getRandomQuests(3);
                for (Quest q : quests) {
                    clan.addActiveQuest(q);
                }
            }
        }
    }

    public void startQuestRenewalTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Clan clan : activeClans.values()) {
                renewClanQuest(clan.getName(), 3);
            }
        }, 20L * 60L,  20L * 60L);
    }

}