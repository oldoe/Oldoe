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
        sender.sendMessage(Component.text(ChatColor.WHITE + "-------" + ChatColor.GOLD + "HELP" + ChatColor.WHITE + "-------"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/SetHome" + ChatColor.WHITE + " | Set your current location for your /home"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/Home" + ChatColor.WHITE + " | Teleport to your home (This only works in the overworld)"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/spawn" + ChatColor.WHITE + " | Teleport to spawn (This only works in the overworld)"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/plot" + ChatColor.WHITE + " | See status of plot you're currently standing in"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/plot buy/sell" + ChatColor.WHITE + " | Buy or sell a plot (Cost: $25,000)"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/border" + ChatColor.WHITE + " | Visualize plot borders"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/cash" + ChatColor.WHITE + " | See current balance. You get $1 per block placed or removed"));
        sender.sendMessage(Component.text(ChatColor.GOLD + "/pvp" + ChatColor.WHITE + " | Toggle PVP on/off"));
        return true;
    }
}
