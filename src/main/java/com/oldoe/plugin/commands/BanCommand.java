package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.logging.Level;

import static com.oldoe.plugin.database.PreparedQueries.BanUser;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        int staffId = 0;

        if (sender instanceof Player player) {
            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());

            if (!oPlayer.isStaff()) {
                player.sendMessage(Component.text("Only staff can use this command!", NamedTextColor.RED));
                return false;
            }
            staffId = oPlayer.getID();
        }

        final int ownerID = staffId;

        if (args.length < 2) {
            sender.sendMessage("Usage: /ban {player} {Reason}");
        } else {
            String user = args[0];
            String[] restOfArgs = Arrays.asList(args).subList(1, args.length).toArray(new String[0]);
            String reason = String.join(" ", restOfArgs);

            OfflinePlayer recipient = Bukkit.getOfflinePlayer(user);

            recipient.banPlayer(reason);

            Oldoe.GetScheduler().runTaskAsynchronously(Oldoe.getInstance(), () -> {
                BanUser(recipient.getUniqueId().toString(), recipient.getName(), reason, ownerID);
            });

            sender.sendMessage(Component.text("Banned user: " + recipient.getName() + " for reason: " + reason, NamedTextColor.RED));

            Oldoe.getInstance().getServer().getLogger().log(Level.INFO, Oldoe.getInstance().getName() + "> " + recipient.getName() + " banned by " + sender.getName() + " for reason: " + reason);
        }

        return true;
    }

}
