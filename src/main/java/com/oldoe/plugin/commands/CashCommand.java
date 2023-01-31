package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class CashCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            BigDecimal cash = new BigDecimal(0.00);

            try {
                String sql = String.format("SELECT `cash` FROM `oldoe_users` WHERE `uuid` = '%s'", uuid);
                ResultSet resultSet = Oldoe.GetDatabase().executeSQL(sql);

                if (resultSet != null) {
                    while (resultSet.next()) {
                        cash = resultSet.getBigDecimal("cash");
                    }
                }
            } catch (SQLException e) {
            }
            finally {
                Oldoe.GetDatabase().close();
            }

            player.sendMessage(ChatColor.GREEN + "Cash: " + cash.toString());
        }

        return true;
    }

}
