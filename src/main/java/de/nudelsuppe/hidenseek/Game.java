package de.nudelsuppe.hidenseek;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Game {

    public static void ShrinkBorder() {
        for (Player p : Vars.inGame) {
            p.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "The worldborder will shrink to " + (int) (Vars.shrinkingFactor * 100) + "% in " + Vars.shrinkingDuration + " seconds!");
        }
        Vars.world.getWorldBorder().setSize(Vars.world.getWorldBorder().getSize() * Vars.shrinkingFactor, Vars.shrinkingDuration);
    }

    public static void stopGame() {
        if (!Vars.freezeSeeker.isCancelled()) {
            Vars.freezeSeeker.cancel();
        }
        Vars.gameGoing = false;
        for (BukkitRunnable task : Vars.tasks) {
            task.cancel();
        }
        Vars.world.getWorldBorder().setSize(Vars.startWorldBorderSize);
    }


}
