package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.oldoe.plugin.converters.CoordConverter.CoordToPlot;

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
                        Sell(player);
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
        }

        return true;
    }

    private void Buy(Player player, String uuid) {
        // For test purpose
        if (player.isOp()) {

            // TODO, check if plot is already owned, can be in the sql call via an exception (if already exists respond)
            int userID =  Oldoe.GetDatabase().getPlayerID(uuid);

            Location loc = player.getLocation();

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

    private void Sell(Player player) {

    }

    private void List(Player player) {

    }

    private void Add(Player player) {

    }

    private void Remove(Player player) {

    }
}
