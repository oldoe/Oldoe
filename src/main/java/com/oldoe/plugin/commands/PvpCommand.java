package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PvpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            OldoePlayer oPlayer = PlayerService.GetPlayer(((Player) sender).getUniqueId());

            if (!oPlayer.isPvpEnabled()) {
                oPlayer.setPVP(true);
                sender.sendMessage(Component.text("PVP Enabled! ", NamedTextColor.RED).append(Component.text("(Type /pvp to toggle pvp)", NamedTextColor.WHITE)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/pvp")));
            } else {

                sender.sendMessage(ChatColor.RED + "Disabling pvp in 10 seconds!");

                Oldoe.GetScheduler().scheduleSyncDelayedTask(Oldoe.getInstance(), () -> {
                    sender.sendMessage(ChatColor.RED + "Disabling pvp in 5 seconds...");
                }, 100L);

                Oldoe.GetScheduler().scheduleSyncDelayedTask(Oldoe.getInstance(), () -> {
                    oPlayer.setPVP(false);
                    sender.sendMessage(Component.text("PVP Disabled! ", NamedTextColor.GREEN).append(Component.text("(Type /pvp to toggle pvp)", NamedTextColor.WHITE)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/pvp")));
                }, 200L);
            }
            return true;

        } else {
            sender.sendMessage("This command can only be sent by a player.");
            return false;
        }
    }
}
