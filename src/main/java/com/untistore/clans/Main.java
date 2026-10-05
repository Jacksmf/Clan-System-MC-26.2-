package com.untistore.clans;

import com.untistore.clans.commands.ClanCommand;
import com.untistore.clans.inventorys.clangui.ClanMenu;
import com.untistore.clans.inventorys.clangui.ClanQuestMenu;
import com.untistore.clans.listener.InventoryListener;
import com.untistore.clans.listener.QuestListener;
import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.managers.ClanWarManager;
import com.untistore.clans.managers.QuestManager;
import com.untistore.clans.managers.StorageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {
    private ClanManager clanManager;
    private QuestManager questManager;
    private StorageManager storageManager;

    private ClanMenu clanMenu;
    private ClanQuestMenu clanQuestMenu;

    @Override
    public void onEnable() {
        saveResource("quests.json", false);

        this.storageManager = new StorageManager(this);
        this.questManager = new QuestManager(this);
        this.clanManager = new ClanManager(this, storageManager, questManager);

        clanManager.ensureClansHaveQuests();
        clanManager.startQuestRenewalTask();
        clanManager.startSaveTask();

        getLogger().info("ClanManager is enabled");

        getServer().getPluginManager().registerEvents(new QuestListener(clanManager), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        new ClanWarManager(new ArrayList<>(), clanManager).runTaskTimer(this, 20L, 20L);

        if (getCommand("clan") != null) getCommand("clan").setExecutor(new ClanCommand(clanManager, questManager));
    }

    @Override
    public void onDisable() {
        clanManager.saveAllData();
        getLogger().info("[Clans] Disabled!");
    }
}
