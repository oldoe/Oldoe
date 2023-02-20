package com.oldoe.plugin.services;

import com.oldoe.plugin.models.OldoePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerService {

    private static List<OldoePlayer> players = new ArrayList<>();

    public static List<OldoePlayer> GetPlayers() {
        return players;
    }

    public static void AddPlayer(OldoePlayer player) {
        players.add(player);
    }

    public static void RemovePlayer(OldoePlayer player) {
        players.remove(player);
    }

    public static OldoePlayer GetPlayer(int id) {
        return players.stream().filter(player -> player.getID() == id).findFirst().orElse(null);
    }

    public static OldoePlayer GetPlayer(String uuid) {
        return players.stream().filter(u -> u.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    public static OldoePlayer GetPlayer(UUID uuid) {
        return players.stream().filter(u -> u.getUUID().equals(uuid.toString())).findFirst().orElse(null);
    }

    public static OldoePlayer GetPlayerByName(String name) {
        return players.stream().filter(player -> player.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
