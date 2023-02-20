package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import com.oldoe.plugin.services.ServiceManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BorderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId().toString());
            oPlayer.toggleBorder();
            return true;
        }
        return false;
    }
}
