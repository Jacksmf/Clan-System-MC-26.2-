package com.untistore.clans.managers;

import com.untistore.clans.models.Clan;
import com.untistore.clans.models.ClanWar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

public class ClanWarManager extends BukkitRunnable {
    private List<ClanWar> activeWars;
    private ClanManager clanManager;

    public ClanWarManager(List<ClanWar> activeWars, ClanManager clanManager) {
        this.activeWars = activeWars;
        this.clanManager = clanManager;
    }


    @Override
    public void run() {
        Iterator<ClanWar> iterator = activeWars.iterator();

        while (iterator.hasNext()) {
            ClanWar war = iterator.next();

            if (System.currentTimeMillis() >= war.getEndTime()) {
                war.setActive(false);
                iterator.remove();

                String winnerId = (war.getPointsA() > war.getPointsB()) ? war.getClanIdA() : (war.getPointsB() > war.getPointsA() ? war.getClanIdB() : null);

                if (winnerId != null) {
                    Clan winner = clanManager.getClan(winnerId);
                    distributeBet(winner, war.getBetAmount(), war.getBetCurrencyType());
                    Bukkit.broadcast(Component.text("[Clan War] " + winner.getName() + " won the war"));
                } else {
                    Bukkit.broadcast(Component.text("[Clan War] The war ended in a tie!"));
                }
            }
        }
    }

    private void distributeBet(Clan winner, int betAmount, String betCurrencyType) {
        List<Player> onlineMembers = winner.getOnlineMembers();
        if (onlineMembers.isEmpty()) return;

        int splitAmount = betAmount / onlineMembers.size();

        for (Player player : onlineMembers) {
            switch (betCurrencyType.toLowerCase()) {
                case "tokens" -> {
                    player.sendMessage(Component.text("You received " + splitAmount + " tokens from the Clan War!"));
                }
                case "items" -> {
                    player.sendMessage(Component.text("You received " + splitAmount + " items from the Clan War!"));
                }
            }
        }
    }
}
