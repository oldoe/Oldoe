package com.oldoe.plugin.listeners;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.oldoe.plugin.Oldoe.isBorderActivate;
import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;
import static com.oldoe.plugin.database.PreparedQueries.IsPlotPublic;
import static com.oldoe.plugin.helpers.CoordConverter.CoordToPlot;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location fromLoc = event.getFrom();
        Location toLoc = event.getTo();

        String uuid = event.getPlayer().getUniqueId().toString();

        // Player moving to a new block (To avoid counting yaw/pitch change)
        if (fromLoc.getBlockX() != toLoc.getBlockX() || fromLoc.getBlockZ() != toLoc.getBlockZ()) {
            if (toLoc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {

                if (isBorderActivate(event.getPlayer().getUniqueId())) {

                    // if user has /border on.
                    int PlotXCenter = CoordToPlot(toLoc.getX());
                    int PlotZCenter = CoordToPlot(toLoc.getZ());

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter -64 + i;
                        int z = PlotZCenter -64;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        event.getPlayer().spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter -64 + i;
                        int z = PlotZCenter +64;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        event.getPlayer().spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter +64;
                        int z = PlotZCenter -64 + i;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        event.getPlayer().spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter -64;
                        int z = PlotZCenter -64 + i;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        event.getPlayer().spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                }

                // Only do permission check on move everytime they cross through a border. This is to prevent extra queries
                if ((fromLoc.getBlockX() >> 6) == (toLoc.getBlockX() >> 6) && (fromLoc.getBlockZ() >> 6) == (toLoc.getBlockZ() >> 6)) {
                    return;
                }

                boolean isFromPublic = IsPlotPublic(event.getFrom());
                boolean isToPublic = IsPlotPublic(event.getTo());

                // public to public, no message, don't do further queries
                if (isFromPublic && isToPublic) {
                    return;
                }

                boolean hasPermsFrom = HasPlotPermissions(uuid, event.getFrom());
                boolean hasPermsTo = HasPlotPermissions(uuid, event.getTo());

                boolean isPrivate = false;

                if (isToPublic) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "You have entered a public plot.");
                    return;
                }

                if (isFromPublic && hasPermsTo) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Private plot, you have permissions here.");
                    return;
                }

                if (isFromPublic && !hasPermsTo) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Private plot, you do not have permissions here.");
                    return;
                }

                if (hasPermsFrom && !hasPermsTo) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Private plot, you do not have permissions here.");
                    return;
                }

                if (!hasPermsFrom && hasPermsTo) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Private plot, you have permissions here.");
                    return;
                }
            }
        }

    }
}
