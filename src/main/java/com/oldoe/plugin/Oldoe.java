package com.oldoe.plugin;

import com.oldoe.plugin.services.ServiceManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Oldoe extends JavaPlugin implements Listener {

    private static List<Material> softBlocks = null;
    private static final List<Material> valuableBlocks = Arrays.asList(Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND_ORE, Material.ANCIENT_DEBRIS);
    private static final List<Material> storageBlocks = Arrays.asList(Material.CHEST, Material.FURNACE, Material.SHULKER_BOX, Material.BLAST_FURNACE, Material.BARREL);
    private static final List<Material> lockableMaterials = Arrays.asList(Material.ACACIA_TRAPDOOR, Material.BAMBOO_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.CHERRY_TRAPDOOR, Material.CRIMSON_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.MANGROVE_TRAPDOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.WARPED_TRAPDOOR,
            Material.LEVER);
    private FileConfiguration config = getConfig();

    public static final List<Material> GetSoftBlocks() {
        return softBlocks;
    }

    public static final List<Material> GetValuableBlocks() {
        return valuableBlocks;
    }

    public static final List<Material> GetLockableMaterials() {
        return lockableMaterials;
    }

    public static final List<Material> GetStorageBlocks() {
        return storageBlocks;
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

    private final List<Material> GetSoftBlocksMaterials() {
        List<Material> materials = new ArrayList<>();
        for (Material mat : Material.values()) {
            if (mat.isBlock() && mat.getHardness() < 0.2f) {
                materials.add(mat);
            }
        }
        return materials;
    }
}
