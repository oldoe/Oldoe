package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PvpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            OldoePlayer oPlayer = PlayerService.GetPlayer(((Player) sender).getUniqueId());

            if (!oPlayer.isPvpEnabled()) {
                oPlayer.setPVP(true);
                sender.sendMessage(ChatColor.GREEN + "PVP Enabled! (Type /pvp to disable)");
            } else {
                oPlayer.setPVP(false);
                sender.sendMessage(ChatColor.RED + "PVP Disabled! (Type /pvp to enable)");
            }
            return true;

        } else {
            sender.sendMessage("This command can only be sent by a player.");
            return false;
        }
    }
}
