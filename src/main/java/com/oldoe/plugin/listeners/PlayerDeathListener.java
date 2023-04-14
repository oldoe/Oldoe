package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.logging.Level;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        player.sendMessage(ChatColor.RED + "Death location: " + ChatColor.WHITE + loc.getWorld().getName() + " at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
        Oldoe.getInstance().getServer().getLogger().log(Level.INFO, Oldoe.getInstance().getName() + " " + player.getName() + " died -> " + loc.getWorld().getName() + " at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
    }
}
