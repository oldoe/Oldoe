package com.oldoe.plugin.commands;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OldoeMute implements CommandExecutor, Listener {
    public Map<UUID, Boolean> mutedPlayers = new HashMap<>();
    private JavaPlugin plugin;

    public OldoeMute() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        loadMutedPlayers();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mute")) {

            Player staffCheck = (Player) sender;
            OldoePlayer oPlayer = PlayerService.GetPlayer(staffCheck.getUniqueId());

            if (!oPlayer.isStaff()) {
                staffCheck.sendMessage(Component.text(ChatColor.RED + "Only staff can use this command!"));
                return false;
            }
            if (args.length < 1) {
                sender.sendMessage("Usage: /mute <player>");
                return true;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage("Player not found.");
                return true;
            }

            UUID playerId = player.getUniqueId();
            boolean isMuted = !mutedPlayers.containsKey(playerId) || !mutedPlayers.get(playerId);

            mutedPlayers.put(playerId, isMuted);

            if (isMuted) {
                sender.sendMessage("Muted " + player.getName() + ".");
                player.sendMessage("You have been muted.");
                saveMutedPlayers();
            } else {
                sender.sendMessage("Unmuted " + player.getName() + ".");
                player.sendMessage("You have been unmuted.");
                saveMutedPlayers();
            }

            return true;
        }

        return false;
    }

    public boolean playerIsMuted(UUID uuid) {
        return mutedPlayers.containsKey(uuid) && mutedPlayers.get(uuid);
    }

    public void loadMutedPlayers() {
        Gson gson = new Gson();
        String json = plugin.getConfig().getString("mutedPlayers");

        if (json != null && !json.isEmpty()) {
            Type type = new TypeToken<Map<UUID, Boolean>>() {
            }.getType();
            mutedPlayers = gson.fromJson(json, type);
        }
    }

    public void saveMutedPlayers() {
        Gson gson = new Gson();
        String json = gson.toJson(mutedPlayers);
        plugin.getConfig().set("mutedPlayers", json);
        plugin.saveConfig();
    }
}
