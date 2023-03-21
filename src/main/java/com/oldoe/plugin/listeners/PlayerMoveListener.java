package com.oldoe.plugin.listeners;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import com.oldoe.plugin.services.ServiceManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Duration;
import java.time.Instant;

import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;
import static com.oldoe.plugin.database.PreparedQueries.IsPlotPublic;
import static com.oldoe.plugin.helpers.CoordConverter.CoordToPlot;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location fromLoc = event.getFrom();
        Location toLoc = event.getTo();

        // Player moving to a new block (To avoid counting yaw/pitch change)
        if (fromLoc.getBlockX() != toLoc.getBlockX() || fromLoc.getBlockZ() != toLoc.getBlockZ()) {

            Player player = event.getPlayer();

            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());

            // If player moves to a new block, count the activity.
            oPlayer.setLastMovementNow();

            if (toLoc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {

                if (oPlayer.borderEnabled()) {

                    // if user has /border on.
                    int PlotXCenter = CoordToPlot(toLoc.getX());
                    int PlotZCenter = CoordToPlot(toLoc.getZ());

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter -64 + i;
                        int z = PlotZCenter -64;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        player.spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter -64 + i;
                        int z = PlotZCenter +64;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        player.spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter +64;
                        int z = PlotZCenter -64 + i;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        player.spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter -64;
                        int z = PlotZCenter -64 + i;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        player.spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                }

                // Only do permission check on move everytime they cross through a border. This is to prevent extra queries
                if ((fromLoc.getBlockX() >> 6) == (toLoc.getBlockX() >> 6) && (fromLoc.getBlockZ() >> 6) == (toLoc.getBlockZ() >> 6)) {
                    return;
                }

                Duration lastMessageDiff = Duration.between(oPlayer.getLastPlotMessageTime(), Instant.now());

                if (lastMessageDiff.toSeconds() < 5L) {
                    return;
                }

                oPlayer.setLastPlotMessageNow();

                boolean isFromPublic = IsPlotPublic(event.getFrom());
                boolean isToPublic = IsPlotPublic(event.getTo());

                // public to public, no message, don't do further queries
                if (isFromPublic && isToPublic) {
                    return;
                }

                boolean hasPermsFrom = HasPlotPermissions(oPlayer.getID(), event.getFrom());
                boolean hasPermsTo = HasPlotPermissions(oPlayer.getID(), event.getTo());

                boolean isPrivate = false;

                if (isToPublic) {
                    player.sendMessage(ChatColor.GREEN + "You have entered a public plot.");
                    return;
                }

                if (isFromPublic && hasPermsTo) {
                    player.sendMessage(ChatColor.GREEN + "Private plot, you have permissions here.");
                    return;
                }

                if (isFromPublic && !hasPermsTo) {
                    player.sendMessage(ChatColor.RED + "Private plot, you do not have permissions here.");
                    return;
                }

                if (hasPermsFrom && !hasPermsTo) {
                    player.sendMessage(ChatColor.RED + "Private plot, you do not have permissions here.");
                    return;
                }

                if (!hasPermsFrom && hasPermsTo) {
                    player.sendMessage(ChatColor.GREEN + "Private plot, you have permissions here.");
                    return;
                }
            }
            // If player falling verticle
        } else if (fromLoc.getBlockY() != toLoc.getBlockY()) {
            if (toLoc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                Location loc = toLoc.getBlock().getLocation();
                boolean isTpPortal = false;
                outerloop:
                for (int x = -2; x < 3; x++) {
                    for (int z = -2; z < 3; z++) {
                        if (x != 0 || z != 0) {
                            if (toLoc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY(), loc.getBlockZ() + z).getType() == Material.GOLD_BLOCK) {
                                isTpPortal = true;
                            } else {
                                isTpPortal = false;
                                break outerloop;
                            }
                        }
                    }
                }

                if (isTpPortal) {
                    Player player = event.getPlayer();
                    player.teleport(ServiceManager.RandomTPLocation());
                }
            }
        }

    }
}
