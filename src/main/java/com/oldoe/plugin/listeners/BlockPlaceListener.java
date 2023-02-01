package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        Location loc = event.getBlock().getLocation();

        if (!HasPlotPermissions(uuid, loc)) {
            event.getPlayer().sendMessage(ChatColor.RED + "This is a private plot, you do not have permission to build here.");
            event.setCancelled(true);
        }
        else {
            String sql = String.format(
                    "UPDATE `oldoe_users` SET `cash` = `cash` + 1 WHERE `uuid` = '%s'",
                    uuid
            );
            Oldoe.GetDatabase().executeSQL(sql);
            Oldoe.GetDatabase().close();
        }
    }
}
