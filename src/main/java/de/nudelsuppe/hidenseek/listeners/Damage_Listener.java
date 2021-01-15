package de.nudelsuppe.hidenseek.listeners;

import de.nudelsuppe.hidenseek.Vars;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Damage_Listener implements Listener {

    @EventHandler
    public static void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                event.setCancelled(true);
                if(player == Vars.seeker) {
                    player.sendMessage(ChatColor.GRAY + "A Hider hit you!");
                }
            }


        }
    }

}
