package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.LastSeenDTO;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import static com.oldoe.plugin.database.PreparedQueries.GetLastSeenByName;

public class SeenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {

            Player player = Bukkit.getServer().getPlayer(args[0]);
            if (player != null && player.isOnline()) {
                sender.sendMessage(Component.text("Player: " + player.getName() + " is currently online!", NamedTextColor.GREEN));
            }

            LastSeenDTO lastSeen = GetLastSeenByName(args[0]);

            if (lastSeen.GetName() != null && lastSeen.GetLastSeen() != null) {
                sender.sendMessage(Component.text(lastSeen.GetName() + " was last seen: " + GetFormattedLastSeen(lastSeen.GetLastSeen()), NamedTextColor.YELLOW));
            } else {
                sender.sendMessage(Component.text("Can't find any player with the name: " + args[0], NamedTextColor.RED));
            }
        }

        return true;
    }

    private String GetFormattedLastSeen(Timestamp stamp) {
        Instant lastSeen = stamp.toInstant();
        Duration duration = Duration.between(lastSeen, Instant.now());
        return duration.toDaysPart() + " days " + duration.toHoursPart() + " hours " + duration.toMinutesPart() + " minutes ago";
    }

}
