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

public class MsgCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 0) {
            Player otherPlayer = Bukkit.getPlayer(args[0]);

            if (otherPlayer != null) {

                if (otherPlayer == sender) {
                    sender.sendMessage(Component.text("You can't message yourself!", NamedTextColor.RED));
                    return true;
                }

                StringBuffer sb = new StringBuffer();
                for(int i = 1; i < args.length; i++) {
                    sb.append(args[i] + " ");
                }

                String msg = sb.toString();
                otherPlayer.sendMessage(Component.text("<Message> ", NamedTextColor.GREEN).append(Component.text(otherPlayer.getName() + " > Me: " + msg, NamedTextColor.WHITE)));
                sender.sendMessage(Component.text("<Message> ", NamedTextColor.GREEN).append(Component.text("Me > " + otherPlayer.getName() + ": " + msg, NamedTextColor.WHITE)));

                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    OldoePlayer oSender = PlayerService.GetPlayer(p.getUniqueId());
                    OldoePlayer oRecipient = PlayerService.GetPlayer(otherPlayer.getUniqueId());

                    // Save each other for /reply and msg functions
                    oSender.setLastMessaged(otherPlayer.getName());
                    oRecipient.setLastMessaged(p.getName());
                }

            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    OldoePlayer oPlayer = PlayerService.GetPlayer(p.getUniqueId());
                    String lastMsgName = oPlayer.getLastMessaged();

                    if (lastMsgName != null) {
                        Player recPlayer = Bukkit.getPlayer(lastMsgName);
                        if (recPlayer != null) {

                            OldoePlayer oRecipient = PlayerService.GetPlayer(recPlayer.getUniqueId());

                            StringBuffer sb = new StringBuffer();
                            for(int i = 0; i < args.length; i++) {
                                sb.append(args[i] + " ");
                            }

                            String msg = sb.toString();
                            recPlayer.sendMessage(Component.text("<Message> ", NamedTextColor.GREEN).append(Component.text(recPlayer.getName() + " > Me: " + msg, NamedTextColor.WHITE)));
                            sender.sendMessage(Component.text("<Message> ", NamedTextColor.GREEN).append(Component.text("Me > " + recPlayer.getName() + ": " + msg, NamedTextColor.WHITE)));

                            // Save each other for /reply and msg functions
                            oPlayer.setLastMessaged(recPlayer.getName());
                            oRecipient.setLastMessaged(p.getName());
                        }
                    }
                }
            }
        }

        return true;
    }
}
