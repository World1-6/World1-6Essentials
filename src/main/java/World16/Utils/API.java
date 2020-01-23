package World16.Utils;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The Bass API for World1-6Ess
 *
 * @author Andrew121410
 */

public class API {

    // Maps
    private Map<String, UUID> uuidCache;
    private Map<UUID, Location> afkMap;
    //...

    // Lists
    private List<String> Fly1;
    private List<String> GodM;
    //...

    private Main plugin;

    //Finals
    public static final String CUSTOM_COMMAND_FORMAT = "`";
    public static final String DATE_OF_VERSION = "1/23/2020";
    public static final String PREFIX = "[&9World1-6Ess&r]";
    public static final String USELESS_TAG = PREFIX + "->[&bUSELESS&r]";
    public static final String DEBUG_TAG = PREFIX + "->[&eDEBUG&r]";
    public static final String EMERGENCY_TAG = PREFIX + "->&c[EMERGENCY]&r";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong.";
    //...

    // FOR MYSQL
    private String mysql_HOST;
    private String mysql_DATABASE;
    private String mysql_USER;
    private String mysql_PASSWORD;
    private String mysql_PORT;
    // END MYSQL

    public API(Main plugin) {
        this.plugin = plugin;
        doSetListMap();
        setMySQL();
    }

    private void doSetListMap() {
        this.uuidCache = this.plugin.getSetListMap().getUuidCache();
        this.afkMap = this.plugin.getSetListMap().getAfkMap();
        this.Fly1 = this.plugin.getSetListMap().getFlyList();
        this.GodM = this.plugin.getSetListMap().getAdminList();
    }

    // START OF MYSQL

    private void setMySQL() {
        mysql_HOST = plugin.getConfig().getString("MysqlHOST");
        mysql_DATABASE = plugin.getConfig().getString("MysqlDATABASE");
        mysql_USER = plugin.getConfig().getString("MysqlUSER");
        mysql_PASSWORD = plugin.getConfig().getString("MysqlPASSWORD");
        mysql_PORT = plugin.getConfig().getString("MysqlPORT");
    }

    public String getMysql_HOST() {
        if (this.mysql_HOST != null) {
            return this.mysql_HOST;
        } else {
            return null;
        }
    }

    public String getMysql_DATABASE() {
        if (this.mysql_DATABASE != null) {
            return this.mysql_DATABASE;
        } else {
            return null;
        }
    }

    public String getMysql_USER() {
        if (this.mysql_USER != null) {
            return this.mysql_USER;
        } else {
            return null;
        }
    }

    @Deprecated
    public String getMysql_PASSWORD() {
        if (this.mysql_PASSWORD != null) {
            return this.mysql_PASSWORD;
        } else {
            return null;
        }
    }

    public String getMysql_PORT() {
        if (this.mysql_PORT != null) {
            return this.mysql_PORT;
        } else {
            return null;
        }
    }
    // END OF MYSQL

    public boolean isAfk(Player p) {
        return afkMap.get(p.getUniqueId()) != null;
    }

    public boolean isFlying(Player p) {
        return Fly1.contains(p.getDisplayName()) || p.isFlying();
    }

    public boolean isGod(Player p) {
        return GodM.contains(p.getDisplayName());
    }

    public boolean isDebug() {
        return plugin.getConfig().getString("debug").equalsIgnoreCase("true");
    }

    public boolean isElevatorsEnabled() {
        return plugin.getConfig().getString("elevators").equalsIgnoreCase("true");
    }

    public boolean isFireAlarmsEnabled() {
        return plugin.getConfig().getString("firealarms").equalsIgnoreCase("true");
    }

    public boolean isDiscordBotEnabled() {
        return plugin.getConfig().getString("discordbot").equalsIgnoreCase("true");
    }

    public boolean isTrafficSystemEnabled() {
        return plugin.getConfig().getString("trafficsystem").equalsIgnoreCase("true");
    }

    public boolean isSignTranslateColors() {
        return plugin.getConfig().getString("signTranslateColors").equalsIgnoreCase("true");
    }

