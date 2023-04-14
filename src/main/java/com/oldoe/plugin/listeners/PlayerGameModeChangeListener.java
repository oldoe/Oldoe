package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.logging.Level;

public class PlayerGameModeChangeListener implements Listener {

    @EventHandler
    public void onPlayerGameModeChangeListener(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        Oldoe.getInstance().getServer().getLogger().log(Level.INFO, Oldoe.getInstance().getName() + "> " + player.getName() + " Changed GameMode -> " + event.getNewGameMode().name() + " in " + loc.getWorld().getName() + " at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
    }
}
