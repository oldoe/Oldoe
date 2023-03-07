package com.oldoe.plugin.listeners;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplosionListener implements Listener {

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        // Clear block list during explosion from creepers in overworld
        if (entity != null && entity.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            if (entity instanceof Monster) {
                event.blockList().clear();
            }
        }
    }
}
