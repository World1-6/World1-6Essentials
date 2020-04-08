package com.andrew121410.World16.Managers;

import com.andrew121410.CCUtils.storage.ISQL;
import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HomeManager {

    private Map<UUID, Map<String, Location>> rawHomesMap;

    private Main plugin;
    private ISQL isql;

    public HomeManager(Main plugin, ISQL isql) {
        this.plugin = plugin;
        this.isql = isql;

        this.rawHomesMap = this.plugin.getSetListMap().getHomesMap();

        isql.connect();
        isql.executeCommand("CREATE TABLE IF NOT EXISTS `Homes` (" +
                "`UUID` TEXT," +
                "`Date` TEXT," +
                "`PlayerName` TEXT," +
                "`HomeName` TEXT," +
                "`X` TEXT," +
                "`Y` TEXT," +
                "`Z` TEXT," +
                "`YAW` TEXT," +
                "`PITCH` TEXT," +
                "`World` TEXT" +
                ");");
        isql.disconnect();
    }

    public void getAllHomesFromISQL(ISQL isql, Player player) {
        this.fixMaps(player.getUniqueId(), false);

        isql.connect();

        ResultSet rs = isql.getResult("SELECT * FROM Homes WHERE (UUID='" + player.getUniqueId().toString() + "');");
        try {
            while (rs.next()) {
                String UUID = rs.getString("UUID");
                String Date = rs.getString("Date");
                String PlayerName = rs.getString("PlayerName");
                String HomeName = rs.getString("HomeName");
                String X = rs.getString("X");
                String Y = rs.getString("Y");
                String Z = rs.getString("Z");
                String YAW = rs.getString("YAW");
                String PITCH = rs.getString("PITCH");
                String World = rs.getString("World");

                rawHomesMap.get(player.getUniqueId()).put(HomeName, new Location(Bukkit.getServer().getWorld(World), Double.parseDouble(X), Double.parseDouble(Y), Double.parseDouble(Z), Float.parseFloat(YAW), Float.parseFloat(PITCH)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            isql.disconnect();
        }
    }

    public Location getHome(Player player, String HomeName) {
        this.fixMaps(player.getUniqueId(), true);

        return rawHomesMap.get(player.getUniqueId()).get(HomeName);
    }

    public Location getHome(UUID uuid, String HomeName) {
        this.fixMaps(uuid, true);

        return rawHomesMap.get(uuid).get(HomeName);
    }

    public void deleteHome(ISQL isql, Player player, String HomeName) {
        this.fixMaps(player.getUniqueId(), true);

        rawHomesMap.get(player.getUniqueId()).remove(HomeName.toLowerCase());

        deleteHomeFromISQL(isql, player, HomeName);
    }

    private void deleteHomeFromISQL(ISQL isql, Player player, String HomeName) {
        isql.connect();
        isql.executeCommand("DELETE FROM Homes WHERE UUID='" + player.getUniqueId() + "' AND HomeName='" + HomeName.toLowerCase() + "'");
        isql.disconnect();
    }

    public void deleteAllHomesFromISQL(ISQL isql, Player player) {
        isql.connect();
        isql.executeCommand("DELETE FROM Homes WHERE UUID='" + player.getUniqueId() + "'");
        isql.disconnect();
        rawHomesMap.remove(player.getUniqueId());
        player.kickPlayer("All homes has been cleared! Rejoin the server!");
    }

    public String listHomesInMap(Player player) {
        this.fixMaps(player.getUniqueId(), true);

        Set<String> homeSet = rawHomesMap.get(player.getUniqueId()).keySet();
        String[] homeString = homeSet.toArray(new String[0]);
        Arrays.sort(homeString);
        String str = String.join(", ", homeString);
        String homeListPrefix = "&6Homes:&r&7";
        return Translate.chat(homeListPrefix + " " + str);
    }

    public void setHome(ISQL isql, Player player, String HomeName) {
        this.fixMaps(player.getUniqueId(), true);

        rawHomesMap.get(player.getUniqueId()).put(HomeName.toLowerCase(), player.getLocation());

        setHomeToISQL(isql, player.getUniqueId(), player.getDisplayName(), HomeName, player.getLocation());
    }

    public void setHome(ISQL isql, UUID uuid, String PlayerName, String HomeName, Location location) {
        this.fixMaps(uuid, true);

        rawHomesMap.get(uuid).put(HomeName.toLowerCase(), location);

        setHomeToISQL(isql, uuid, PlayerName, HomeName, location);
    }

    private void setHomeToISQL(ISQL isql, UUID uuid, String PlayerName, String HomeName, Location location) {
        isql.connect();
        PreparedStatement preparedStatement = isql.executeCommandPreparedStatement("INSERT INTO Homes (UUID,Date,PlayerName,HomeName,X,Y,Z,YAW,PITCH,World) VALUES (?,?,?,?,?,?,?,?,?,?);");
        try {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, "0");
            preparedStatement.setString(3, PlayerName);
            preparedStatement.setString(4, HomeName.toLowerCase());
            preparedStatement.setString(5, String.valueOf(location.getX()));
            preparedStatement.setString(6, String.valueOf(location.getY()));
            preparedStatement.setString(7, String.valueOf(location.getZ()));
            preparedStatement.setString(8, String.valueOf(location.getYaw()));
            preparedStatement.setString(9, String.valueOf(location.getPitch()));
            preparedStatement.setString(10, location.getWorld().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            isql.disconnect();
        }

    }

    public void unloadPlayerHomes(Player player) {
        rawHomesMap.remove(player.getUniqueId());
    }

    private void fixMaps(UUID uuid, boolean getAllHomesFromISQL) {
        if (rawHomesMap.get(uuid) == null) {
            rawHomesMap.put(uuid, new HashMap<>());
            if (getAllHomesFromISQL) {
                Player player = Bukkit.getServer().getPlayer(uuid);
                if (player == null || !player.isOnline()) {
                    return;
                }
                this.getAllHomesFromISQL(isql, player);
            }
        }

    }
}