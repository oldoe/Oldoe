package com.oldoe.plugin.listeners;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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

}
