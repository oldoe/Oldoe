package com.oldoe.plugin;

import com.oldoe.plugin.commands.HomeCommand;
import com.oldoe.plugin.commands.SetHomeCommand;
import com.oldoe.plugin.commands.SpawnCommand;
import com.oldoe.plugin.database.MYSQLConnector;
import com.oldoe.plugin.listeners.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Oldoe extends JavaPlugin implements Listener {

    private static MYSQLConnector dbConnector = null;
    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new EntitySpawningListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplosionListener(), this);
        getServer().getPluginManager().registerEvents(new EntityChangeBlockListener(), this);
        getServer().getPluginManager().registerEvents(new BlockFromToListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("sethome").setExecutor(new SetHomeCommand());
        this.getCommand("home").setExecutor(new HomeCommand());

        initConfig();

        this.dbConnector = new MYSQLConnector(this);
    }

    private void initConfig() {
        config.addDefault("db_ip", "127.0.0.1");
        config.addDefault("db_name", "mcDatabase");
        config.addDefault("db_username", "db_Username");
        config.addDefault("db_password", "db_Password");
        config.options().copyDefaults(true);
        saveConfig();
    }

    public static MYSQLConnector GetDatabase() {
        return dbConnector;
    }

    @Override
    public void onDisable() {
        // Close any existing mysql connections
        if (dbConnector != null) {
            dbConnector.closeConnection();
        }
    }

}
