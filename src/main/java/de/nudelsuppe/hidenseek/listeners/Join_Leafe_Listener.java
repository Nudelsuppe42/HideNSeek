package de.nudelsuppe.hidenseek.listeners;

import de.nudelsuppe.hidenseek.Game;
import de.nudelsuppe.hidenseek.Main;
import de.nudelsuppe.hidenseek.Vars;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Join_Leafe_Listener implements Listener {
    private static Main plugin;


    @EventHandler
    public void OnPlayerQuitEvent(PlayerQuitEvent e) {
        if (Vars.gameGoing && Vars.inGame.contains(e.getPlayer())) {
            if (Vars.seeker.getUniqueId() == e.getPlayer().getUniqueId()) {
                for (Player p : Vars.inGame) {
                    p.getInventory().clear();
                    p.sendMessage(ChatColor.WHITE + "The seeker left the game. The game stopped.");
                    if (Vars.config.getBoolean("hideNameTags")) {
                        p.setCustomNameVisible(true);
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Game.stopGame();
                        for (Player p : Vars.inGame) {
                            p.teleport(Vars.world.getSpawnLocation());
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                    }
                }.runTaskLater(plugin, 20 * 10);
            }
            boolean isHiderAlive = false;
            for (Player p2 : Vars.inGame) {
                if (p2.getGameMode() == GameMode.SURVIVAL && p2.getUniqueId() != Vars.seeker.getUniqueId()) {
                    isHiderAlive = true;
                }
            }
            if (!(isHiderAlive)) {
                for (Player p2 : Vars.inGame) {
                    p2.getInventory().clear();
                    if (Vars.config.getBoolean("hideNameTags")) {
                        p2.setCustomNameVisible(true);
                    }
                    p2.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "The seeker found everyone. Well done " + Vars.seeker.getDisplayName());
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Game.stopGame();
                        for (Player p : Vars.inGame) {
                            p.teleport(Vars.world.getSpawnLocation());
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                    }
                }.runTaskLater(plugin, 20 * 10);
            }
        }
    }
}
