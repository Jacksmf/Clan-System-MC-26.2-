package com.untistore.clans.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Clan {
    private String clanName;
    private int clanId;
    private UUID ownerId;
    private Set<UUID> members = new HashSet<>();

    private String clanHome = null;

    private int level = 1;
    private int experience = 0;
    private double levelUpMultiplier = 1.35;

    private long lastQuestRenewal;

    private Set<String> completedQuestIds = new HashSet<>();
    private Map<String, Boolean> settings = new HashMap<>();
    private Set<Quest> activeQuests = new HashSet<>();

    public Clan(int id, String name, UUID ownerId) {
        this.clanId = id;
        this.clanName = name;
        this.ownerId = ownerId;
        this.settings.put("friendly_fire", false);
        this.settings.put("invite_only", true);
    }

    public int getClanId() {
        return clanId;
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
    public void clearActiveQuests() { this.activeQuests.clear(); }
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

    public Location getHome() {
        if (clanHome == null) return null;

        String[] parts = clanHome.split(",");
        if (parts.length < 6) return null;

        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }
    public void setHome(Location loc) {
        if (loc == null || loc.getWorld() == null) return;

        this.clanHome = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    public UUID getOwner() { return this.ownerId; }
    public Collection<UUID> getMembers() { return new ArrayList<>(this.members); }

    public long getLastQuestRenewal() {
        return lastQuestRenewal;
    }
    public void setLastQuestRenewal(long lastQuestRenewal) {
        this.lastQuestRenewal = lastQuestRenewal;
    }

    public String getLevelProgress() {
        return this.experience + "/" + (this.level * 10) * levelUpMultiplier;
    }
}