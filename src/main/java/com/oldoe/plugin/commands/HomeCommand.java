package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.services.DataService;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                Location home = GetHome(uuid);
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

    private Location GetHome(String uuid) {
        Location homeLocation = null;
        try {
            String sql = String.format(
                    "SELECT * FROM `oldoe_homes` sh JOIN `oldoe_users` su ON sh.uuid = su.id WHERE su.uuid = '%s'",
                    uuid
            );
            ResultSet rs = DataService.getDatabase().executeSQL(sql);
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
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            DataService.getDatabase().close();
            return homeLocation;
        }
    }

}
