package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BorderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
            oPlayer.toggleBorder();

            if (oPlayer.borderEnabled()) {
                player.sendMessage(ChatColor.GREEN + "Plot borders enabled! (Type /border to disable)");
            } else {
                player.sendMessage(ChatColor.RED + "Plot borders disabled! (Type /border to enable)");
            }

            return true;
        }
        return false;
    }
}
