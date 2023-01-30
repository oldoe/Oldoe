package com.oldoe.plugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockFromToListener  implements Listener {

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent event) {
        List<Material> unbreakables = Arrays.asList(Material.TORCH, Material.REDSTONE, Material.REDSTONE_WIRE, Material.REDSTONE_TORCH, Material.REDSTONE_WALL_TORCH);

        Material blockType = event.getToBlock().getType();
        if (unbreakables.contains(blockType)) {
            event.setCancelled(true);
        }

    }
}
