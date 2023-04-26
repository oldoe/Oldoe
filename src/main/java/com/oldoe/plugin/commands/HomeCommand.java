package com.oldoe.plugin.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.oldoe.plugin.database.PreparedQueries.GetPlayerHome;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                Location home = GetPlayerHome(uuid);
                if (home == null) {
                    player.sendMessage(ChatColor.RED + "You currently have no home. Set your home by typing /sethome");
                } else {
                    player.teleport(home);
                    player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 0.9f, 0.5f);
                    /*
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Oldoe.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 0.9f, 0.5f);
                        }
                    }, 20L * 1);

                     */

                }
            } else {
                player.sendMessage(ChatColor.RED + "The /home command does not work here.");
            }
        }

        return true;
    }

}
