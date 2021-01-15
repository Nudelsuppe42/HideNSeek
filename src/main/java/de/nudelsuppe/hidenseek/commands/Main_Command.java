package de.nudelsuppe.hidenseek.commands;

import de.nudelsuppe.hidenseek.Game;
import de.nudelsuppe.hidenseek.Main;
import de.nudelsuppe.hidenseek.Vars;
import de.nudelsuppe.hidenseek.utils.Config;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

import static de.nudelsuppe.hidenseek.Vars.configFile;

public class Main_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("hideandseek")) {
            if (!(sender.hasPermission("sybhideandseek.start"))) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("stop")) {
                    for (Player p2 : Vars.inGame) {
                        p2.getInventory().clear();
                        if (Vars.config.getBoolean("hideNameTags")) {
                            p2.setCustomNameVisible(true);
                        }
                    }
                    Game.stopGame();
                    sender.sendMessage("The game stopped.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("shrink")) {
                    Game.ShrinkBorder();
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Vars.config = YamlConfiguration.loadConfiguration(configFile);
                    Config.loadConfig();
                    sender.sendMessage("The settings from the config.yml file have been reloaded.");
                    return true;
                }
            }
            if (Vars.gameGoing) {
                sender.sendMessage(ChatColor.RED + "A game has already started.");
                return true;
            }
            Vars.world = Bukkit.getWorld(Vars.worldName);
            if (Vars.world != null) {
                Vars.inGame = Vars.world.getPlayers();
                Vars.world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, !Vars.hideDefaultDeathMessages);
                if (Vars.inGame.size() >= Vars.minPlayers) {
                    Vars.gameGoing = true;
                    Vars.playedSound = new ArrayList<String>();
                    Vars.startWorldBorderSize = Vars.world.getWorldBorder().getSize();
                    for (Player p : Vars.inGame) {
                        p.teleport(Vars.world.getSpawnLocation());
                        p.setGameMode(GameMode.SURVIVAL);
                        p.sendMessage("You're now in a Hide & Seek game.");
                        if (Vars.config.getBoolean("hideNameTags")) {
                            p.setCustomNameVisible(false);
                        }
                    }
                    Vars.seeker = Vars.inGame.get(new Random().nextInt(Vars.inGame.size()));
                    Vars.seeker.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "You're the seeker.");
                    for (Player p : Vars.inGame) {
                        if (p.getUniqueId() != Vars.seeker.getUniqueId()) {
                            p.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + Vars.seeker.getDisplayName() + " is the seeker, you have 1 minute to get away.");
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Vars.hideTime * 20, 0, false, false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));
                            p.getInventory().clear();
                        } else {
                            p.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "Wait " + Vars.hideTime + " seconds before you can search for the hiders.");
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * Vars.hideTime, 10, false, false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * Vars.hideTime, 255, false, false));
                            p.getInventory().clear();
                            Vars.tasks.add(new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Vars.seeker.teleport(Vars.world.getSpawnLocation());
                                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                                    sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 16);
                                    sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                                    Vars.seeker.getInventory().setItem(0, sword);
                                }
                            });
                            Vars.tasks.get(Vars.tasks.size() - 1).runTaskLater(Main.instance, 20 * Vars.hideTime);
                            Vars.freezeSeeker = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Location spawn = Vars.world.getSpawnLocation();
                                    spawn.setPitch(90);
                                    Vars.seeker.teleport(spawn);
                                }
                            };
                            Vars.freezeSeeker.runTaskTimer(Main.instance, 2, 2);
                        }
                    }
                    for (Integer time : Vars.announceTimeAt) {
                        Vars.tasks.add(new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Player p : Vars.inGame) {
                                    p.sendMessage(time + " seconds left");
                                }
                            }
                        });
                        Vars.tasks.get(Vars.tasks.size() - 1).runTaskLater(Main.instance, 20 * (Vars.hideTime - time));
                    }
                    Vars.tasks.add(new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Player p : Vars.inGame) {
                                p.sendMessage(ChatColor.BOLD + "The seeker is seeking.");
                                Vars.freezeSeeker.cancel();
                            }
                        }
                    });
                    Vars.tasks.get(Vars.tasks.size() - 1).runTaskLater(Main.instance, 20 * Vars.hideTime);
                    if (Vars.shrinking) {
                        Vars.tasks = new ArrayList<BukkitRunnable>();
                        for (Integer time : Vars.shrinkingAfter) {
                            Vars.tasks.add(new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (Vars.gameGoing) {
                                        Game.ShrinkBorder();
                                    }
                                }
                            });
                            Vars.tasks.get(Vars.tasks.size() - 1).runTaskLater(Main.instance, 20 * time + 20 * Vars.hideTime);
                        }
                    }
                } else {
                    sender.sendMessage("There aren't enough players in the '" + Vars.worldName + "' world.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "There's no world called '" + Vars.worldName + "', please create it or change the world in the config.yml file.");
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("sound")) {
            if (!Vars.sound) {
                sender.sendMessage(ChatColor.RED + "Sounds are not enabled.");
                return true;
            }
            if (!sender.hasPermission("sybhideandseek.sound")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
                return true;
            }
            if (!Vars.gameGoing) {
                sender.sendMessage(ChatColor.RED + "You're not in a Hide and Seek game.");
                return true;
            }
            Player p = (Player) sender;
            if (!Vars.inGame.contains(p)) {
                sender.sendMessage(ChatColor.RED + "You're not in a Hide and Seek game.");
                return true;
            }
            if (p.getGameMode() != GameMode.SURVIVAL) {
                sender.sendMessage(ChatColor.RED + "You're not alive anymore.");
                return true;
            }
            if (Vars.seeker.getUniqueId() == p.getUniqueId()) {
                sender.sendMessage(ChatColor.RED + "The seeker can't get rewards for playing sounds.");
                return true;
            }
            if (Vars.playedSound.contains(p.getUniqueId().toString())) {
                sender.sendMessage(ChatColor.RED + "You've already played a sound, please wait before playing another.");
                return true;
            }
            Vars.playedSound.add(p.getUniqueId().toString());
            Vars.world.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 2.0F, 0.1F);
            sender.sendMessage("Survive for " + Vars.soundSurviveTime + " seconds to get a reward.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Vars.playedSound.contains(p.getUniqueId().toString())) {
                        sender.sendMessage("You survived.");
                        Vars.playedSound.remove(p.getUniqueId().toString());
                        for (String command : Vars.soundRewardCommands) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replaceAll("\\{player}", p.getName()));
                        }
                    }
                }
            }.runTaskLater(Main.instance, 20 * Vars.soundMinDelay);
            return true;
        }
        return false;
    }

}
