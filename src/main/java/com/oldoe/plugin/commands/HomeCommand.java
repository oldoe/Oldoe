package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                Location home = GetHome(uuid);
                if (home == null) {
                    player.sendMessage("Error, no home set! Set a home by typing /sethome");
                } else {
                    player.teleport(home);
                }
            } else {
                player.sendMessage("The /home command does not work here.");
            }
        }

        return true;
    }

    private Location GetHome(String uuid) {
        Location homeLocation = null;
        try {
            String sql = String.format(
                    "SELECT * FROM `oldoe_homes` sh JOIN `oldoe_users` su ON sh.uuid = su.id WHERE su.uuid = '%s'",
                    uuid
            );
            ResultSet rs = Oldoe.GetDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    homeLocation = new Location(
                            Bukkit.getWorld(rs.getString("world")),
                            rs.getDouble("x"),
                            rs.getDouble("y"),
                            rs.getDouble("z"),
                            rs.getFloat("yaw"),
                            rs.getFloat("pitch")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Oldoe.GetDatabase().close();
            return homeLocation;
        }
    }

}
