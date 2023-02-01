package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            Bukkit.broadcast(Component.text(ChatColor.GREEN + player.getName() + " has joined for the first time."));

            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
            player.sendMessage(Component.text(ChatColor.GREEN + "Welcome to " + ChatColor.WHITE + "Oldoe!"));
            player.sendMessage(Component.text(ChatColor.GREEN + "Website: " + ChatColor.WHITE + " www.oldoe.com"));
            player.sendMessage(Component.text(ChatColor.GREEN + "Day: " + ChatColor.WHITE + player.getWorld().getFullTime() / 24000));
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));

            // Add the player to the players table if not already in it
            Oldoe.GetDatabase().executeSQL(String.format("INSERT INTO `oldoe_users` (uuid, name) VALUES ('%s', '%s')", uuid, player.getName()));
            Oldoe.GetDatabase().close();
        } else {
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
            player.sendMessage(Component.text(ChatColor.GREEN + "Welcome back, " + player.getName() + "!"));
            player.sendMessage(Component.text(ChatColor.GREEN + "Day: " + player.getWorld().getFullTime() / 24000));
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
        }
    }
}
