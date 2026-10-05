package com.untistore.clans.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanPlayer;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class StorageManager {
    private final Plugin plugin;
    private final Gson gson;
    private final File clansFolder;
    private final File playerFolder;

    public StorageManager(Plugin plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        this.clansFolder = new File(plugin.getDataFolder(), "data/clans");
        this.playerFolder = new File(plugin.getDataFolder(), "data/players");

        createFolders();
    }

    public void createFolders() {
        if (!clansFolder.exists()) {
            clansFolder.mkdirs();
        }
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }
    }

    public void saveClan(Clan clan) {
        File file = new File(clansFolder, clan.getClanId() + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(clan, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Clan loadClan(int clanId) {
        File file = new File(clansFolder, clanId + ".json");
        if (!doesClanExist(clanId)) return null;

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Clan.class);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doesClanExist(int clanId) {
        File file = new File(clansFolder, clanId + ".json");
        return file.exists();
    }

    public void saveClanPlayer(ClanPlayer player) {
        File file = new File(playerFolder, player.getPlayerUUID().toString() + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(player, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClanPlayer loadClanPlayer(UUID playerUUID) {
        File file = new File(playerFolder, playerUUID.toString() + ".json");
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, ClanPlayer.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getClansFolder() {
        return clansFolder;
    }
    public File getPlayerFolder() {
        return playerFolder;
    }
}
