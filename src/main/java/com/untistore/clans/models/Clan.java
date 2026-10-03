package com.untistore.clans.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Clan {
    private String clanName;
    private String clanId;
    private UUID ownerId;
    private Set<UUID> members = new HashSet<>();

    private Location clanHome;

    private int level = 1;
    private int experience = 0;
    private double levelUpMultiplier = 1.25;

    // This tracks if the clan as a whole finished a quest (if you still want clan-wide quests)
    private Set<String> completedQuestIds = new HashSet<>();
    private Map<String, Boolean> settings = new HashMap<>();
    private Set<Quest> activeQuests = new HashSet<>();

    public Clan(String id, String name, UUID ownerId) {
        this.clanId = id;
        this.clanName = name;
        this.ownerId = ownerId;
        this.settings.put("friendly_fire", false);
        this.settings.put("invite_only", true);
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public void completeQuest(String questId) { this.completedQuestIds.add(questId); }
    public void addActiveQuest(Quest quest) { this.activeQuests.add(quest); }
    public void removeActiveQuest(Quest quest) { this.activeQuests.remove(quest); }

    public List<Quest> getActiveQuests() { return new ArrayList<>(this.activeQuests); }
    public boolean hasCompletedQuest(String questId) { return this.completedQuestIds.contains(questId); }

    public String getName() { return this.clanName; }

    public List<Player> getOnlineMembers() {
        List<Player> onlineMembers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.members.contains(player.getUniqueId()) || this.ownerId.equals(player.getUniqueId())) {
                onlineMembers.add(player);
            }
        }
        return onlineMembers;
    }

    public Location getHome() { return this.clanHome; }
    public void setHome(Location clanHome) { this.clanHome = clanHome; }

    public UUID getOwner() { return this.ownerId; }
    public Collection<UUID> getMembers() { return new ArrayList<>(this.members); }
}