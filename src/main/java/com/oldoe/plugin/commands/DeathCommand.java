package com.oldoe.plugin.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeathCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            Location loc = player.getLastDeathLocation();

            if (loc != null) {
                player.sendMessage(ChatColor.GREEN + "Last Death location: " + ChatColor.WHITE + loc.getWorld().getName() + " at (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
            } else {
                player.sendMessage(ChatColor.RED + "Last Death location unknown");
            }
        }

        return true;
    }

}
