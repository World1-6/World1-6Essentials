package World16.Commands;

import World16.CustomConfigs.CustomConfigManager;
import World16.Events.PlayerInteractEvent;
import World16.Main.Main;
import World16.Objects.RawLocationObject;
import World16.Storage.OldMySQL;
import World16.Utils.API;
import World16.Utils.CustomYmlManager;
import World16.Utils.KeyAPI;
import World16.Utils.Translate;
import World16.test.ERamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class eram implements CommandExecutor {

    private Main plugin;

    OldMySQL mysql;
    KeyAPI keyapi = new KeyAPI();
    API api = new API();

    private CustomYmlManager shitYml = null;
    private CustomConfigManager customConfigManager;

    public eram(CustomConfigManager getCustomYml, Main getPlugin) {
        this.shitYml = getCustomYml.getShitYml();
        this.customConfigManager = getCustomYml;
        this.plugin = getPlugin;
        this.mysql = new OldMySQL();

        eRamManager = new ERamManager(this.customConfigManager);

        this.plugin.getCommand("eram").setExecutor(this);
    }

    Map<String, Map<String, RawLocationObject>> aaa = ERamManager.stringRawLocationObjectHashMap;
    Map<String, Location> latestClickedBlocked = PlayerInteractEvent.latestClickedBlocked;

    private ERamManager eRamManager;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 3 && args[0].equalsIgnoreCase("do") && args[1] != null && args[2] != null) {
                eRamManager.doIt(args[1], args[2]);
                return true;
            }else if(args.length == 8 && args[0].equalsIgnoreCase("copy")){
                //copy
                String playerName = args[1];
                UUID uuid = UUID.fromString(args[2]);
                String saveName = args[3];
                String letter = args[4];
                String x = args[5];
                String y = args[6];
                String z = args[7];

                aaa.computeIfAbsent(playerName, k -> new HashMap<>());
                if (aaa.get(playerName).get(saveName) == null) {
                    aaa.get(playerName).put(saveName, new RawLocationObject());
                }
                aaa.get(playerName).get(saveName).set(letter, new Location(Bukkit.getWorld("world"), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z)));
                eRamManager.saveThingy(playerName, uuid, saveName);
            }
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("world16.eram")) {
            api.PermissionErrorMessage(p);
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: Please use tab complete!"));
            return true;
        } else if (args.length == 3 && args[0] != null && args[1] != null && args[2] != null && args[0].equalsIgnoreCase("save")) {
            String saveName = args[1].toLowerCase();
            String letter = args[2].toUpperCase();

            aaa.computeIfAbsent(p.getDisplayName(), k -> new HashMap<>());
            if (aaa.get(p.getDisplayName()).get(saveName) == null) {
                aaa.get(p.getDisplayName()).put(saveName, new RawLocationObject());
            }
            aaa.get(p.getDisplayName()).get(saveName).set(letter, latestClickedBlocked.get(p.getDisplayName()));
            eRamManager.saveThingy(p.getDisplayName(), p.getUniqueId(), saveName);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("load")) {
            eRamManager.loadUp(p);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("do") && args[1] != null) {
            eRamManager.doIt(p.getDisplayName(), args[1]);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (aaa.get(p.getDisplayName()).isEmpty()) {
                return true;
            }
            Set<String> homeSet = aaa.get(p.getDisplayName()).keySet();
            String[] homeString = homeSet.toArray(new String[0]);
            Arrays.sort(homeString);
            String str = String.join(", ", homeString);
            p.sendMessage(Translate.chat(str));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete") && args[1] != null) {
            eRamManager.delete(p.getDisplayName(), p.getUniqueId(), args[1].toLowerCase());
            p.sendMessage(Translate.chat("&cDone..."));
            return true;
        } else {
            p.sendMessage("Something messed up!");
            return true;
        }
    }
}