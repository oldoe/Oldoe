package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.services.DataService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        Location loc = BlockToLocation(event.getBlock());

        if (loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            if (!HasPlotPermissions(uuid, loc)) {
                event.getPlayer().sendMessage(ChatColor.RED + "This is a private plot, you do not have permission to build here.");
                event.setCancelled(true);
            }
        }

        if (!event.isCancelled()) {
            Material blockType = event.getBlock().getType();
            if (!Oldoe.GetSoftBlocks().contains(blockType)) {
                String sql = String.format(
                        "UPDATE `oldoe_users` SET `cash` = `cash` + 1 WHERE `uuid` = '%s'",
                        uuid
                );
                DataService.getDatabase().executeSQL(sql);
            }
        }
    }
}
