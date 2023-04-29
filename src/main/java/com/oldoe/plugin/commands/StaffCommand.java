package com.oldoe.plugin.commands;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.models.OldoePlayer;
import com.oldoe.plugin.services.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.oldoe.plugin.database.PreparedQueries.GetPlayerHome;

public class StaffCommand implements CommandExecutor, TabCompleter, Listener {

    private Set<Player> hiddenPlayers = new HashSet<>();
    private Scoreboard scoreboard;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            OldoePlayer oPlayer = PlayerService.GetPlayer(player.getUniqueId());

            if (!oPlayer.isStaff()) {
                player.sendMessage(Component.text(ChatColor.RED + "Only staff can use this command!"));
                return false;
            }

            if (args.length <=0) {

                if (oPlayer.isStaffEnabled()) {
                    ToggleStaffMode(player,oPlayer, false);
                } else {
                    ToggleStaffMode(player,oPlayer, true);
                }

            } else {

                if (args.length > 1) {

                    Player targetP = GetPlayer(args[1], player);

                    switch (args[0].toLowerCase()) {
                        case ("end"):
                            if (targetP != null) {
                                player.openInventory(targetP.getEnderChest());
                            }
                            break;
                        case ("open"):
                            if (targetP != null) {
                                player.openInventory(targetP.getInventory());
                            }
                            break;
                        case ("home"):
                            if (targetP != null) {
                                String uuid = targetP.getUniqueId().toString();
                                Location home = GetPlayerHome(uuid);

                                if (home != null) {
                                    if (!oPlayer.isStaffEnabled()) {
                                        ToggleStaffMode(player,oPlayer, true);
                                    }
                                    player.teleport(home);
                                }
                            }
                            break;
                    }

                } else {
                    Player targetP = Bukkit.getPlayer(args[0]);
                    if (targetP == null) {
                        player.sendMessage(Component.text(ChatColor.RED + "Player is not online!"));
                    } else {
                        if (!oPlayer.isStaffEnabled()) {
                            ToggleStaffMode(player,oPlayer, true);
                        }
                        player.setSpectatorTarget(targetP);
                    }
                }
            }

        }

        return true;
    }

    private Player GetPlayer(String searchPlayer, Player sender) {
        Player targetP = Bukkit.getPlayer(searchPlayer);
        if (targetP == null) {
            sender.sendMessage(Component.text(ChatColor.RED + searchPlayer + " is not online!"));
        }
        return targetP;
    }

    private void ToggleStaffMode(Player player, OldoePlayer oPlayer, boolean enable) {

        if (enable) {
            oPlayer.setStaffEnabled(true);
            oPlayer.setLastStaffLocation(player.getLocation());
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(Component.text(ChatColor.GREEN + "Staff Mode Enabled."));
            player.setSleepingIgnored(true);
            player.setInvisible(true);
            hiddenPlayers.add(player);
            updateTabList();
        } else {
            oPlayer.setStaffEnabled(false);
            player.teleport(oPlayer.getStaffStartLocation());
            player.sendMessage(Component.text(ChatColor.GREEN + "Staff Mode Disabled."));
            player.setGameMode(GameMode.SURVIVAL);
            player.setSleepingIgnored(false);
            player.setInvisible(false);
            hiddenPlayers.remove(player);
            updateTabList();
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            players.add(p.getName());
        }

        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("end");
            arguments.add("open");
            arguments.add("home");
            arguments.addAll(players);
            return arguments;
        }

        if (args.length == 2) {
            return players;
        }

        return null;
    }

    public void HideSpectatorTabList() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("tab", "dummy");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, Oldoe.getInstance());
    }

    private void updateTabList() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player hiddenPlayer : hiddenPlayers) {
                Objects.requireNonNull(scoreboard.getTeam(player.getName())).removeEntry(hiddenPlayer.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        scoreboard.registerNewTeam(player.getName()).addEntry(player.getName());
        updateTabList();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        scoreboard.getTeam(player.getName()).unregister();
        hiddenPlayers.remove(player);
        updateTabList();
    }

}
