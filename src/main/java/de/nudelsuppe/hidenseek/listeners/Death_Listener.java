
package de.nudelsuppe.hidenseek.listeners;

import de.nudelsuppe.hidenseek.Game;
import de.nudelsuppe.hidenseek.Main;
import de.nudelsuppe.hidenseek.Vars;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Death_Listener implements Listener {
    private static Main plugin;


    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        boolean isInGame = false;
        boolean isHiderAlive = false;
        if (Vars.inGame != null && Vars.gameGoing) {
            for (Player p2 : Vars.inGame) {
                if (p2.getUniqueId() == p.getUniqueId()) {
                    isInGame = true;
                }
            }
            if (isInGame) {
                if (p.getUniqueId() != Vars.seeker.getUniqueId()) {
                    if (Vars.playedSound.contains(p.getUniqueId().toString())) {
                        p.sendMessage(ChatColor.RED + "You didn't get a reward because you were found by the seeker.");
                        Vars.playedSound.remove(p.getUniqueId().toString());
                    }
                    p.setGameMode(GameMode.SPECTATOR);
                    p.teleport(Vars.seeker.getLocation());
                    for (Player p2 : Vars.inGame) {
                        p2.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + p.getDisplayName() + ChatColor.RESET + "" + ChatColor.WHITE + " was found by seeker.");
                    }
                } else {
                    p.setGameMode(GameMode.SPECTATOR);
                    for (Player p2 : Vars.inGame) {
                        p2.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "The seeker got killed.");
                    }
                }
                for (Player p2 : Vars.inGame) {
                    if (p2.getGameMode() == GameMode.SURVIVAL && p2.getUniqueId() != Vars.seeker.getUniqueId()) {
                        isHiderAlive = true;
                    }
                }
                if (!(isHiderAlive)) {
                    for (Player p2 : Vars.inGame) {
                        p2.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "The seeker found everyone. Well done " + Vars.seeker.getDisplayName());
                        p2.getInventory().clear();
                        if (Vars.config.getBoolean("hideNameTags")) {
                            p2.setCustomNameVisible(true);
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
            }
        }
    }
}