package com.oldoe.plugin.database;

import com.oldoe.plugin.Oldoe;
import org.bukkit.Location;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.oldoe.plugin.converters.CoordConverter.CoordToPlot;

public class PreparedQueries {

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

    public static Boolean IsPlotOwner(String uuid, Location loc) {

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

        if (owner == -1 && owner != userID) {
            return false;
        } else {
            return true;
        }
    }

    public static Boolean IsPlotPublic(Location loc) {

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

        if (owner == -1) {
            return true;
        } else {
            return false;
        }
    }

    public static void DeletePlot(Location loc) {
        int x = CoordToPlot(loc.getX());
        int z = CoordToPlot(loc.getZ());

        String sql = String.format("DELETE FROM `oldoe_plots` WHERE x = '%d' AND z = '%d'", x, z);
        Oldoe.GetDatabase().executeSQL(sql);
        Oldoe.GetDatabase().close();
    }

    public static BigDecimal GetCash(String uuid) {
        BigDecimal cash = new BigDecimal(0.00);

        try {
            String sql = String.format("SELECT `cash` FROM `oldoe_users` WHERE `uuid` = '%s'", uuid);
            ResultSet resultSet = Oldoe.GetDatabase().executeSQL(sql);

            if (resultSet != null) {
                while (resultSet.next()) {
                    cash = resultSet.getBigDecimal("cash");
                }
            }
        } catch (SQLException e) {
        }
        finally {
            Oldoe.GetDatabase().close();
        }

        return cash;
    }

    public static void UpdateMoney(String uuid, boolean add, BigDecimal amt) {

        String mathVal = "-";

        if (add) {
            mathVal = "+";
        }

        String sql = String.format(
                "UPDATE `oldoe_users` SET `cash` = `cash` %s %f WHERE `uuid` = '%s'",
                mathVal,
                amt.doubleValue(),
                uuid
        );
        Oldoe.GetDatabase().executeSQL(sql);
        Oldoe.GetDatabase().close();
    }
}
