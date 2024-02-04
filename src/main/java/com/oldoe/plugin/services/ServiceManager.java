package com.oldoe.plugin.services;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceManager {

    private static PlayerService playerService = new PlayerService();
    private static CommandService commandService = new CommandService();
    private static EventService eventService = new EventService();
    private static DataService dataService = new DataService();
    private static Location randomTPLocation;
    private static final List<Biome> BlockedRtpBiomes = Arrays.asList(Biome.DEEP_COLD_OCEAN, Biome.DEEP_OCEAN, Biome.COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.OCEAN, Biome.WARM_OCEAN);

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

        InitScheduler(instance);
    }

    public void UnRegister() {
        dataService.DisableService();
    }

    private void InitScheduler(JavaPlugin instance) {
        int min = -9000; // Minimum value of range
        int max = 9000; // Maximum value of range

        BukkitScheduler scheduler = Oldoe.getInstance().getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(instance, () -> {
            boolean foundLoc = false;

            // Set RTP to random location that is not in the Blocked list (oceans)
            while(!foundLoc) {
                int x = (int)Math.floor(Math.random() * (max - min + 1) + min);
                int z = (int)Math.floor(Math.random() * (max - min + 1) + min);
                Biome biome = Oldoe.getInstance().getServer().getWorlds().get(0).getBiome(x, 90, z);

                if (!BlockedRtpBiomes.contains(biome)) {
                    randomTPLocation = Oldoe.getInstance().getServer().getWorlds().get(0).getHighestBlockAt(x, z).getLocation().add(0, 1, 0);
                    foundLoc = true;
                }
            }

            // Keep alive for DB to prevent connection timeout
            String sql = String.format("SELECT * FROM `oldoe_users` LIMIT 1");
            DataService.getDatabase().executeSQL(sql);
            DataService.getDatabase().close();

        }, 0L, 6000L);

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
}
