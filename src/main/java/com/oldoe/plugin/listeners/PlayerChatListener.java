package com.oldoe.plugin.listeners;

import com.oldoe.plugin.commands.StaffChatCommand;
import com.oldoe.plugin.commands.ToggleCmd;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {
    ToggleCmd toggleCmd;
    StaffChatCommand staffChatCommand;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncChatEvent event) {
        TextComponent msg = (TextComponent) event.message();

        OldoePlayer oPlayer = PlayerService.GetPlayer(event.getPlayer().getUniqueId());

        String badge = new String();
        if (oPlayer.isStaff()) {
            if (toggleCmd.staffChat.contains(event.getPlayer().getUniqueId())) {
                staffChatCommand.staffChatHandler(event.getPlayer(), event.message().toString());
                event.setCancelled(true);
                return;
            }
            badge = "[Staff] ";
        }

        Component comp = LegacyComponentSerializer.builder().character('&').extractUrls().build().deserialize(Component.text(ChatColor.GREEN + "\u2022 " + ChatColor.RED + badge + ChatColor.WHITE + event.getPlayer().getName() + ": ").content() + msg.content());

        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(comp);
        }

        event.setCancelled(true);
    }
}
