package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.models.Plot;
import com.oldoe.plugin.models.UUIDDataType;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.oldoe.plugin.database.PreparedQueries.GetPlotbyLocation;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class PlayerInteractListener implements Listener {

    private List<Material> untouchables = Arrays.asList(Material.CHEST, Material.BARREL, Material.SHULKER_BOX, Material.HOPPER, Material.DISPENSER, Material.BREWING_STAND, Material.FURNACE, Material.BLAST_FURNACE, Material.ANVIL, Material.COMPARATOR, Material.REPEATER);

    @EventHandler
    public void onInteract(PlayerInteractEvent event){

        Location interactPoint = event.getInteractionPoint();

        if (interactPoint == null) {
            return;
        }

        OldoePlayer oPlayer = PlayerService.GetPlayer(event.getPlayer().getUniqueId());

        Location loc = BlockToLocation(interactPoint.getBlock());



        // Staff Mode Info
        if (oPlayer.isStaffEnabled()) {
            // Get owner and created timestamp on storage blocks
            if (Oldoe.GetStorageBlocks().contains(event.getClickedBlock().getType()) && event.getClickedBlock().getState() instanceof TileState tState) {
                UUID pId = tState.getPersistentDataContainer().get(NamespacedKey.fromString("owner"), new UUIDDataType());
                if (pId != null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(pId);
                    String createdDate = tState.getPersistentDataContainer().get(NamespacedKey.fromString("created"), PersistentDataType.STRING);

                    Instant createdInstant = Instant.parse(createdDate);
                    Duration duration = Duration.between(createdInstant, Instant.now());


                    Component comp = Component.text("--- X: " + event.getClickedBlock().getX() + ", Y: " + event.getClickedBlock().getY() + ", Z: " + event.getClickedBlock().getZ() + " ---", NamedTextColor.WHITE).appendNewline()
                            .append(Component.text("Type: ", NamedTextColor.DARK_GREEN)).append(Component.text(event.getClickedBlock().getType().name().toLowerCase())).appendNewline()
                            .append(Component.text("Owner: ", NamedTextColor.DARK_GREEN).append(Component.text(offlinePlayer.getName(), NamedTextColor.WHITE)).appendNewline()
                            .append(Component.text("Created: ", NamedTextColor.DARK_GREEN)).append(Component.text(duration.toDays() + " days " + duration.toHours() + " hours " + duration.toMinutes() + " minutes ago", NamedTextColor.WHITE)));

                    event.getPlayer().sendMessage(comp);

                }
            }
        }

        if (loc.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            Plot plot = GetPlotbyLocation(loc);
            if (!plot.hasPerms(oPlayer.getID())) {
                // If they don't have permission, but they are in staff mode.
                if (oPlayer.isStaffEnabled()) {
                    return;
                }

                if (event.getClickedBlock().getType().equals( Material.FARMLAND)) {
                    event.getPlayer().sendActionBar(Component.text(ChatColor.RED + "This is a private plot, you do not have permission here."));
                    event.setCancelled(true);
                }

                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && untouchables.contains(event.getClickedBlock().getType())) {
                    event.getPlayer().sendActionBar(Component.text(ChatColor.RED + "This is a private plot, you do not have permission here."));
                    event.setCancelled(true);
                }
            }
        }
    }
}
