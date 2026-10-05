package com.untistore.clans.managers;

import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.models.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ClanManager {
    private Plugin plugin;
    private StorageManager storageManager;

    private Random random = new Random();

    private Map<Integer, Clan> activeClans = new HashMap<>();
    private Map<UUID, ClanPlayer> clanPlayers = new HashMap<>();

    private final QuestManager questManager;

    public ClanManager(Plugin plugin, StorageManager storageManager, QuestManager questManager) {
        this.plugin = plugin;
        this.storageManager = storageManager;
        this.questManager = questManager;

        loadClans();
        loadClanPlayers();
    }

    private void loadClans() {
        File[] clans = storageManager.getClansFolder().listFiles();
        if (clans == null) return;

        for (File clanFile : clans) {
            if (!clanFile.getName().endsWith(".json")) continue;

            String id = clanFile.getName().replace(".json", "");
            Clan clanObj = storageManager.loadClan(Integer.parseInt(id));
            if (clanObj == null) return;

            activeClans.put(clanObj.getClanId(), clanObj);
        }
    }

    private void loadClanPlayers() {
        File[] clanPlayerFiles = storageManager.getPlayerFolder().listFiles();
        if (clanPlayerFiles == null) return;

        for (File playerFile : clanPlayerFiles) {
            if (!playerFile.getName().endsWith(".json")) continue;

            UUID uuid = UUID.fromString(playerFile.getName().replace(".json", ""));
            ClanPlayer clanPlayerObj = storageManager.loadClanPlayer(uuid);
            if (clanPlayerObj == null) return;

            clanPlayers.put(uuid, clanPlayerObj);
        }
    }

    public Clan getClan(String clanId) {
        return activeClans.get(clanId);
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
        int newClanId = random.nextInt(999999);
        Clan clan = new Clan(newClanId, clanName, ownerId);

        List<Quest> quests = questManager.getRandomQuests(3);
        for (Quest q : quests) {
            clan.addActiveQuest(q);
        }

        activeClans.put(clan.getClanId(), clan);

        ClanPlayer ownerProfile = getClanPlayer(ownerId);
        ownerProfile.setClanId(newClanId);

        return true;
    }

    public void renewClanQuest(int clanId, int amount) {
        if (clanId == -1 || amount <= 0) return;

        Clan clan = activeClans.get(clanId);
        if (clan == null) return;

        long lastQuestRenewal = clan.getLastQuestRenewal();
        long nextQuestRenewal = System.currentTimeMillis() + 24 * 60 * 60 * 1000;

        if (lastQuestRenewal < nextQuestRenewal) return;

        var quests = clan.getActiveQuests();
        if (quests.isEmpty()) return;

        clan.setLastQuestRenewal(nextQuestRenewal);

        clan.clearActiveQuests();
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
                renewClanQuest(clan.getClanId(), 3);
            }
        }, 20L * 60L,  20L * 60L);
    }

    public void startSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllData, 20L * 60L, 20L * 60L);
    }

    public void saveAllData() {
        for (Clan clan : activeClans.values()) {
            storageManager.saveClan(clan);
        }

        for (ClanPlayer clanPlayer : clanPlayers.values()) {
            storageManager.saveClanPlayer(clanPlayer);
        }
    }
}