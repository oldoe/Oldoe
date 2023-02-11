package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.oldoe.plugin.converters.CoordConverter.CoordToPlot;
import static com.oldoe.plugin.database.PreparedQueries.*;

public class PlotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof  Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            if (!player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                player.sendMessage(ChatColor.RED + "The /plot command does not work here.");
                return false;
            }

            if (args.length > 0) {
                switch (args[0]) {
                    case ("buy"):
                        Buy(player, uuid);
                        break;
                    case ("sell"):
                        Sell(player, uuid);
                        break;
                    case("list"):
                        List(player, uuid);
                        break;
                    case("add"):
                        ModifyMembers(player, uuid, args, true);
                        break;
                    case("remove"):
                        ModifyMembers(player, uuid, args, false);
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "Command usage: /plot add/remove {name}");
                        player.sendMessage(ChatColor.RED + "Command usage: /plot buy/sell");
                        break;
                }
            }
            else {
                String owner = "None";

                Location loc = player.getLocation();

                int x = CoordToPlot(loc.getBlockX());
                int z = CoordToPlot(loc.getBlockZ());

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
                    Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
                } finally {
                    Oldoe.GetDatabase().close();
                }

                int plotID = GetPlotID(loc);
                List<String> members = GetPlotMembers(plotID);

                player.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + "Plot Info" + ChatColor.WHITE + "------",
                                ChatColor.GOLD + "Plot Owner: " + ChatColor.WHITE + owner,
                                ChatColor.GOLD + "Coordinates: " + ChatColor.WHITE + "(" + x + ", " + z + ")",
                                ChatColor.GOLD + "Members: " + ChatColor.WHITE + members.toString());
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

            int x = CoordToPlot(loc.getBlockX());
            int z = CoordToPlot(loc.getBlockZ());

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
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.9f, 0.5f);
        }
    }

    private void Sell(Player player, String uuid) {
        Location loc = player.getLocation();
        if (IsPlotOwner(uuid, loc)) {
            BigDecimal plotPrice = new BigDecimal(25000);
            UpdateMoney(uuid, true, plotPrice);

            DeletePlot(loc);

            int x = CoordToPlot(loc.getBlockX());
            int z = CoordToPlot(loc.getBlockZ());

            player.sendMessage(ChatColor.GREEN + "Plot sold! You no longer own: (" + x + ", " + z + ")");
            player.sendMessage(ChatColor.GREEN + "You received $" + plotPrice + " for your plot.");
        } else {
            player.sendMessage(ChatColor.RED + "You do not own this plot!");
        }

    }

    private void List(Player player, String uuid) {
        int userID =  Oldoe.GetDatabase().getPlayerID(uuid);

        List<String> Plots = new ArrayList<>();

        try {
            String sql = String.format("SELECT * FROM `oldoe_plots` WHERE owner= '%d'", userID);
            ResultSet rs = Oldoe.GetDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    Plots.add(String.format("X: %d Z: %d", rs.getInt("x"), rs.getInt("z")));
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            Oldoe.GetDatabase().close();
        }

        player.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + "Plot List" + ChatColor.WHITE + "------");
        for (String p : Plots) {
            player.sendMessage(ChatColor.WHITE + p);
        }

        if (Plots.size() == 0) {
            player.sendMessage(ChatColor.RED + "You don't have any plots.");
        }
    }

    /*
     * /plot add/remove {user}
     */
    private void ModifyMembers(Player player, String uuid, String[] args, boolean add) {
        if (args.length > 1) {

            Location loc = player.getLocation();
            if (IsPlotOwner(uuid, loc)) {

                int newMemberID = GetPlayerIdByName(args[1]);

                if (newMemberID != -1) {
                    int plotID = GetPlotID(loc);

                    if (add) {

                        if(IsPlotMember(plotID, newMemberID)) {
                            player.sendMessage(ChatColor.RED + args[1] + " is already a member of this plot.");
                            return;
                        }

                        String sql = String.format(
                                "INSERT INTO `oldoe_plot_perms` (plot, user) " +
                                        "VALUES (%d, %d)",
                                plotID,
                                newMemberID
                        );
                        Oldoe.GetDatabase().executeSQL(sql);
                        Oldoe.GetDatabase().close();

                        player.sendMessage(ChatColor.GREEN + args[1] + " added to plot.");
                    } else {

                        if(!IsPlotMember(plotID, newMemberID)) {
                            player.sendMessage(ChatColor.RED + args[1] + " is already not a member of this plot.");
                            return;
                        }

                        String sql = String.format(
                                "DELETE FROM `oldoe_plot_perms` WHERE plot = '%d' AND user = '%d'",
                                plotID,
                                newMemberID
                        );
                        Oldoe.GetDatabase().executeSQL(sql);
                        Oldoe.GetDatabase().close();

                        player.sendMessage(ChatColor.GREEN + args[1] + " removed from plot.");
                    }


                } else {
                    player.sendMessage(ChatColor.RED + "Sorry, unable to find a player by that name.");
                }

            } else {
                player.sendMessage(ChatColor.RED + "Only the plot owner can add/remove users!");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Command usage: /plot add {player}");
            player.sendMessage(ChatColor.RED + "Command usage: /plot remove {player}");
        }
    }
}
