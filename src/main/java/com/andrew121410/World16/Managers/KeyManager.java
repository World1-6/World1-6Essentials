package com.andrew121410.World16.Managers;

import com.andrew121410.CCUtils.storage.ISQL;
import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Objects.KeyObject;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

/**
 * The key API for the /key
 *
 * @author Andrew121410
 * @version 2
 * @since 2/4/2019
 */

public class KeyManager {

    private Main plugin;

    //Maps
    private Map<String, KeyObject> keyDatam;
    //...

    public KeyManager(Main plugin, ISQL isql) {
        this.plugin = plugin;
        this.keyDatam = this.plugin.getSetListMap().getKeyDataM();

        isql.connect();
        isql.executeCommand("CREATE TABLE IF NOT EXISTS `KeyData` (" +
                "`KeyDataID` INT," +
                "`Player` TEXT," +
                "`Lore` TEXT" +
                ");");
        isql.disconnect();
    }

    /**
     * Get's all of the key's for the Player from ISQL
     *
     * @param playerName Player name string
     * @param mysql      mysql object
     */
    public void getAllKeysISQL(String playerName, ISQL mysql) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                ResultSet rs = mysql.getResult("SELECT * FROM KeyData WHERE (Player='" + playerName + "')");
                try {
                    while (rs.next()) {
                        String KeyID = rs.getString("KeyDataID");
                        String PlayerData = rs.getString("Player");
                        String LoreData = rs.getString("Lore");
                        if (keyDatam.get(playerName) != null) {
                            keyDatam.get(playerName).setKey(Integer.valueOf(KeyID), LoreData);
                        }
                    }
                } catch (SQLException e) {
                    mysql.disconnect();
                    e.printStackTrace();
                } finally {
                    mysql.disconnect();
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

    /**
     * Gets the key from the ram and it gives it too the player
     *
     * @param p         Player
     * @param keyDataID key ID
     */
    public void giveKey(Player p, int keyDataID) { //GETS THE 1 KEY FROM THE MEMORY.
        Material material = Material.TRIPWIRE_HOOK;
        int amount = 1;
        String name = "Key";

        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Collections.singletonList(keyDatam.get(p.getDisplayName()).getKey(keyDataID)));
        item.setItemMeta(itemMeta);

        p.getInventory().addItem(item);
        p.sendMessage(Translate.chat("&6[Keys] &aDone!"));
    }

    //1
    public void ResetEverythingFromPlayerMySQL(ISQL mysql, Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("DELETE FROM KeyData WHERE Player='" + p.getDisplayName() + "'");
                mysql.disconnect();
            }
        }.runTaskAsynchronously(plugin);
    }

    //2
    public void ResetEverythingFromPlayerMySQL(ISQL mysql, String p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("DELETE FROM KeyData WHERE Player='" + p + "'");
                mysql.disconnect();
            }
        }.runTaskAsynchronously(plugin);
    }

    //1
    public void DeleteKeyDataID(ISQL mysql, Player p, int INT) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("DELETE FROM KeyData WHERE KeyDataID='" + INT + "' AND Player='" + p.getDisplayName() + "'");
                mysql.disconnect();
            }
        }.runTaskAsynchronously(plugin);
    }

    //2
    public void DeleteKeyDataID(ISQL mysql, String p, int INT) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("DELETE FROM KeyData WHERE KeyDataID='" + INT + "' AND Player='" + p + "'");
                mysql.disconnect();
            }
        }.runTaskAsynchronously(plugin);
    }

    //1
    public void SetKey(ISQL mysql, int KeyDataID, Player p, String Lore) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                try {
                    PreparedStatement preparedStatement = mysql.executeCommandPreparedStatement("INSERT INTO KeyData (KeyDataID, Player, Lore) VALUES (?,?,?);");
                    preparedStatement.setInt(1, KeyDataID);
                    preparedStatement.setString(2, p.getDisplayName());
                    preparedStatement.setString(3, Lore);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    mysql.disconnect();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    //2
    public void SetKey(ISQL mysql, int KeyDataID, String p, String Lore) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                try {
                    PreparedStatement preparedStatement = mysql.executeCommandPreparedStatement("INSERT INTO KeyData (KeyDataID, Player, Lore) VALUES (?,?,?);");
                    preparedStatement.setInt(1, KeyDataID);
                    preparedStatement.setString(2, p);
                    preparedStatement.setString(3, Lore);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    mysql.disconnect();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    //1
    public void SetKeyAndDeleteOldKey(ISQL mysql, int KeyDataID, Player p, String Lore) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("DELETE FROM KeyData WHERE KeyDataID='" + KeyDataID + "' AND Player='" + p.getDisplayName() + "'");
                try {
                    PreparedStatement preparedStatement = mysql.executeCommandPreparedStatement("INSERT INTO KeyData (KeyDataID, PLayer, Lore) VALUES (?,?,?);");
                    preparedStatement.setInt(1, KeyDataID);
                    preparedStatement.setString(2, p.getDisplayName());
                    preparedStatement.setString(3, Lore);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    mysql.disconnect();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    //2
    public void SetKeyAndDeleteOldKey(ISQL mysql, int KeyDataID, String p, String Lore) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("DELETE FROM KeyData WHERE KeyDataID='" + KeyDataID + "' AND Player='" + p + "'");
                try {
                    PreparedStatement preparedStatement = mysql.executeCommandPreparedStatement("INSERT INTO KeyData (KeyDataID, PLayer, Lore) VALUES (?,?,?);");
                    preparedStatement.setInt(1, KeyDataID);
                    preparedStatement.setString(2, p);
                    preparedStatement.setString(3, Lore);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    mysql.disconnect();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    //3
    public void SetKeyAndDeleteOldKey(ISQL mysql, int KeyDataID, String p, String Lore, boolean autoConnectAndAutoDisconnect) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (autoConnectAndAutoDisconnect) {
                    mysql.connect();
                }
                mysql.executeCommand("DELETE FROM KeyData WHERE KeyDataID='" + KeyDataID + "' AND Player='" + p + "'");
                try {
                    PreparedStatement preparedStatement = mysql.executeCommandPreparedStatement("INSERT INTO KeyData (KeyDataID, PLayer, Lore) VALUES (?,?,?);");
                    preparedStatement.setInt(1, KeyDataID);
                    preparedStatement.setString(2, p);
                    preparedStatement.setString(3, Lore);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    mysql.disconnect();
                    if (autoConnectAndAutoDisconnect) {
                        mysql.disconnect();
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    //1
    public void ReplaceKey(ISQL mysql, int KeyDataID, Player p, String Lore) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("UPDATE KeyData SET Lore = '" + Lore + "' WHERE KeyDataID = " + KeyDataID + " AND Player = '" + p.getDisplayName() + "'");
                mysql.disconnect();
            }
        }.runTaskAsynchronously(plugin);
    }

    //2
    public void ReplaceKey(ISQL mysql, int KeyDataID, String p, String Lore) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.connect();
                mysql.executeCommand("UPDATE KeyData SET Lore = '" + Lore + "' WHERE KeyDataID = " + KeyDataID + " AND Player = '" + p + "'");
                mysql.disconnect();
            }
        }.runTaskAsynchronously(plugin);
    }
}