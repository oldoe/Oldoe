package com.oldoe.plugin.listeners;

import org.bukkit.World;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawningListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        Entity entity = event.getEntity();

        // Hostile mobs that are wanting to spawn in the overworld
        if (entity instanceof Enemy && entity.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            int spawnX = entity.getWorld().getSpawnLocation().getBlockX();
            int spawnZ = entity.getWorld().getSpawnLocation().getBlockZ();
            // Prevent hostile mobs from spawning within 128 blocks of spawn.
            if (Math.abs((spawnX - entity.getLocation().getBlockX())) < 128 && Math.abs((spawnZ - entity.getLocation().getBlockZ())) < 128) {
                event.setCancelled(true);
            }
        }
    }
}
