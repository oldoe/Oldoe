package com.oldoe.plugin.listeners;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.models.Plot;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;


import static com.oldoe.plugin.database.PreparedQueries.GetPlotbyLocation;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class SignChangeListener  implements Listener {

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        Location interactPoint = event.getBlock().getLocation();

        if (interactPoint == null) {
            return;
        }

        OldoePlayer oPlayer = PlayerService.GetPlayer(event.getPlayer().getUniqueId());

        Location loc = BlockToLocation(interactPoint.getBlock());

        if (loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            Plot plot = GetPlotbyLocation(loc);
            if (!plot.hasPerms(oPlayer.getID())) {
                // If they don't have permission, but they are in staff mode.
                if (oPlayer.isStaffEnabled()) {
                    return;
                }
                event.getPlayer().sendActionBar(Component.text(ChatColor.RED + "This is a private plot, you do not have permission here."));
                event.setCancelled(true);
            }
        }
    }
}
