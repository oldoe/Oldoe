package com.oldoe.plugin.services;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private static PlayerService playerService = new PlayerService();
    private static CommandService commandService = new CommandService();
    private static EventService eventService = new EventService();
    private static DataService dataService = new DataService();
    private static Location randomTPLocation;

    public static PlayerService PlayerService() {
        return playerService;
    }

    public static CommandService CommandService() {
        return commandService;
    }
    public static EventService EventsService() {
        return eventService;
    }

    public static DataService DataService() {
        return dataService;
    }

    public static Location RandomTPLocation() {
        return randomTPLocation;
    }

    public void Register(JavaPlugin instance) {
        commandService.registerCommands(instance);
        eventService.registerEvents(instance);
        dataService.registerService(Oldoe.getInstance());

        int min = -200; // Minimum value of range
        int max = 200; // Maximum value of range

        BukkitScheduler scheduler = Oldoe.getInstance().getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(instance, () -> {
            int x = (int)Math.floor(Math.random() * (max - min + 1) + min);
            int z = (int)Math.floor(Math.random() * (max - min + 1) + min);
            randomTPLocation = Oldoe.getInstance().getServer().getWorlds().get(0).getHighestBlockAt(x, z).getLocation();
        }, 0L, 1200L);

        scheduler.scheduleSyncRepeatingTask(instance, () -> {
            Instant now = Instant.now();

            List<Player> toKick = new ArrayList<>();

            for(OldoePlayer p : PlayerService.GetPlayers()) {
                Player player = Bukkit.getPlayer(p.getName());
                Duration res = Duration.between(p.getLastMovement(), now);
                Duration loginDiff = Duration.between(p.getLoginTime(), now);

                if (loginDiff.toMinutes() > 1438) {
                    toKick.add(player);
                }

                if (res.toMinutes() > 5L ) {
                    if (res.toMinutes() < 7L) {
                        player.sendMessage(ChatColor.GOLD + "You are now afk.");
                    }
                    player.setSleepingIgnored(true);
                } else {
                    player.setSleepingIgnored(false);
                }
            }

            for (Player player : toKick) {
                player.kick(Component.text("24H AFK Kick"));
            }
        }, 0L, 1200L);
    }

    public void UnRegister() {
        dataService.DisableService();
    }
}
