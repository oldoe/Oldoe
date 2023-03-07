package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());

            if (!oPlayer.isStaff()) {
                player.sendMessage(Component.text(ChatColor.RED + "Only staff can use this command!"));
                return false;
            }

            if (args.length > 0) {
                player.setGameMode(GameMode.SPECTATOR);
                Player targetP = Bukkit.getPlayer(args[0]);
                if (targetP == null) {
                    player.sendMessage(Component.text(ChatColor.RED + "Player is not online!"));
                } else {
                    player.setSpectatorTarget(targetP);
                }
            } else {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    player.setGameMode(GameMode.SPECTATOR);
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                    // TP back to start location
                }
            }
        }

        return true;
    }

}
