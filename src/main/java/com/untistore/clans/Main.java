package com.untistore.clans;

import com.untistore.clans.commands.ClanCommand;
import com.untistore.clans.inventorys.clangui.ClanMenu;
import com.untistore.clans.listener.InventoryListener;
import com.untistore.clans.listener.QuestListener;
import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.managers.ClanWarManager;
import com.untistore.clans.managers.QuestManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {
    private ClanManager clanManager;
    private QuestManager questManager;

    private ClanMenu clanMenu;

    @Override
    public void onEnable() {
        saveResource("quests.json", false);

        this.questManager = new QuestManager(this);
        this.clanManager = new ClanManager(this, questManager);

        clanManager.ensureClansHaveQuests();

        clanMenu = new ClanMenu(clanManager, questManager);

        getLogger().info("ClanManager is enabled");

        getServer().getPluginManager().registerEvents(new QuestListener(clanManager), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        new ClanWarManager(new ArrayList<>(), clanManager).runTaskTimer(this, 20L, 20L);

        if (getCommand("clan") != null) getCommand("clan").setExecutor(new ClanCommand(clanManager, clanMenu));
    }

    @Override
    public void onDisable() {
        getLogger().info("[Clans] Disabled!");
    }
}
