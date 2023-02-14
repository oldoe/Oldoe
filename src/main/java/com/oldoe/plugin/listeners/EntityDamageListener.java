package com.oldoe.plugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (event.getCause().equals(EntityDamageEvent.DamageCause.STARVATION)) {
            Player player = (Player) entity;
            if (player.getHealth() < 10.5) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onEntityD(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity entityDamager = event.getDamager();
        if (entity instanceof Enemy) {
            // For now all hostile mobs can be killed anywhere.
            return;
        }
        else {
            if(entityDamager instanceof Player && entity.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                Player damager = (Player) entityDamager;
                String uuid = damager.getUniqueId().toString();
                Location loc = event.getEntity().getLocation();

                if (!HasPlotPermissions(uuid, loc)) {
                    damager.sendMessage(ChatColor.RED + "This is a private plot, you do not have permission here.");
                    event.setCancelled(true);
                }
            }
        }
    }

}
