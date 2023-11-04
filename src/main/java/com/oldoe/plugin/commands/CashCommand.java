package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        DecimalFormat df = new DecimalFormat("#,###.##");

        if (sender instanceof Player player) {

            String uuid = player.getUniqueId().toString();

            BigDecimal cash = GetCash(uuid);

            // Commands:
            // /cash
            // /cash send {user} amount
            // comm, args[0], args[1], args[2]
            if (args.length < 1) {
                player.sendMessage(Component.text("Cash: ", NamedTextColor.GREEN).append(Component.text("$" + df.format(cash), NamedTextColor.WHITE)));
            } else {
                if (args[0].equals("send")) {
                    if (args.length > 1) {
                        String user = args[1];

                        Player recipient = Bukkit.getPlayer(user);

                        if (recipient == null) {
                            player.sendMessage(Component.text("User must be online in order to send cash.", NamedTextColor.RED));
                        } else {
                            BigDecimal sendAmount = new BigDecimal(0.00);
                            try {
                                sendAmount = new BigDecimal(args[2]).setScale(2, RoundingMode.HALF_UP);
                            } catch (NumberFormatException ex) {
                                player.sendMessage(Component.text("Invalid number. Command usage: /cash send {user} {amount}.", NamedTextColor.RED));
                                return true;
                            }

                            if (sendAmount.intValue() >= 1) {
                                // -1 = less than, 0 equal too, 1 greater than
                                if (cash.compareTo(sendAmount) > -1) {

                                    BigDecimal finalSendAmount = sendAmount;

                                    Oldoe.GetScheduler().runTaskAsynchronously(Oldoe.getInstance(), () -> {
                                        UpdateMoney(uuid, false, finalSendAmount);
                                        UpdateMoney(recipient.getUniqueId().toString(), true, finalSendAmount);
                                    });

                                    player.sendMessage(ChatColor.GREEN + "You have sent $" + df.format(sendAmount) + " to " + recipient.getName());
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
        else {
            if (args.length > 1 && args[0].equals("send")) {
                if (args.length > 1) {
                    String user = args[1];

                    Player recipient = Bukkit.getPlayer(user);

                    if (recipient == null) {
                    } else {
                        BigDecimal sendAmount = new BigDecimal(0.00);
                        try {
                            sendAmount = new BigDecimal(args[2]).setScale(2, RoundingMode.HALF_UP);
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(Component.text("Invalid number. Command usage: /cash send {user} {amount}.", NamedTextColor.RED));
                            return true;
                        }

                        if (sendAmount.intValue() >= 1) {
                            BigDecimal finalSendAmount = sendAmount;

                            Oldoe.GetScheduler().runTaskAsynchronously(Oldoe.getInstance(), () -> {
                                UpdateMoney(recipient.getUniqueId().toString(), true, finalSendAmount);
                            });
                            recipient.sendMessage(ChatColor.GREEN + "You have received $" + df.format(sendAmount) + " from " + sender.getName());

                        }
                    }
                }
            }
        }

        return true;
    }

    private void SendCommandUsage(Player p) {
        p.sendMessage(ChatColor.RED + "Command usage: /cash send {user} {amount}");
    }
}
