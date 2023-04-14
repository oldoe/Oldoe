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

            if (oPlayer.isStaffEnabled()) {
                if (args.length <= 0) {
                    oPlayer.setStaffEnabled(false);
                    player.teleport(oPlayer.getStaffStartLocation());
                    player.sendMessage(Component.text(ChatColor.GREEN + "Staff Mode Disabled."));
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setSleepingIgnored(false);
                    player.setInvisible(false);
                }
            }
            else
            {
                oPlayer.setStaffEnabled(true);
                oPlayer.setLastStaffLocation(player.getLocation());
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(Component.text(ChatColor.GREEN + "Staff Mode Enabled."));
                player.setSleepingIgnored(true);
                player.setInvisible(true);
            }

            if ( oPlayer.isStaffEnabled() && args.length > 0) {

                if (args.length > 1) {

                    if (args[0].equalsIgnoreCase("end")) {
                        Player targetP = Bukkit.getPlayer(args[1]);
                        if (targetP == null) {
                            player.sendMessage(Component.text(ChatColor.RED + "Player is not online!"));
                        } else {
                            player.openInventory(targetP.getEnderChest());
                        }
                    } else if (args[0].equalsIgnoreCase("open")) {
                        Player targetP = Bukkit.getPlayer(args[1]);
                        if (targetP == null) {
                            player.sendMessage(Component.text(ChatColor.RED + "Player is not online!"));
                        } else {
                            player.openInventory(targetP.getInventory());
                        }
                    }

                } else {
                    Player targetP = Bukkit.getPlayer(args[0]);
                    if (targetP == null) {
                        player.sendMessage(Component.text(ChatColor.RED + "Player is not online!"));
                    } else {
                        player.setSpectatorTarget(targetP);
                    }
                }
            }
        }

        return true;
    }

}
