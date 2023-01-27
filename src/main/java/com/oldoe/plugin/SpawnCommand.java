package com.oldoe.plugin;

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
            } else {
                player.sendMessage("The /spawn command does not work here.");
            }
        }

        return true;
    }

}
