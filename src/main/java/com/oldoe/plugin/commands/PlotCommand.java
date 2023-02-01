package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.oldoe.plugin.converters.CoordConverter.CoordToPlot;
import static com.oldoe.plugin.database.PreparedQueries.*;

public class PlotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof  Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            if (args.length > 0) {
                switch (args[0]) {
                    case ("buy"):
                        Buy(player, uuid);
                        break;
                    case ("sell"):
                        Sell(player, uuid);
                        break;
                    case("list"):
                        List(player);
                        break;
                    case("add"):
                        Add(player);
                        break;
                    case("remove"):
                        Remove(player);
                        break;
                }
            }
            else {
                String owner = "None";

                Location loc = player.getLocation();

                int x = CoordToPlot(loc.getX());
                int z = CoordToPlot(loc.getZ());

                try {
                    String sql = String.format(
                            "SELECT sh.name FROM `oldoe_users` sh JOIN `oldoe_plots` su ON sh.id = su.owner WHERE su.x= '%d' AND su.z = '%d'",
                            x, z
                    );
                    ResultSet rs = Oldoe.GetDatabase().executeSQL(sql);
                    if (rs != null) {
                        while (rs.next()) {
                            owner = rs.getString("name");
                        }
                    }
                } catch (SQLException e) {
                    //e.printStackTrace();
                } finally {
                    Oldoe.GetDatabase().close();
                }

                player.sendMessage(ChatColor.WHITE +
                                "-----------------",
                                "Plot Owner: " + owner,
                                "Coordinates: (" + x + ", " + z + ")",
                                "-----------------"
                        );
            }
        }

        return true;
    }

    private void Buy(Player player, String uuid) {

        Location loc = player.getLocation();

        Boolean isPublic = IsPlotPublic(loc);

        if (!isPublic) {
            player.sendMessage(ChatColor.RED + "This plot already has an owner.");
            return;
        }

        BigDecimal plotPrice = new BigDecimal(25000);

        if (GetCash(uuid).compareTo(plotPrice) > -1) {

            int userID =  Oldoe.GetDatabase().getPlayerID(uuid);

            UpdateMoney(uuid, false, plotPrice);

            int x = CoordToPlot(loc.getX());
            int z = CoordToPlot(loc.getZ());

            String name = "None";

            String sql = String.format(
                    "INSERT INTO `oldoe_plots` (owner, world, name, x, z) " +
                            "VALUES (%d, '%s', '%s', %d, %d)",
                    userID,
                    loc.getWorld().getName(),
                    name,
                    x,
                    z
            );
            Oldoe.GetDatabase().executeSQL(sql);
            Oldoe.GetDatabase().close();

            player.sendMessage(ChatColor.GREEN + "Plot purchased! (" + x + ", " + z + ")");
        }
    }

    private void Sell(Player player, String uuid) {
        Location loc = player.getLocation();
        if (IsPlotOwner(uuid, loc)) {
            BigDecimal plotPrice = new BigDecimal(25000);
            UpdateMoney(uuid, true, plotPrice);

            DeletePlot(loc);

            int x = CoordToPlot(loc.getX());
            int z = CoordToPlot(loc.getZ());

            player.sendMessage(ChatColor.GREEN + "Plot sold! (" + x + ", " + z + ")");
        } else {
            player.sendMessage(ChatColor.RED + "You do not own this plot!");
        }

    }

    private void List(Player player) {

    }

    private void Add(Player player) {

    }

    private void Remove(Player player) {

    }
}
