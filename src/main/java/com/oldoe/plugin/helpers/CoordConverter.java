package com.oldoe.plugin.helpers;

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
}
