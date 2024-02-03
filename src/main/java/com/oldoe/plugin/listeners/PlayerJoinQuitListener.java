package com.oldoe.plugin.listeners;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.DataService;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
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
                .append(Component.text("Welcome to ", NamedTextColor.GREEN))
                .append(Component.text("Oldoe", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("Website: ", NamedTextColor.GREEN))
                .append(Component.text("www.oldoe.com", NamedTextColor.WHITE)).appendNewline()
                .append(Component.text("-------------------", NamedTextColor.WHITE)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://oldoe.com"));


        if ( DataService.getDatabase().getPlayerID(uuid) == -1) {

            if (!player.hasPlayedBefore()) {
                Bukkit.broadcast(Component.text(ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " has joined for the first time!"));
            }

            // Add the player to the players table if not already in it
            DataService.getDatabase().executeSQL(String.format("INSERT INTO `oldoe_users` (uuid, name) VALUES ('%s', '%s')", uuid, player.getName()));
            DataService.getDatabase().close();

            playerID = DataService.getDatabase().getPlayerID(uuid);

        } else {

            playerID = DataService.getDatabase().getPlayerID(uuid);

            // Update DB with the users name (For instances where it changes) TODO update last_seen date on login/logout
            DataService.getDatabase().UpdatePlayerName(uuid, player.getName());

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

        final Component footer = Component.text("play.oldoe.com", NamedTextColor.GREEN).append(Component.text(" | ", NamedTextColor.WHITE)).append(Component.text("www.oldoe.com", NamedTextColor.GREEN));
        player.sendPlayerListFooter(footer);

        /*
        // Display client info to staff
        for( Player p : Bukkit.getOnlinePlayers()) {
            OldoePlayer oP = PlayerService.GetPlayer(p.getUniqueId());
            if (oP.isStaff()) {
                p.sendMessage(Component.text("[INFO] " + player.getName() + " - Client: " + Objects.toString(player.getClientBrandName(), "Modded (Fabric/Forge)"), NamedTextColor.GRAY));
            }
        }
         */
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
