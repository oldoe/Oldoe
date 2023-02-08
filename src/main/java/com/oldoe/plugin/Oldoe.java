package com.oldoe.plugin;

import com.oldoe.plugin.commands.*;
import com.oldoe.plugin.database.MYSQLConnector;
import com.oldoe.plugin.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Oldoe extends JavaPlugin implements Listener {

    private static MYSQLConnector dbConnector = null;
    private static List<Material> softBlocks = null;
    private FileConfiguration config = getConfig();

    public static List<Material> GetSoftBlocks() {
        return softBlocks;
    }

    public static MYSQLConnector GetDatabase() {
        return dbConnector;
    }
    private static Oldoe instance;

    public static Oldoe getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        Bukkit.getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new EntitySpawningListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplosionListener(), this);
        getServer().getPluginManager().registerEvents(new EntityChangeBlockListener(), this);
        getServer().getPluginManager().registerEvents(new BlockFromToListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("sethome").setExecutor(new SetHomeCommand());
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("plot").setExecutor(new PlotCommand());
        this.getCommand("cash").setExecutor(new CashCommand());

        initConfig();

        this.dbConnector = new MYSQLConnector(this);

        this.softBlocks = GetSoftBlocksMaterials();
    }

    private List<Material> GetSoftBlocksMaterials() {
        List<Material> materials = new ArrayList<>();
        for (Material mat : Material.values()) {
            if (mat.isBlock() && mat.getHardness() < 0.2f) {
                materials.add(mat);
            }
        }
        return materials;
    }

    @Override
    public void onDisable() {
        // Close any existing mysql connections
        if (dbConnector != null) {
            dbConnector.closeConnection();
        }
    }

    private void initConfig() {
        config.addDefault("db_ip", "127.0.0.1");
        config.addDefault("db_name", "mcDatabase");
        config.addDefault("db_username", "db_Username");
        config.addDefault("db_password", "db_Password");
        config.options().copyDefaults(true);
        saveConfig();
    }
}
