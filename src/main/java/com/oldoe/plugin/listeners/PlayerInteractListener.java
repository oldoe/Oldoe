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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.oldoe.plugin.database.PreparedQueries.GetPlotbyLocation;
import static com.oldoe.plugin.helpers.CoordConverter.BlockToLocation;

public class PlayerInteractListener implements Listener {

    private List<Material> untouchables = Arrays.asList(Material.CHEST, Material.BARREL, Material.SHULKER_BOX, Material.HOPPER, Material.DISPENSER, Material.DROPPER, Material.BREWING_STAND, Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER, Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL, Material.COMPARATOR, Material.REPEATER, Material.JUKEBOX,
            Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX,
            Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX);

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
                    return;
                }

                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Oldoe.GetLockableMaterials().contains(event.getClickedBlock().getType())) {
                    if (event.getClickedBlock().getState() instanceof TileState tState) {
                        boolean isLocked = tState.getPersistentDataContainer().get(NamespacedKey.fromString("islocked"), PersistentDataType.BOOLEAN);

                        if (isLocked) {
                            event.getPlayer().sendActionBar(Component.text(ChatColor.RED + "Item is locked. This is a private plot, you do not have permission here."));
                            event.setCancelled(true);
                        }
                    }
                }

            }
            else {
                /*
                if (oPlayer.isLockMode() && !plot.isPublic()) {
                    event.getPlayer().sendMessage(Component.text("1", NamedTextColor.GREEN));
                    if (Oldoe.GetLockableMaterials().contains(event.getClickedBlock().getType())) {
                        event.getPlayer().sendMessage(Component.text(event.getClickedBlock().getState().getType().name(), NamedTextColor.GREEN));
                        // Player has permission to the plot and it is a private plot
                        BlockState bState = event.getClickedBlock().getState();
                            event.getPlayer().sendMessage(Component.text("3", NamedTextColor.GREEN));
                            boolean isLocked = bState.get.getPersistentDataContainer().get(NamespacedKey.fromString("islocked"), PersistentDataType.BOOLEAN);
                                if (isLocked) {
                                    tState.getPersistentDataContainer().set(NamespacedKey.fromString("islocked"), PersistentDataType.BOOLEAN, false);
                                    event.getPlayer().sendMessage(Component.text(event.getClickedBlock().getType().name().toLowerCase() + " Unlocked", NamedTextColor.GREEN));
                                } else {
                                    tState.getPersistentDataContainer().set(NamespacedKey.fromString("islocked"), PersistentDataType.BOOLEAN, true);
                                    event.getPlayer().sendMessage(Component.text(event.getClickedBlock().getType().name().toLowerCase() + " Locked", NamedTextColor.RED));
                                }

                            tState.update();

                    }
                }

                 */
            }
        }
    }
}
