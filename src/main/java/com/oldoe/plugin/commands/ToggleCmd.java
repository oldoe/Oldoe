package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class ToggleCmd implements CommandExecutor {
    public ArrayList<UUID> pmSpy = new ArrayList<>();
    public ArrayList<UUID> cmdSpy = new ArrayList<>();
    public ArrayList<UUID> staffChat = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
            if (args.length <= 0) {
                if (oPlayer.isStaff()) {
                    player.sendMessage(ChatColor.GRAY + "-------------------");
                    player.sendMessage(ChatColor.GRAY + "/toggle pmspy: " + ChatColor.WHITE + "Enables staff to see player messages");
                    player.sendMessage(ChatColor.GRAY + "/toggle cmdspy: " + ChatColor.WHITE + "Enables staff to see player commands");
                    player.sendMessage(ChatColor.GRAY + "/toggle staffchat: " + ChatColor.WHITE + "Makes all messages go to staff chat");
                    player.sendMessage(ChatColor.GRAY + "-------------------");
                } else {
                    player.sendMessage(ChatColor.GRAY + "-------------------");
                    player.sendMessage(ChatColor.GRAY + "No toggles yet, suggest some ideas!");
                    player.sendMessage(ChatColor.GRAY + "-------------------");
                }
            } else {
                toggler(player, args[0]);
            }

        }
        return false;
    }

    public void toggler(Player sender, String reqToggle) {
        OldoePlayer oPlayer = PlayerService.GetPlayer(sender.getUniqueId());
        if (reqToggle.equalsIgnoreCase("pmspy") && oPlayer.isStaff()) pmSpy.add(sender.getUniqueId());
        if (reqToggle.equalsIgnoreCase("cmdspy") && oPlayer.isStaff()) cmdSpy.add(sender.getUniqueId());
        if (reqToggle.equalsIgnoreCase("staffchat") && oPlayer.isStaff()) staffChat.add(sender.getUniqueId());

    }

}
