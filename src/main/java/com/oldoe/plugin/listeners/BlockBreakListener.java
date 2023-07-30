package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.models.Plot;
import com.oldoe.plugin.services.DataService;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static com.oldoe.plugin.database.PreparedQueries.GetPlotbyLocation;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
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

            if (Oldoe.GetValuableBlocks().contains(blockType)) {
                for( Player p : Bukkit.getOnlinePlayers()) {
                    OldoePlayer oP = PlayerService.GetPlayer(p.getUniqueId());
                    if (oP.isStaff()) {
                        p.sendMessage(Component.text("[INFO] ", NamedTextColor.GRAY).append(Component.text(event.getPlayer().getName() + " mined " + blockType.name() + " at " + event.getBlock().getWorld().getName() + " (" + event.getBlock().getX() + ", " + event.getBlock().getY() + ", " + event.getBlock().getZ() + ")")).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/staff " + event.getPlayer().getName())));
                    }
                }
            }
        }
    }
}
