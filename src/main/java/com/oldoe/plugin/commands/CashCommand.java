package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static com.oldoe.plugin.database.PreparedQueries.GetCash;
import static com.oldoe.plugin.database.PreparedQueries.UpdateMoney;

public class CashCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            DecimalFormat df = new DecimalFormat("#,###.00");
            BigDecimal cash = GetCash(uuid);

            // Commands:
            // /cash
            // /cash send {user} amount
            // comm, args[0], args[1], args[2]
            if (args.length < 1) {
                player.sendMessage(ChatColor.GREEN + "Cash: $" + df.format(cash));
            } else {
                if (args[0].equals("send")) {
                    if (args.length > 1) {
                        String user = args[1];

                        Player recipient = Bukkit.getPlayer(user);

                        if (recipient == null) {
                            player.sendMessage(ChatColor.RED + "User must be online in order to send cash.");
                        } else {
                            BigDecimal sendAmount = new BigDecimal(0.00);
                            try {
                                sendAmount = new BigDecimal(args[2]).setScale(2, RoundingMode.HALF_UP);
                            } catch (NumberFormatException ex) {
                                player.sendMessage(ChatColor.RED + "Invalid number. Command usage: /cash send {user} {amount}.");
                                return true;
                            }

                            if (sendAmount.intValue() >= 1) {
                                // -1 = less than, 0 equal too, 1 greater than
                                if (cash.compareTo(sendAmount) > -1) {
                                    UpdateMoney(uuid, false, sendAmount);
                                    player.sendMessage(ChatColor.GREEN + "You have sent $" + df.format(sendAmount) + " to " + recipient.getName());
                                    UpdateMoney(recipient.getUniqueId().toString(), true, sendAmount);
                                    recipient.sendMessage(ChatColor.GREEN + "You have received $" + df.format(sendAmount) + " from " + player.getName());

                                } else {
                                    player.sendMessage(ChatColor.RED + "You can't send more cash than you currently have!");
                                }
                            } else {
                                SendCommandUsage(player);
                            }
                        }
                    } else {
                        SendCommandUsage(player);
                    }
                } else {
                    SendCommandUsage(player);
                }
            }
        }

        return true;
    }

    private void SendCommandUsage(Player p) {
        p.sendMessage(ChatColor.RED + "Command usage: /cash send {user} {amount}");
    }
}
