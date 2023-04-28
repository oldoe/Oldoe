package com.oldoe.plugin.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 0) {
            switch (args[0]) {
                case ("plots"):
                    sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + "HELP - Plots" + ChatColor.WHITE + "-------"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "Note:" + ChatColor.WHITE + " Plots are 128x128 blocks and only in the OverWorld"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot" + ChatColor.WHITE + " | See status of plot you're currently standing in"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot buy" + ChatColor.WHITE + " | Buy plot for $25k (see current balance with /cash)"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot sell" + ChatColor.WHITE + " | Sell plot back to the server."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot add {username}" + ChatColor.WHITE + " | Add a user to this plot."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot remove {username}" + ChatColor.WHITE + " | Remove a user from this plot."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/border" + ChatColor.WHITE + " | Toggle on/off plot border visibility."));
                    break;
                case ("home"):
                    sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + "HELP - Home" + ChatColor.WHITE + "-------"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "Note:" + ChatColor.WHITE + " /sethome and /home only work in the OverWorld."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/sethome" + ChatColor.WHITE + " | Set your current location for your /home"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/home" + ChatColor.WHITE + " | teleport back to your saved home location."));
                    break;
                case("cash"):
                    sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + "HELP - Cash" + ChatColor.WHITE + "-------"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "Note:" + ChatColor.WHITE + " $1 is earned for each block broken or placed."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/cash" + ChatColor.WHITE + " | Check your current cash balance."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/cash sender {user} {amount}" + ChatColor.WHITE + " | Send cash amount to specified user."));
                    break;
                default:
                    SendDefault(sender);
                    break;
            }
        } else {
            SendDefault(sender);
        }
        return true;
    }

    private void SendDefault(CommandSender sender) {
        sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + "HELP" + ChatColor.WHITE + "-------"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/spawn" + ChatColor.WHITE + " | Teleport to spawn (This only works in the overworld)"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/pvp" + ChatColor.WHITE + " | Toggle PVP on/off"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/help plots" + ChatColor.WHITE + " | View Plot commands"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/help cash" + ChatColor.WHITE + " | View Cash commands"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/help home" + ChatColor.WHITE + " | View Home commands"));
    }
}
