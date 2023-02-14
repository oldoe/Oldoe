package com.oldoe.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocationListener implements Listener {

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            event.setSpawnLocation(event.getSpawnLocation().getWorld().getSpawnLocation());
        }
    }
}
