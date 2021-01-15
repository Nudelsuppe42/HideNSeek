package de.nudelsuppe.hidenseek;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;

public class Vars {

    public static List<Player> inGame;
    public static boolean gameGoing;
    public static World world;
    public static double startWorldBorderSize;
    public static Player seeker;
    public static List<BukkitRunnable> tasks;
    public static BukkitRunnable freezeSeeker;
    public static String worldName = "Game";
    public static List<Integer> announceTimeAt;
    public static Integer hideTime = 90;
    public static boolean shrinking = true;
    public static List<Integer> shrinkingAfter;
    public static Integer shrinkingDuration = 20;
    public static double shrinkingFactor = 0.75;
    public static boolean hideDefaultDeathMessages = true;
    public static Integer minPlayers = 2;
    public static List<String> playedSound;
    public static boolean sound = false;
    public static Integer soundMinDelay = 0;
    public static Integer soundSurviveTime = 30;
    public static List<String> soundRewardCommands;
    public static FileConfiguration config;
    public static File configFile;


    public static void init() {
        shrinkingAfter.add(100);
        shrinkingAfter.add(140);
        shrinkingAfter.add(180);
        shrinkingAfter.add(220);
        shrinkingAfter.add(260);
        shrinkingAfter.add(300);

        announceTimeAt.add(60);
        announceTimeAt.add(30);
        announceTimeAt.add(20);
        announceTimeAt.add(10);
        announceTimeAt.add(5);
        announceTimeAt.add(4);
        announceTimeAt.add(3);
        announceTimeAt.add(2);
        announceTimeAt.add(1);
    }
}
