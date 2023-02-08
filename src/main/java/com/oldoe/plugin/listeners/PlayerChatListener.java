package com.oldoe.plugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.nio.charset.StandardCharsets;

public class PlayerChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncChatEvent event) {
        TextComponent msg = (TextComponent) event.message();
        Bukkit.broadcast(Component.text(ChatColor.GOLD + "> " + ChatColor.WHITE + event.getPlayer().getName() + ": " + msg.content()));

        event.setCancelled(true);
    }
}
