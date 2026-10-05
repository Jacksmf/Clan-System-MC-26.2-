package com.untistore.clans.commands;

import com.untistore.clans.inventorys.clangui.ClanMenu;
import com.untistore.clans.inventorys.clangui.ClanQuestMenu;
import com.untistore.clans.managers.ClanManager;
import com.untistore.clans.managers.QuestManager;
import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.models.Quest;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClanCommand implements CommandExecutor {
    private ClanManager clanManager;
    private QuestManager questManager;


    public ClanCommand(ClanManager clanManager, QuestManager questManager) {
        this.clanManager = clanManager;
        this.questManager = questManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }


        if (args.length == 0) {
            Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
            if (clan == null) {
                sendHelp(player);
                return true;
            }

            new ClanMenu(player, clanManager, questManager).showPlayer(player);
            return true;
        }

        String subCommand = args[0];
        switch (subCommand.toLowerCase()) {
            case "create" -> handleCreate(player, args);
            case "home" -> handleHome(player);
            case "sethome" -> handleSetHome(player);
            case "top" -> handleTop(player);
            case "quests" -> handleQuests(player);
            case "war" -> handleWar(player, args);
            case "help" -> sendHelp(player);
            default -> {
                player.sendMessage(Component.text("Unknown sub-command"));
            }
        }

        return true;
    }

    private void handleCreate(Player player, @NotNull String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: /clan create <name>"));
            return;
        }

        if (clanManager.getClanByPlayer(player.getUniqueId()) != null) {
            player.sendMessage(Component.text("You're already in a clan"));
            return;
        }

        String clanName = args[1];
        boolean created = clanManager.createClan(player.getUniqueId(), clanName);
        if (created) {
            player.sendMessage(Component.text("Clan has been created"));
        } else {
            player.sendMessage(Component.text("Failed to create clan"));
        }
    }

    private void handleHome(Player player) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(Component.text("You're not in a clan"));
            return;
        }

        if (clan.getHome() == null) {
            player.sendMessage(Component.text("Clan home has not been set"));
            return;
        }

        player.teleport(clan.getHome());
        player.sendMessage(Component.text("Clan has been set to " + clan.getName()));
    }

    private void handleSetHome(Player player) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(Component.text("You're not in a clan"));
            return;
        }

        if (!clan.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("You're not the owner of the clan"));
            return;
        }

        clan.setHome(player.getLocation());
        player.sendMessage(Component.text("Clan home has been set to your current location"));
    }

    private void handleTop(Player player) {
        player.sendMessage(Component.text("--- Clan Leaderboard ---"));
        int rank = 1;
        for (Clan clan : clanManager.getTopClans(5)) {
            player.sendMessage(Component.text(rank + ". " + clan.getName() + " - " + " Level " + clan.getLevel()));
            rank++;
        }
    }

    private void handleQuests(Player player) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(Component.text("You're not in a clan"));
            return;
        }

        List<Quest> quests = clan.getActiveQuests();

        ClanPlayer clanPlayer = clanManager.getClanPlayer(player.getUniqueId());
        player.sendMessage(Component.text("--- Quests ---"));
        for (Quest quest : quests) {
            int progress = clanPlayer.getQuestProgress(quest.getQuestId());
            int required = quest.getRequiredAmount();

            if (progress < required) {
                player.sendMessage(Component.text("- " + quest.getQuestId() + " (" + progress + "/" + required + ")"));
            } else {
                player.sendMessage(Component.text("- " + quest.getQuestId() + " (Completed)"));
            }
        }
    }

    private void handleWar(Player player, @NotNull String @NotNull [] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: /clan war <clan name>"));
            return;
        }

        player.sendMessage(Component.text("Not implemented yet"));
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6--- Clan Commands ---");
        player.sendMessage("§e/clan create <name> §7- Create a new clan");
        player.sendMessage("§e/clan home §7- Teleport to clan home");
        player.sendMessage("§e/clan sethome §7- Set the clan home location");
        player.sendMessage("§e/clan quests §7- View active clan quests");
        player.sendMessage("§e/clan war <clan> <bet> §7- Challenge a clan");
        player.sendMessage("§e/clan top §7- View the clan leaderboard");
    }
}
