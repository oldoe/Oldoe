package com.oldoe.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof  Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            if (args.length > 0) {
                switch (args[0]) {
                    case ("buy"):
                        Buy(player);
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

    private void Buy(Player player) {

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
