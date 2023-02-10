package com.oldoe.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MsgCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 0) {
            Player otherPlayer = Bukkit.getPlayer(args[0]);

            if (otherPlayer != null) {
                StringBuffer sb = new StringBuffer();
                for(int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                }

                String msg = sb.toString();
                otherPlayer.sendMessage(ChatColor.GOLD + "<Message> " + ChatColor.WHITE + sender.getName() + ChatColor.GOLD + " > " + ChatColor.WHITE + otherPlayer.getName() + ": " + msg);
                sender.sendMessage(ChatColor.GOLD + "<Message> " + ChatColor.WHITE + sender.getName() + ChatColor.GOLD + " > " + ChatColor.WHITE + otherPlayer.getName() + ": " + msg);
            } else {
                // Todo, implement reply feature without having to type recipient each time.
            }
        }

        return true;
    }
}
