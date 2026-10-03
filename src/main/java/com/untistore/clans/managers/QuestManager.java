package com.untistore.clans.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.untistore.clans.models.Quest;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestManager {
    private final Plugin plugin;
    private List<Quest> allAvailableQuests;
    private long lastQuestUpdate;

    public QuestManager(Plugin plugin) {
        this.plugin = plugin;
        loadQuests();
    }

    private void loadQuests() {
        File file = new File(plugin.getDataFolder(), "quests.json");
        Gson gson = new Gson();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileReader reader = new FileReader(file)) {
            Type questListType = new TypeToken<List<Quest>>() {}.getType();
            allAvailableQuests = gson.fromJson(reader, questListType);

            if (allAvailableQuests == null) {
                allAvailableQuests = new ArrayList<>();
            }

            plugin.getLogger().info("Loaded " + allAvailableQuests.size() + " quests.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Quest> getRandomQuests(int amount) {
        if (allAvailableQuests == null) return new ArrayList<>();

        List<Quest> randomQuests = new ArrayList<>(allAvailableQuests);
        Collections.shuffle(randomQuests);

        int limit = Math.min(amount, randomQuests.size());
        return randomQuests.subList(0, limit);
    }
}
