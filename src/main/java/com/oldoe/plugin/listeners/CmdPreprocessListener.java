package com.oldoe.plugin.listeners;

import com.oldoe.plugin.commands.ToggleCmd;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;
import java.util.UUID;

public class CmdPreprocessListener implements Listener {
    ToggleCmd toggleCmd;

    @EventHandler
    public void onPlayerCmdPreprocess(PlayerCommandPreprocessEvent e) {
        for (UUID i : toggleCmd.cmdSpy) {
            OldoePlayer oSender = PlayerService.GetPlayer(e.getPlayer().getUniqueId());
            if (!oSender.isStaff()) {
                Objects.requireNonNull(Bukkit.getPlayer(i)).sendMessage(ChatColor.GOLD + "<cmdspy>" + ChatColor.GRAY + e.getPlayer().getName() + ": " + e.getMessage());
            }
        }
    }
}
