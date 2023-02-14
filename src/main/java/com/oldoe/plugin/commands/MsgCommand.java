package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MsgCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 0) {
            Player otherPlayer = Bukkit.getPlayer(args[0]);

            if (otherPlayer != null) {

                if (otherPlayer == sender) {
                    sender.sendMessage(ChatColor.RED + "You can't message yourself.");
                    return true;
                }

                StringBuffer sb = new StringBuffer();
                for(int i = 1; i < args.length; i++) {
                    sb.append(args[i] + " ");
                }

                String msg = sb.toString();
                otherPlayer.sendMessage(ChatColor.GOLD + "<Message> " + ChatColor.WHITE + sender.getName() + ChatColor.GOLD + " to " + ChatColor.WHITE + otherPlayer.getName() + ": " + msg);
                sender.sendMessage(ChatColor.GOLD + "<Message> " + ChatColor.WHITE + sender.getName() + ChatColor.GOLD + " to " + ChatColor.WHITE + otherPlayer.getName() + ": " + msg);

                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    // Save each other for /reply and msg functions
                    Oldoe.SetLastMessage(otherPlayer.getUniqueId(), p.getUniqueId());
                    Oldoe.SetLastMessage(p.getUniqueId(), otherPlayer.getUniqueId());
                }

            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    UUID r = Oldoe.GetLastPlayerMessaged(p.getUniqueId());
                    if (r != null) {
                        Player recPlayer = Bukkit.getPlayer(r);
                        if (recPlayer != null) {

                            StringBuffer sb = new StringBuffer();
                            for(int i = 0; i < args.length; i++) {
                                sb.append(args[i] + " ");
                            }

                            String msg = sb.toString();
                            recPlayer.sendMessage(ChatColor.GOLD + "<Message> " + ChatColor.WHITE + sender.getName() + ChatColor.GOLD + " to " + ChatColor.WHITE + recPlayer.getName() + ": " + msg);
                            sender.sendMessage(ChatColor.GOLD + "<Message> " + ChatColor.WHITE + sender.getName() + ChatColor.GOLD + " to " + ChatColor.WHITE + recPlayer.getName() + ": " + msg);

                            // Save each other for /reply and msg functions
                            Oldoe.SetLastMessage(recPlayer.getUniqueId(), p.getUniqueId());
                            Oldoe.SetLastMessage(p.getUniqueId(), recPlayer.getUniqueId());
                        }
                    }
                }
            }
        }

        return true;
    }
}
