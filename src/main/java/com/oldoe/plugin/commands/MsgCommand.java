package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class MsgCommand implements CommandExecutor {

    ToggleCmd toggleCmd;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 0) {
            Player p = (Player) sender;
            Player otherPlayer = Bukkit.getPlayer(args[0]);
            OldoePlayer oSender = PlayerService.GetPlayer(p.getUniqueId());
            OldoePlayer oRecipient = PlayerService.GetPlayer(otherPlayer.getUniqueId());
            {

                if (otherPlayer == sender) {
                    sender.sendMessage(ChatColor.RED + "You can't message yourself!");
                    return true;
                }

                StringBuffer sb = new StringBuffer();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i] + " ");
                }

                String msg = sb.toString();
                otherPlayer.sendMessage(ChatColor.GREEN + "<msg> " + ChatColor.WHITE + sender.getName() + " > " + ChatColor.WHITE + otherPlayer.getName() + ": " + msg);
                sender.sendMessage(ChatColor.GREEN + "<msg> " + ChatColor.WHITE + sender.getName() + " > " + ChatColor.WHITE + otherPlayer.getName() + ": " + msg);
                if (!(oSender.isStaff() || oRecipient.isStaff())) {
                    for (UUID i : toggleCmd.pmSpy) {
                        Objects.requireNonNull(Bukkit.getPlayer(i)).sendMessage(ChatColor.GOLD + "<pmspy>" + ChatColor.GRAY + sender.getName() + " > " + otherPlayer.getName() + ": " + msg);
                    }
                }

                // Save each other for /reply and msg functions
                oSender.setLastMessaged(otherPlayer.getName());
                oRecipient.setLastMessaged(p.getName());

            }
        }

        return true;
    }
}
