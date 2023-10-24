package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());

            if (!oPlayer.isLockMode()) {
                oPlayer.setLockMode(true);
                player.sendMessage(Component.text("Lock Mode Enabled. Use a stick and left click items to lock them.", NamedTextColor.GREEN));
            } else {
                oPlayer.setLockMode(false);
                player.sendMessage(Component.text("Lock Mode Disabled", NamedTextColor.RED));
            }
        }

        return true;
    }
}