    public String FormatTime(LocalDateTime time) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return time.format(myFormatObj);
    }

    public String Time() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return time.format(myFormatObj);
    }

    public String convertTime(OfflinePlayer target) {
        Format format = new SimpleDateFormat("MM-dd-YYYY-HH:mm:ss z");
        Date date = new Date(TimeUnit.SECONDS.toMillis(target.getLastPlayed()));
        return format.format(date);
    }

    public String getServerVersion() {
        String version = this.plugin.getServer().getVersion();
        if (version.contains("1.15")) {
            return "1.15";
        } else if (version.contains("1.14")) {
            return "1.14";
        } else if (version.contains("1.13")) {
            return "1.13";
        } else if (version.contains("1.12")) {
            return "1.12";
        }
        return null;
    }

    public UUID getUUIDFromMojangAPI(String playername) {
        if (uuidCache.get(playername) != null) {
            return uuidCache.get(playername);
        }

        URL url;
        UUID uuid1 = null;
        try {
            url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playername);
            String uuid = (String) ((JSONObject) new JSONParser()
                    .parse(new InputStreamReader(url.openStream()))).get("id");
            uuid1 = UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-"
                    + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        uuidCache.put(playername, uuid1);
        return uuid1;
    }

    public Location getLocationFromFile(CustomYmlManager configinstance, String path) {
        if (configinstance == null || path == null) {
            return null;
        }

        return (Location) configinstance.getConfig().get(path);
    }

    public void setLocationToFile(CustomYmlManager configinstance, String path, Location location) {
        if (configinstance == null || path == null || location == null) {
            return;
        }

        configinstance.getConfig().set(path, location);
        configinstance.saveConfigSilent();
    }

    public Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public ConfigurationSection getPlayersYML(CustomConfigManager customConfigManager, Player player) {
        ConfigurationSection configurationSection = customConfigManager.getPlayersYml().getConfig().getConfigurationSection("UUID." + player.getUniqueId());
        if (configurationSection == null)
            configurationSection = customConfigManager.getPlayersYml().getConfig().createSection("UUID." + player.getUniqueId());
        return configurationSection;
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBoolean(String boolean1) {
        return boolean1.equalsIgnoreCase("true") || boolean1.equalsIgnoreCase("false");
    }

    public Integer asIntOrDefault(String input, int default1) {
        try {
            Integer.parseInt(input);
            return Integer.valueOf(input);
        } catch (Exception e) {
            return default1;
        }
    }

    public Long asLongOrDefault(String input, long default1) {
        try {
            Long.parseLong(input);
            return Long.valueOf(input);
        } catch (Exception e) {
            return default1;
        }
    }

    public Double asDoubleOrDefault(String input, double default1) {
        try {
            Double.parseDouble(input);
            return Double.valueOf(input);
        } catch (Exception e) {
            return default1;
        }
    }

    public float asFloatOrDefault(String input, float default1) {
        try {
            Float.parseFloat(input);
            return Float.parseFloat(input);
        } catch (Exception e) {
            return default1;
        }
    }

    public boolean asBooleanOrDefault(String boolean1, boolean default1) {
        try {
            Boolean.parseBoolean(boolean1);
            return Boolean.valueOf(boolean1);
        } catch (Exception e) {
            return default1;
        }
    }

    public boolean isClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void PermissionErrorMessage(Player p) {
        p.sendMessage(Translate.chat("&4You do not have permission to do this command."));
    }

    private void ClearHashMapMessage(String place) {
        plugin.getServer().getConsoleSender().sendMessage(Translate.chat(USELESS_TAG
                + " Class: World16.Utils.API has cleared the HashMap of " + place + " For EVERY PLAYER"));
    }

    private void ClearHashMapMessage(String place, Player p) {
        plugin.getServer().getConsoleSender().sendMessage(Translate.chat(USELESS_TAG
                + " Class: World16.Utils.API has cleared the HashMap of " + place + " For Player: " + p
                .getDisplayName()));
    }

    private void ClearArrayListMessage(String place) {
        plugin.getServer().getConsoleSender().sendMessage(Translate.chat(USELESS_TAG
                + " Class: World16.Utils.API has cleared the ArrayList of " + place + " For EVERY PLAYER"));
    }

    private void ClearArrayListMessage(String place, Player p) {
        plugin.getServer().getConsoleSender().sendMessage(Translate.chat(USELESS_TAG
                + " Class: World16.Utils.API has cleared the ArrayList of " + place + " For Player: " + p
                .getDisplayName()));
    }
}