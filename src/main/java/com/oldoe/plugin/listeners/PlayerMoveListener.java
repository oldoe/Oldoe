package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.models.Plot;
import com.oldoe.plugin.services.PlayerService;
import com.oldoe.plugin.services.ServiceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Duration;
import java.time.Instant;

import static com.oldoe.plugin.database.PreparedQueries.*;
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
                    // Fairy attack
                    //player.spawnParticle(Particle.END_ROD, player.getLocation().getBlockX(), player.getLocation().getBlockY() +1.5, player.getLocation().getBlockZ(),  50, 5, 0, 5);

                    // if user has /border on.
                    int PlotXCenter = CoordToPlot(toLoc.getX());
                    int PlotZCenter = CoordToPlot(toLoc.getZ());

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter - 64 + i;
                        int z = PlotZCenter - 64;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        player.spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter - 64 + i;
                        int z = PlotZCenter + 64;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        player.spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter + 64;
                        int z = PlotZCenter - 64 + i;
                        int y = event.getPlayer().getWorld().getHighestBlockAt(x, z).getY();
                        player.spawnParticle(Particle.END_ROD, x, y +1.5, z,  0);
                    }

                    for(int i=0;i<=128;i++){
                        int x = PlotXCenter - 64;
                        int z = PlotZCenter - 64 + i;
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

                Plot fromPlot = GetPlotbyLocation(event.getFrom());

                Plot toPlot = GetPlotbyLocation(event.getTo());

                // public to public, no message, don't do further queries
                if (fromPlot.isPublic() && toPlot.isPublic()) {
                    return;
                }

                if (toPlot.isPublic()) {
                    player.sendActionBar(Component.text(ChatColor.GREEN + "You have entered a public plot."));
                    return;
                }

                if (fromPlot.isPublic() && toPlot.hasPerms(oPlayer.getID())) {
                    player.sendActionBar(Component.text(ChatColor.GREEN + "Private plot, you have permissions here."));
                    return;
                }

                if (fromPlot.isPublic() && !toPlot.hasPerms(oPlayer.getID())) {
                    player.sendActionBar(Component.text(ChatColor.RED + "Private plot, you do not have permissions here."));
                    return;
                }

                if (fromPlot.hasPerms(oPlayer.getID()) && !toPlot.hasPerms(oPlayer.getID())) {
                    player.sendActionBar(Component.text(ChatColor.RED + "Private plot, you do not have permissions here."));
                    return;
                }

                if (!fromPlot.hasPerms(oPlayer.getID()) && toPlot.hasPerms(oPlayer.getID())) {
                    player.sendActionBar(Component.text(ChatColor.GREEN + "Private plot, you have permissions here."));
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

                    final Component mainTitle = Component.text("Teleported", NamedTextColor.GREEN);
                    final Component subtitle = Component.text("(" + ServiceManager.RandomTPLocation().getBlockX() + ", " + ServiceManager.RandomTPLocation().getBlockZ() + ")", NamedTextColor.WHITE);
                    final Title title = Title.title(mainTitle, subtitle);
                    player.showTitle(title);

                    player.sendMessage(ChatColor.GREEN + "[Tip] " + ChatColor.WHITE + "Random TP machine changes location every 5 minutes");

                    Oldoe.GetScheduler().scheduleSyncDelayedTask(Oldoe.getInstance(), () -> {
                        player.sendMessage(ChatColor.GREEN + "[Tip] " + ChatColor.WHITE + "Once you've found a good spot you can type /sethome and return to that location with the /home command.");
                    }, 300L);

                }
            }
        }
    }
}
