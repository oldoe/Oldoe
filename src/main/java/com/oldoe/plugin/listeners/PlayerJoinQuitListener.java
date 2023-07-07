package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.DataService;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        // Get the unique id of the player
        String uuid = player.getUniqueId().toString();
        int playerID = -1;


        Component intoMessage = Component.text("-------------------", NamedTextColor.WHITE).appendNewline()
                .append(Component.text("Welcome to Oldoe!", NamedTextColor.GOLD)).appendNewline()
                .append(Component.text("Website: ", NamedTextColor.GOLD))
                .append(Component.text("oldoe.com")).appendNewline()
                .append(Component.text("-------------------", NamedTextColor.WHITE)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://oldoe.com"));


        if ( DataService.getDatabase().getPlayerID(uuid) == -1) {

            if (!player.hasPlayedBefore()) {
                Bukkit.broadcast(Component.text(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " has joined for the first time."));
            }

            // Add the player to the players table if not already in it
            DataService.getDatabase().executeSQL(String.format("INSERT INTO `oldoe_users` (uuid, name) VALUES ('%s', '%s')", uuid, player.getName()));
            DataService.getDatabase().close();

            playerID = DataService.getDatabase().getPlayerID(uuid);

            if (!player.hasPlayedBefore()) {
                // Play a deep welcome sound.
                //player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 0.9f, 0.5f);
            }

        } else {

            playerID = DataService.getDatabase().getPlayerID(uuid);

            long timeMS = player.getWorld().getFullTime();
            long totalDays = timeMS / 24000;
            long year = totalDays / 365;
            long dayOfYear = totalDays - (year * 365);
        }

        player.sendMessage(intoMessage);

        //int id, String uuid, String displayName
        OldoePlayer oldoePlayer = new OldoePlayer(playerID, uuid, player.getName());

        List<String> staffList = Oldoe.getInstance().getConfig().getStringList("staff");

        if (staffList != null && staffList.contains(uuid)) {
            oldoePlayer.setStaff();
        }

        PlayerService.AddPlayer(oldoePlayer);

        ToggleHide(player, true);

        final Component footer = Component.text("play.olode.com", NamedTextColor.GOLD).append(Component.text(" | Website: ", NamedTextColor.WHITE)).append(Component.text("oldoe.com", NamedTextColor.GOLD));
        player.sendPlayerListFooter(footer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ToggleHide(player, false);
        OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
        PlayerService.RemovePlayer(oPlayer);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());
        PlayerService.RemovePlayer(oPlayer);
    }

    private void ToggleHide(Player player, boolean hide) {
        for (OldoePlayer oPlayer : PlayerService.GetPlayers()) {
            if (oPlayer.isHidden()) {
                Player hiddenPlayer = Bukkit.getPlayer(oPlayer.getName());

                if (hiddenPlayer == null) {
                    break;
                }

                if (hide) {
                    player.hidePlayer(Oldoe.getInstance(), hiddenPlayer);
                } else {
                    player.showPlayer(Oldoe.getInstance(), hiddenPlayer);
                }
            }
        }
    }
}
