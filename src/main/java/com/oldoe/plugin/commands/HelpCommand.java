package com.oldoe.plugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class HelpCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 0) {
            switch (args[0]) {
                case ("plots"):
                    sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + " Help - Plots " + ChatColor.WHITE + "-------"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "Note:" + ChatColor.WHITE + " Plots are 128x128 blocks and only in the OverWorld"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot" + ChatColor.WHITE + " | See status of plot you're currently standing in").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/plot")));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot buy" + ChatColor.WHITE + " | Buy plot for $25k (see current balance with /cash)"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot sell" + ChatColor.WHITE + " | Sell plot back to the server."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot add {username}" + ChatColor.WHITE + " | Add a user to this plot."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/plot remove {username}" + ChatColor.WHITE + " | Remove a user from this plot."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/border" + ChatColor.WHITE + " | Toggle on/off plot border visibility.").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/border")));
                    break;
                case ("home"):
                    sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + " Help - Home " + ChatColor.WHITE + "-------"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "Note:" + ChatColor.WHITE + " /sethome and /home only work in the OverWorld."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/sethome" + ChatColor.WHITE + " | Set your current location for your /home"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/home" + ChatColor.WHITE + " | teleport back to your saved home location."));
                    break;
                case("cash"):
                    sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + " Help - Cash " + ChatColor.WHITE + "-------"));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "Note:" + ChatColor.WHITE + " $1 is earned for each block broken or placed."));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/cash" + ChatColor.WHITE + " | Check your current cash balance.").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/cash")));
                    sender.sendMessage(Component.text(ChatColor.GOLD + "/cash send {user} {amount}" + ChatColor.WHITE + " | Send cash amount to specified user."));
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
        sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + " Help " + ChatColor.WHITE + "-------"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/spawn" + ChatColor.WHITE + " | Teleport to spawn (This only works in the overworld)"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/pvp" + ChatColor.WHITE + " | Toggle PVP on/off"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/help plots" + ChatColor.WHITE + " | View Plot commands").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/help plots")));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/help cash" + ChatColor.WHITE + " | View Cash commands").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/help cash")));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/help home" + ChatColor.WHITE + " | View Home commands").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/help home")));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            return Arrays.asList("plots", "home", "cash");
        }

        return null;
    }
}
