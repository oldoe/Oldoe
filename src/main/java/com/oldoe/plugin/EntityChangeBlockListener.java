package com.oldoe.plugin;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityChangeBlockListener implements Listener {

    @EventHandler
    public void onEntityChangeBlock (EntityChangeBlockEvent event) {

        if(event.getEntity() == null) {
            return;
        }

        // Stop Enderman from picking up blocks
        if (event.getEntity().getType() == EntityType.ENDERMAN) {
            event.setCancelled(true);
        }
    }
}
