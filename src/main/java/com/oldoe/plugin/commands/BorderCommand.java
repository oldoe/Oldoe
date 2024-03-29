package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BorderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
            oPlayer.toggleBorder();

            if (oPlayer.borderEnabled()) {
                player.sendMessage(Component.text("Plot borders enabled! ", NamedTextColor.GREEN).append(Component.text("(Type /border to toggle)", NamedTextColor.WHITE)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/border")));
            } else {
                player.sendMessage(Component.text("Plot borders disabled! ", NamedTextColor.RED).append(Component.text("(Type /border to toggle)", NamedTextColor.WHITE)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/border")));
            }

            return true;
        }
        return false;
    }
}
