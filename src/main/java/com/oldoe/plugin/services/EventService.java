package com.oldoe.plugin.services;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EventService {

    public void registerEvents(JavaPlugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();

        Bukkit.getPluginManager().registerEvents(Oldoe.getInstance(), instance);
        pm.registerEvents(new EntitySpawningListener(), instance);
        pm.registerEvents(new EntityExplosionListener(), instance);
        pm.registerEvents(new EntityChangeBlockListener(), instance);
        pm.registerEvents(new BlockFromToListener(), instance);
        pm.registerEvents(new PlayerJoinQuitListener(), instance);
        pm.registerEvents(new BlockBreakListener(), instance);
        pm.registerEvents(new BlockPlaceListener(), instance);
        pm.registerEvents(new PlayerInteractListener(), instance);
        pm.registerEvents(new PlayerDeathListener(), instance);
        pm.registerEvents(new PlayerChatListener(), instance);
        pm.registerEvents(new EntityDamageListener(), instance);
        pm.registerEvents(new PlayerMoveListener(), instance);
        pm.registerEvents(new BucketListeners(), instance);
        pm.registerEvents(new PlayerSpawnLocationListener(), instance);
        pm.registerEvents(new PlayerGameModeChangeListener(), instance);
    }
}
