package de.nudelsuppe.hidenseek.utils;

import de.nudelsuppe.hidenseek.Vars;

public class Config {

    public static void loadConfig() {
        Vars.worldName = Vars.config.getString("world");

        Vars.shrinking = Vars.config.getBoolean("shrinking.enable");//
        Vars.shrinkingAfter = Vars.config.getIntegerList("shrinking.after");//
        Vars.shrinkingDuration = Vars.config.getInt("shrinking.duration");//
        Vars.shrinkingFactor = Vars.config.getDouble("shrinking.factor");//

        Vars.announceTimeAt = Vars.config.getIntegerList("announceTimeAt");//

        Vars.hideTime = Vars.config.getInt("hideTime");//

        Vars.hideDefaultDeathMessages = Vars.config.getBoolean("hideDefaultDeathMessages");//

        Vars.minPlayers = Vars.config.getInt("minPlayers");//

        Vars.sound = Vars.config.getBoolean("sound.enable");//
        Vars.soundMinDelay = Vars.config.getInt("sound.minDelay");//
        Vars.soundSurviveTime = Vars.config.getInt("sound.surviveTime");//
        Vars.soundRewardCommands = Vars.config.getStringList("sound.rewardCommands");//
    }

}
