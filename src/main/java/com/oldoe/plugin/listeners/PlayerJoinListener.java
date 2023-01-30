package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text("Welcome to Oldoe, " + event.getPlayer().getName() + "!"));

        Player player = event.getPlayer();
        // Get the unique id of the player
        String uuid = player.getUniqueId().toString();

        // Add the player to the players table if not already in it
        Oldoe.GetDatabase().executeSQL(String.format("INSERT IGNORE INTO oldoe_users (uuid) VALUES ('%s')", uuid));
        Oldoe.GetDatabase().close();
    }
}
