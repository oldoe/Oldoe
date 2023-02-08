package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        // Get the unique id of the player
        String uuid = player.getUniqueId().toString();

        if (Oldoe.GetDatabase().getPlayerID(uuid) == -1) {
            Bukkit.broadcast(Component.text(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " has joined for the first time."));

            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Welcome to " + ChatColor.WHITE + "Oldoe!"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Website: " + ChatColor.WHITE + " www.oldoe.com"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Day: " + ChatColor.WHITE + player.getWorld().getFullTime() / 24000));
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));

            // Add the player to the players table if not already in it
            Oldoe.GetDatabase().executeSQL(String.format("INSERT INTO `oldoe_users` (uuid, name) VALUES ('%s', '%s')", uuid, player.getName()));
            Oldoe.GetDatabase().close();

            // Play a deep welcome sound.
            player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 0.9f, 0.5f);
        } else {
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Welcome back, " + ChatColor.WHITE + player.getName() + "!"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Day: " + ChatColor.WHITE + player.getWorld().getFullTime() / 24000));
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
        }
    }
}
