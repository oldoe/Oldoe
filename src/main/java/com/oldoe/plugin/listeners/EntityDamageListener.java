package com.oldoe.plugin.listeners;

import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.models.Plot;
import com.oldoe.plugin.services.PlayerService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import static com.oldoe.plugin.database.PreparedQueries.GetPlotbyLocation;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (event.getCause().equals(EntityDamageEvent.DamageCause.STARVATION) && entity instanceof Player player) {
            if (player.getHealth() < 10.5) {
                event.setCancelled(true);
            }
        }

        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) && entity instanceof Item) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityD(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity entityDamager = event.getDamager();
        if (entity instanceof Enemy) {
            // For now all hostile mobs can be killed anywhere.
            return;
        }
        else {
            if (entity.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                if(entityDamager instanceof Player damager) {
                    OldoePlayer oDamager = PlayerService.GetPlayer(damager.getUniqueId());

                    // Player vs player damage
                    if (entity instanceof Player) {
                        OldoePlayer oPlayer = PlayerService.GetPlayer(entity.getUniqueId());
                        if (oDamager.isPvpEnabled() && oPlayer.isPvpEnabled()) {
                            return;
                        } else {
                            damager.sendMessage(ChatColor.RED + "Both players must have pvp turned on. Type /pvp to enable.");
                            event.setCancelled(true);
                            return;
                        }
                    }

                    Location loc = event.getEntity().getLocation();

                    Plot plot = GetPlotbyLocation(loc);
                    if (!plot.hasPerms(oDamager.getID())) {
                        damager.sendMessage(ChatColor.RED + "This is a private plot, you do not have permission here.");
                        event.setCancelled(true);
                    }
                }
                else if (entityDamager instanceof Projectile) {
                    ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
                    if (shooter instanceof Player damager) {
                        OldoePlayer oDamager = PlayerService.GetPlayer(damager.getUniqueId());

                        // Player vs player damage
                        if (entity instanceof Player) {
                            OldoePlayer oPlayer = PlayerService.GetPlayer(entity.getUniqueId());
                            if (oDamager.isPvpEnabled() && oPlayer.isPvpEnabled()) {
                                return;
                            } else {
                                damager.sendMessage(ChatColor.RED + "Both players must have pvp turned on. Type /pvp to enable.");
                                event.setCancelled(true);
                                return;
                            }
                        }

                        Location loc = event.getEntity().getLocation();

                        Plot plot = GetPlotbyLocation(loc);
                        if (!plot.hasPerms(oDamager.getID())) {
                            damager.sendMessage(ChatColor.RED + "This is a private plot, you do not have permission here.");
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

}
