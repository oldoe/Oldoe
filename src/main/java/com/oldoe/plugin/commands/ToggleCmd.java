package com.oldoe.plugin.commands;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class ToggleCmd implements CommandExecutor {
    public HashSet<UUID> pmSpy = new HashSet<>();
    public HashSet<UUID> cmdSpy = new HashSet<>();
    public HashSet<UUID> staffChat = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
            if (args.length <= 0) {
                if (oPlayer.isStaff()) {
                    player.sendMessage(ChatColor.GRAY + "-------------------");
                    player.sendMessage(ChatColor.GRAY + "/toggle pmspy: " + ChatColor.WHITE + "Enables staff to see player messages " + toggleCheck(player.getUniqueId(), "pmspy"));
                    player.sendMessage(ChatColor.GRAY + "/toggle cmdspy: " + ChatColor.WHITE + "Enables staff to see player commands " + toggleCheck(player.getUniqueId(), "cmdspy"));
                    player.sendMessage(ChatColor.GRAY + "/toggle staffchat: " + ChatColor.WHITE + "Makes all messages go to staff chat " + toggleCheck(player.getUniqueId(), "staffchat"));
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
        // Staff
        if (reqToggle.equalsIgnoreCase("pmspy") && oPlayer.isStaff()) {
            if (pmSpy.contains(sender.getUniqueId())) {
                pmSpy.remove(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "PM Spy has been disabled.");
            } else {
                pmSpy.add(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "PM Spy has been enabled.");
            }
        } else if (reqToggle.equalsIgnoreCase("cmdspy") && oPlayer.isStaff()) {
            if (cmdSpy.contains(sender.getUniqueId())) {
                cmdSpy.remove(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Command Spy has been disabled.");
            } else {
                cmdSpy.add(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Command Spy has been enabled.");
            }
        } else if (reqToggle.equalsIgnoreCase("staffchat") && oPlayer.isStaff()) {
            if (staffChat.contains(sender.getUniqueId())) {
                staffChat.remove(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Staff Chat has been disabled.");
            } else {
                staffChat.add(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Staff Chat has been enabled.");
            }
        }
    }

    public String toggleCheck(UUID uuid, String toggle) {
        // Staff
        String output = pmSpy.contains(uuid) && toggle.equals("pmspy") ? ChatColor.GREEN + "(Enabled)" : ChatColor.RED + "(Disabled)";
        output = cmdSpy.contains(uuid) && toggle.equals("cmdspy") ? ChatColor.GREEN + "(Enabled)" : output;
        output = staffChat.contains(uuid) && toggle.equals("staffchat") ? ChatColor.GREEN + "(Enabled)" : output;
        return output;
    }



    public Iterator<UUID> getPmSpyIterator() {
        return pmSpy.iterator();
    }

    public Iterator<UUID> getCmdSpyIterator() {
        return cmdSpy.iterator();
    }

    public Iterator<UUID> getStaffChatIterator() {
        return staffChat.iterator();
    }
}