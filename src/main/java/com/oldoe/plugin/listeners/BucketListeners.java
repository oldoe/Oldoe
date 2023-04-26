package com.oldoe.plugin.listeners;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.models.Plot;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import static com.oldoe.plugin.database.PreparedQueries.GetPlotbyLocation;
import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class BucketListeners implements Listener {

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event){
        Player player = event.getPlayer();
        OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
        Location loc = BlockToLocation(event.getBlock());

        if(player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            Plot plot = GetPlotbyLocation(loc);
            if (!plot.hasPerms(oPlayer.getID())) {
                event.getPlayer().sendActionBar(Component.text(ChatColor.RED + "This is a private plot, you do not have permission to build here."));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
        Player player = event.getPlayer();
        OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
        Location loc = event.getBlock().getLocation();

        if(player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            if (!HasPlotPermissions(oPlayer.getID(), loc)) {
                event.getPlayer().sendMessage(ChatColor.RED + "This is a private plot, you do not have permission here.");
                event.setCancelled(true);
            }
        }
    }
}
