package de.nudelsuppe.hidenseek;

import de.nudelsuppe.hidenseek.commands.Main_Command;
import de.nudelsuppe.hidenseek.listeners.Death_Listener;
import de.nudelsuppe.hidenseek.listeners.Join_Leafe_Listener;
import de.nudelsuppe.hidenseek.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        System.out.println("Plugin started");
        instance = this;

        //Vars.gameGoing = false;
        //Vars.config = getConfig();
        //Vars.config.options().copyDefaults(true);
        //saveConfig();
        //Config.loadConfig();
        //Vars.configFile = new File(getDataFolder(), "config.yml");
        Vars.world = Bukkit.getWorld(Vars.worldName);
        Bukkit.getServer().getPluginManager().registerEvents(new Death_Listener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Join_Leafe_Listener(), this);
        instance.getCommand("hindeandseek").setExecutor(new Main_Command());

    }

    @Override
    public void onDisable() {

    }


    public static void registerListeners() {

    }

    public static void registerCommands() {

    }
}
