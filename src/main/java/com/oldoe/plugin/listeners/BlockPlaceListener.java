package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.models.Plot;
import com.oldoe.plugin.services.DataService;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.oldoe.plugin.database.PreparedQueries.GetPlotbyLocation;
import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        OldoePlayer oPlayer = PlayerService.GetPlayer(event.getPlayer().getUniqueId());
        Location loc = BlockToLocation(event.getBlock());

        if (loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            Plot plot = GetPlotbyLocation(loc);
            if (!plot.hasPerms(oPlayer.getID())) {
                event.getPlayer().sendActionBar(Component.text(ChatColor.RED + "This is a private plot, you do not have permission to build here."));
                event.setCancelled(true);
            }
        }

        if (!event.isCancelled()) {
            Material blockType = event.getBlock().getType();
            if (!Oldoe.GetSoftBlocks().contains(blockType)) {
                String sql = String.format(
                        "UPDATE `oldoe_users` SET `cash` = `cash` + 1 WHERE `id` = '%s'",
                        oPlayer.getID()
                );

                Oldoe.GetScheduler().runTaskAsynchronously(Oldoe.getInstance(), () -> {
                    DataService.getDatabase().executeSQL(sql);
                    DataService.getDatabase().close();
                });
            }
        }
    }
}
