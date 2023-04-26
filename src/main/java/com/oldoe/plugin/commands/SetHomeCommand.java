package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.services.DataService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                String uuid = player.getUniqueId().toString();
                Location loc = player.getLocation();
                Oldoe.GetScheduler().runTaskAsynchronously(Oldoe.getInstance(), () -> {
                    saveHome(uuid, loc);
                });

                player.sendMessage(ChatColor.GREEN + "Home saved!");
            } else {
                player.sendMessage(ChatColor.RED + "The /sethome command does not work here.");
            }
        }

        return true;
    }

    private void saveHome(String uuid, Location loc) {
        int userID = DataService.getDatabase().getPlayerID(uuid);

        String sql = String.format(
                "REPLACE INTO `oldoe_homes` (uuid, world, x, y, z, pitch, yaw) " +
                        "VALUES (%d, '%s', %f, %f, %f, %f, %f)",
                userID,
                loc.getWorld().getName(),
                new BigDecimal(loc.getX()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                new BigDecimal(loc.getY()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                new BigDecimal(loc.getZ()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                loc.getPitch(),
                loc.getYaw()
        );
        DataService.getDatabase().executeSQL(sql);
        DataService.getDatabase().close();
    }

}
