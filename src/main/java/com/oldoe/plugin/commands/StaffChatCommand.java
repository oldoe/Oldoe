package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());

            if (!oPlayer.isStaff()) {
                player.sendMessage(Component.text("Only staff can use this command!", NamedTextColor.RED));
                return false;
            }

            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < args.length; i++) {
                sb.append(args[i] + " ");
            }

            String msg = sb.toString();

            for( Player p : Bukkit.getOnlinePlayers()) {
                OldoePlayer oP = PlayerService.GetPlayer(p.getUniqueId());
                if (oP.isStaff()) {
                    p.sendMessage(Component.text("<staff> ", NamedTextColor.DARK_PURPLE).append(Component.text(sender.getName() + ": " + msg, NamedTextColor.WHITE)));
                }
            }
        }

        return true;
    }
}
