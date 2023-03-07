package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.DataService;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        // Get the unique id of the player
        String uuid = player.getUniqueId().toString();

        if ( DataService.getDatabase().getPlayerID(uuid) == -1) {
            Bukkit.broadcast(Component.text(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " has joined for the first time."));

            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Welcome to " + ChatColor.WHITE + "Oldoe!"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Website: " + ChatColor.WHITE + " www.oldoe.com"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Day: " + ChatColor.WHITE + player.getWorld().getFullTime() / 24000));
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));

            // Add the player to the players table if not already in it
            DataService.getDatabase().executeSQL(String.format("INSERT INTO `oldoe_users` (uuid, name) VALUES ('%s', '%s')", uuid, player.getName()));
            DataService.getDatabase().close();

            // Play a deep welcome sound.
            player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 0.9f, 0.5f);
        } else {
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Welcome back to Oldoe!"));
            player.sendMessage(Component.text(ChatColor.GOLD + "Day: " + ChatColor.WHITE + player.getWorld().getFullTime() / 24000));
            player.sendMessage(Component.text(ChatColor.WHITE + "------------------------------"));
        }

        //int id, String uuid, String displayName
        OldoePlayer oldoePlayer = new OldoePlayer(uuid, player.getName());

        List<String> staffList = Oldoe.getInstance().getConfig().getStringList("staff");

        if (staffList != null && staffList.contains(uuid)) {
            oldoePlayer.setStaff();
        }

        PlayerService.AddPlayer(oldoePlayer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
        PlayerService.RemovePlayer(oPlayer);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
        PlayerService.RemovePlayer(oPlayer);
    }
}
