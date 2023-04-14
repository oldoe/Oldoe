package com.oldoe.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                player.teleport(player.getWorld().getSpawnLocation());
                player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 0.9f, 0.5f);
            } else {
                player.sendMessage(ChatColor.RED + "The /spawn command does not work here.");
            }
        }

        return true;
    }

}
