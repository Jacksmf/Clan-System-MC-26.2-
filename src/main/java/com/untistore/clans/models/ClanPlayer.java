package com.untistore.clans.models;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ClanPlayer {
    private UUID playerUUID;
    private int clanId;
    private Role role;

    private Map<String, Integer> questProgress = new HashMap<>();
    private Set<String> questsCompleted = new HashSet<>();
    private int warKills;

    public ClanPlayer(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() { return playerUUID; }

    public int getClanId() { return clanId; }
    public void setClanId(int clanId) { this.clanId = clanId; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public int getWarKills() { return warKills; }
    public void setWarKills(int warKills) { this.warKills = warKills; }

    public int getQuestProgress(String questId) {
        return this.questProgress.getOrDefault(questId, 0);
    }

    public void incrementQuestProgress(String questId, int amount) {
        questProgress.put(questId, getQuestProgress(questId) + amount);
    }

    public boolean hasCompletedQuest(String questId) {
        return questsCompleted.contains(questId);
    }

    public void completeQuest(String questId) {
        questsCompleted.add(questId);
    }

    public static String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}