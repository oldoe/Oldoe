package com.oldoe.plugin;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawningListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        Entity entity = event.getEntity();

        // Hostile mobs that are wanting to spawn in the overworld
        if (entity instanceof Monster && entity.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            // Prevent hostile mobs from spawning within 50 blocks of spawn.
            if (Math.abs(entity.getLocation().getX()) < 50 && Math.abs(entity.getLocation().getZ()) < 50 ) {
                event.setCancelled(true);
            }
        }
    }
}
