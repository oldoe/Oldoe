package com.oldoe.plugin.helpers;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class CoordConverter {

    // Reason for a double is to obtain correct values for inputs such as -0.4 vs 0.4
    public static int CoordToPlot(double coord) {
        // turn double back to int and get abs value
        int val = Math.abs((int)coord);
        val = (int)Math.floor(val/128);
        val *= 128;
        val += 64;

        if (coord >= 0) {
            return val;
        } else {
            return -val;
        }
    }

    public static Location BlockToLocation(Block block) {

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        if (x < -1) {
            x += 1;
        }
        if (z < -1) {
            z += 1;
        }
        return new Location(block.getWorld(), x, y, z);
    }
}
