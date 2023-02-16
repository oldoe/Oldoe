package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

import static com.oldoe.plugin.database.PreparedQueries.HasPlotPermissions;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class PlayerInteractListener implements Listener {

    private List<Material> untouchables = Arrays.asList(Material.CHEST, Material.BARREL, Material.SHULKER_BOX, Material.HOPPER, Material.DISPENSER, Material.BREWING_STAND, Material.FURNACE, Material.BLAST_FURNACE, Material.ANVIL, Material.COMPARATOR, Material.REPEATER);

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        String uuid = event.getPlayer().getUniqueId().toString();

        Location interactPoint = event.getInteractionPoint();

        if (interactPoint == null) {
            return;
        }

        Location loc = BlockToLocation(interactPoint.getBlock());

        if (loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            if (!HasPlotPermissions(uuid, loc)) {
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && untouchables.contains(event.getClickedBlock().getType())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "This is a private plot, you do not have permission here.");
                    event.setCancelled(true);
                }
            }
        }
    }
}
