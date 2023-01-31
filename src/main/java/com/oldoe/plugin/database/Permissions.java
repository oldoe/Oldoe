package com.oldoe.plugin.database;

import com.oldoe.plugin.Oldoe;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.oldoe.plugin.converters.CoordConverter.CoordToPlot;

public class Permissions {

    public static Boolean HasPlotPermissions(String uuid, Location loc) {

        int userID =  Oldoe.GetDatabase().getPlayerID(uuid);

        int x = CoordToPlot(loc.getX());
        int z = CoordToPlot(loc.getZ());

        int owner = -1;

        try {
            String sql = String.format(
                    "SELECT `owner` FROM `oldoe_plots` WHERE x = '%d' AND z = '%d'",
                    x, z
            );
            ResultSet rs = Oldoe.GetDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    owner = rs.getInt("owner");
                }
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        } finally {
            Oldoe.GetDatabase().close();
        }

        // TODO also check for plot members, can be part of the query above
        if (owner != -1 && owner != userID) {
            return false;
        } else {
            return true;
        }
    }
}
