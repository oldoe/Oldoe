package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                String uuid = player.getUniqueId().toString();
                Location loc = player.getLocation();
                saveHome(uuid, loc);
                player.sendMessage("Home saved!");
            } else {
                player.sendMessage("The /sethome command does not work here.");
            }
        }

        return true;
    }

    private void saveHome(String uuid, Location loc) {
        int userID =  Oldoe.GetDatabase().getPlayerID(uuid);
        String sql = String.format(
                "INSERT INTO `oldoe_homes` (uuid, world, x, y, z, pitch, yaw) " +
                        "VALUES (%d, '%s', %f, %f, %f, %f, %f)",
                userID,
                loc.getWorld().getName(),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw()
        );
        Oldoe.GetDatabase().executeSQL(sql);
        Oldoe.GetDatabase().close();
    }

}
