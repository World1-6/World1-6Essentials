package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.ERamManager;
import World16.TabComplete.ERamTab;
import World16.Utils.API;
import World16.Utils.ERamTag;
import World16.Utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class eram implements CommandExecutor {

    private Main plugin;

    private API api;

    private ERamManager eRamManager;

    private CustomConfigManager customConfigManager;

    private ERamTag ERamTagClass;

    //Maps
    private Map<String, Map<String, List<Location>>> eramM;
    private Map<String, Location> latestClickedBlocked;

    //...

    public eram(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        eRamManager = new ERamManager(this.plugin, this.customConfigManager);
        this.ERamTagClass = new ERamTag(this.plugin.getSetListMap());

        this.eramM = this.plugin.getSetListMap().geteRamRaw();
        this.latestClickedBlocked = this.plugin.getSetListMap().getLatestClickedBlocked();

        this.plugin.getCommand("eram").setExecutor(this);
        this.plugin.getCommand("eram").setTabCompleter(new ERamTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {

            if (!(sender instanceof BlockCommandSender)) {
                return true;
            }

            BlockCommandSender cmdblock = (BlockCommandSender) sender;
            Block commandblock = cmdblock.getBlock();

            if (args.length == 3 && args[0].equalsIgnoreCase("do") && args[1] != null && args[2] != null) {
                String playerName = args[1];
                String tag = args[2];

                if (tag.startsWith("@")) {
                    String tagWithoutA = tag.replace("@", "");
                    tag = ERamTagClass.getTag(playerName, tagWithoutA);
                }
                eRamManager.doIt(playerName, tag);
                return true;
            } else if (args.length == 3 && args[0].equalsIgnoreCase("undo") && args[1] != null && args[2] != null) {
                String playerName = args[1];
                String tag = args[2];

                if (tag.startsWith("@")) {
                    String tagWithoutA = tag.replace("@", "");
                    tag = ERamTagClass.getTag(playerName, tagWithoutA);
                }
                eRamManager.undoIt(playerName, tag);
                return true;
            } else if (args.length == 3 && args[0].equalsIgnoreCase("deletetag")) {
                String playerName = args[1];
                String tagName = args[2].toLowerCase();

                if (tagName.startsWith("@")) {
                    String tagWithoutA = tagName.replace("@", "");
                    tagName = ERamTagClass.getTag(playerName, tagWithoutA);
                }

                ERamTagClass.removeTag(playerName, tagName);
                return true;
            } else if (args.length == 4 && args[0].equalsIgnoreCase("addtag")) {
                String playerName = args[1];
                String tagName = args[2].toLowerCase();
                String string = args[3].toLowerCase();
                ERamTagClass.addTag(playerName, tagName, string);
                return true;
            } else if (args.length == 4 && args[0].equalsIgnoreCase("delete")) {
                String playerName = args[1];
                UUID uuid = api.getUUIDFromMojangAPI(playerName);
                String tagName = args[2].toLowerCase();

                eRamManager.delete(playerName, uuid, tagName);
                return true;
            } else if (args.length == 6 && args[0].equalsIgnoreCase("copy")) {
                //copy
                String playerName = args[1];
                UUID uuid = api.getUUIDFromMojangAPI(playerName);
                String saveName = args[2];
                String x = args[3];
                String y = args[4];
                String z = args[5];

                if (saveName.startsWith("@")) {
                    String tagWithoutA = saveName.replace("@", "");
                    saveName = ERamTagClass.getTag(playerName, tagWithoutA);
                }

                //Relative cords
                if (x.startsWith("~")) {
                    String newX = x.replace("~", "");
                    int xAsInt = api.asIntOrDefault(newX, 0);
                    int xDone = commandblock.getX() + xAsInt;
                    x = String.valueOf(xDone);
                }

                if (y.startsWith("~")) {
                    String newY = y.replace("~", "");
                    int yAsInt = api.asIntOrDefault(newY, 0);
                    int yDone = commandblock.getY() + yAsInt;
                    y = String.valueOf(yDone);
                }

                if (z.startsWith("~")) {
                    String newZ = z.replace("~", "");
                    int zAsInt = api.asIntOrDefault(newZ, 0);
                    int zDone = commandblock.getZ() + zAsInt;
                    z = String.valueOf(zDone);
                }
                //...

                Block block = this.plugin.getServer().getWorld("world").getBlockAt(api.asIntOrDefault(x, 1), api.asIntOrDefault(y, 1), api.asIntOrDefault(z, 1));
                if (block.getType() != Material.REDSTONE_BLOCK) {
                    return true;
                }

                if (saveName == null) {
                    Player player = Bukkit.getPlayerExact(args[1]);
                    if (player != null && player.isOnline()) {
                        player.sendMessage(Translate.chat("Hey can you check your command blcoks for eram because the saveName == null"));
                        return true;
                    }
                    return true;
                }

                eramM.computeIfAbsent(playerName, k -> new HashMap<>());
                eramM.get(playerName).computeIfAbsent(saveName, k -> new ArrayList<>());

                eramM.get(playerName).get(saveName).add(new Location(Bukkit.getWorld("world"), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z)));
                eRamManager.saveThingy(playerName, uuid, saveName);
            }
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }


        //PLAYER COMMANDS


        Player p = (Player) sender;
        if (!p.hasPermission("world16.eram")) {
            api.PermissionErrorMessage(p);
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: Please use tab complete!"));
            return true;
        } else if (args.length == 3 && args[0] != null && args[1] != null && args[0].equalsIgnoreCase("save")) {
            String saveName = args[1].toLowerCase();

            eramM.computeIfAbsent(p.getDisplayName(), k -> new HashMap<>());
            eramM.get(p.getDisplayName()).computeIfAbsent(saveName, k -> new ArrayList<>());

            eramM.get(p.getDisplayName()).get(saveName).add(latestClickedBlocked.get(p.getDisplayName()));
            eRamManager.saveThingy(p.getDisplayName(), p.getUniqueId(), saveName);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("load")) {
            eRamManager.loadUp(p);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("do") && args[1] != null) {
            eRamManager.doIt(p.getDisplayName(), args[1]);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("undo") && args[1] != null) {
            eRamManager.undoIt(p.getDisplayName(), args[1]);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (eramM.get(p.getDisplayName()) == null) {
                eramM.computeIfAbsent(p.getDisplayName(), k -> new HashMap<>());
                p.sendMessage("You almost broke the plugin but this if statement saved it!");
                return true;
            }

            if (eramM.get(p.getDisplayName()).isEmpty()) {
                p.sendMessage(Translate.chat("There is nothing?"));
                return true;
            }
            Set<String> homeSet = eramM.get(p.getDisplayName()).keySet();
            String[] homeString = homeSet.toArray(new String[0]);
            Arrays.sort(homeString);
            String str = String.join(", ", homeString);
            p.sendMessage(Translate.chat(str));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete") && args[1] != null) {
            eRamManager.delete(p.getDisplayName(), p.getUniqueId(), args[1].toLowerCase());
            p.sendMessage(Translate.chat("&cDone..."));
            return true;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("addtag")) {
            String tagName = args[1].toLowerCase();
            String string = args[2].toLowerCase();
            ERamTagClass.addTag(p.getDisplayName(), tagName, string);
            p.sendMessage(Translate.chat("&6Added tag -> " + tagName + " " + string));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("deletetag")) {
            String tagName = args[1].toLowerCase();
            ERamTagClass.removeTag(p.getDisplayName(), tagName);
            p.sendMessage(Translate.chat("&cRemoved tag -> " + tagName));
            return true;
        } else {
            p.sendMessage("Something messed up!");
            return true;
        }
    }
}