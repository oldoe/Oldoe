package com.oldoe.plugin;

import com.oldoe.plugin.commands.StaffCommand;
import com.oldoe.plugin.services.ServiceManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class Oldoe extends JavaPlugin implements Listener {

    private static List<Material> softBlocks = null;
    private FileConfiguration config = getConfig();

    public static List<Material> GetSoftBlocks() {
        return softBlocks;
    }

    private static ServiceManager serviceManager = new ServiceManager();

    public static ServiceManager GetServiceManager() { return serviceManager; }

    public static BukkitScheduler GetScheduler() { return instance.getServer().getScheduler(); }

    private static Oldoe instance;

    public static Oldoe getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        initConfig();

        GetServiceManager().Register(instance);

        this.softBlocks = GetSoftBlocksMaterials();
        StaffCommand staffCommand = new StaffCommand();
        staffCommand.HideSpectatorTabList();
    }

    @Override
    public void onDisable() {
        GetServiceManager().UnRegister();
    }

    private void initConfig() {
        config.addDefault("db_ip", "127.0.0.1");
        config.addDefault("db_name", "mcDatabase");
        config.addDefault("db_username", "db_Username");
        config.addDefault("db_password", "db_Password");
        config.addDefault("staff", "");
        config.options().copyDefaults(true);
        saveConfig();
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
}
