package com.oldoe.plugin.database;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.services.DataService;
import org.bukkit.Location;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.oldoe.plugin.helpers.CoordConverter.CoordToPlot;

public class PreparedQueries {

    public static int GetPlayerIdByName(String name) {
        int userID = -1;
        try {
            String sql = String.format("SELECT `id` FROM `oldoe_users` WHERE `name` LIKE '%s'", name);
            ResultSet resultSet = DataService.getDatabase().executeSQL(sql);

            if (resultSet != null) {
                while (resultSet.next()) {
                    userID = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        }

        return userID;
    }

    public static Boolean HasPlotPermissions(String uuid, Location loc) {

        int userID = DataService.getDatabase().getPlayerID(uuid);

        int x = CoordToPlot(loc.getX());
        int z = CoordToPlot(loc.getZ());

        int owner = -1;
        int plotID = -1;

        try {
            String sql = String.format(
                    "SELECT `owner`, `id` FROM `oldoe_plots` WHERE x = '%d' AND z = '%d'",
                    x, z
            );
            ResultSet rs = DataService.getDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    owner = rs.getInt("owner");
                    plotID = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            DataService.getDatabase().close();
        }

        Boolean isPlotMember = IsPlotMember(plotID, userID);
        if (owner != -1 && owner != userID && !isPlotMember) {
            return false;
        } else {
            return true;
        }
    }

    public static int GetPlotID(Location loc) {
        int x = CoordToPlot(loc.getX());
        int z = CoordToPlot(loc.getZ());

        int id = -1;

        try {
            String sql = String.format(
                    "SELECT `id` FROM `oldoe_plots` WHERE x = '%d' AND z = '%d'",
                    x, z
            );
            ResultSet rs = DataService.getDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            DataService.getDatabase().close();
        }
        return id;
    }

    public static Boolean IsPlotOwner(String uuid, Location loc) {

        int userID =  DataService.getDatabase().getPlayerID(uuid);

        int x = CoordToPlot(loc.getBlockX());
        int z = CoordToPlot(loc.getBlockZ());

        int owner = -1;

        try {
            String sql = String.format(
                    "SELECT `owner` FROM `oldoe_plots` WHERE x = '%d' AND z = '%d'",
                    x, z
            );
            ResultSet rs = DataService.getDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    owner = rs.getInt("owner");
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            DataService.getDatabase().close();
        }

        if (owner == -1 || owner != userID) {
            return false;
        } else {
            return true;
        }
    }

    public static Boolean IsPlotMember(int plotID, int userID) {

        int id = -1;

        try {
            String sql = String.format("SELECT * FROM `oldoe_plot_perms` WHERE `plot` = '%d' AND `user` = '%d'", plotID, userID);
            ResultSet rs = DataService.getDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    id = rs.getInt("id");
                }
            }
            DataService.getDatabase().close();
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            DataService.getDatabase().close();
        }

        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public static List<String> GetPlotMembers(int plotID) {
        List<String> members = new ArrayList<>();

        try {
            String sql = String.format(
                    "SELECT sh.name FROM `oldoe_users` sh JOIN `oldoe_plot_perms` su ON sh.id = su.user WHERE su.plot= '%d'", plotID);
            ResultSet rs = DataService.getDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    members.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            DataService.getDatabase().close();
        }
        return members;
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
            ResultSet rs = DataService.getDatabase().executeSQL(sql);
            if (rs != null) {
                while (rs.next()) {
                    owner = rs.getInt("owner");
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        } finally {
            DataService.getDatabase().close();
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
        DataService.getDatabase().executeSQL(sql);
        DataService.getDatabase().close();
    }

    public static BigDecimal GetCash(String uuid) {
        BigDecimal cash = new BigDecimal(0.00);

        try {
            String sql = String.format("SELECT `cash` FROM `oldoe_users` WHERE `uuid` = '%s'", uuid);
            ResultSet resultSet = DataService.getDatabase().executeSQL(sql);

            if (resultSet != null) {
                while (resultSet.next()) {
                    cash = resultSet.getBigDecimal("cash");
                }
            }
        } catch (SQLException e) {
            Oldoe.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        }
        finally {
            DataService.getDatabase().close();
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
        DataService.getDatabase().executeSQL(sql);
        DataService.getDatabase().close();
    }
}
