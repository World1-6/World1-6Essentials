package World16.Commands;

import World16.Main.Main;
import World16.Objects.LocationObject;
import World16.TabComplete.BackTab;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class back implements CommandExecutor {

    private Main plugin;
    private API api;

    //Maps
    private Map<UUID, LocationObject> backm;
    //...

    public back(Main plugin) {
        this.plugin = plugin;

        this.api = new API(this.plugin);

        this.backm = this.plugin.getSetListMap().getBackM();

        this.plugin.getCommand("back").setExecutor(this);
        this.plugin.getCommand("back").setTabCompleter(new BackTab(this.plugin));
    }

    /**
     * Death ID for getLocation is 1;
     * Tp ID for GetLocation is 2;
     * Set ID for GetLocation is 3;
     */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        LocationObject back = this.backm.get(p.getUniqueId());

        if (back == null) {
            backm.putIfAbsent(p.getUniqueId(), new LocationObject());
            p.sendMessage(Translate.chat("[&cBack&r] &cHey, If you see something broke in /back report this to Andrew121410#2035 on discord."));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("[&cBack&r] &a&oHere's all of the back commands/sub."));
            p.sendMessage(Translate.chat("&6/back death"));
            p.sendMessage(Translate.chat("&6/back tp"));
            p.sendMessage(Translate.chat("&6/back set"));
            p.sendMessage(Translate.chat("&6/back goto"));
            return true;
        }

        //DEATH
        if (args[0].equalsIgnoreCase("death")) {
            if (!p.hasPermission("world16.back.death")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            if (back.getLocation(1) != null) {
                Location deathlocation = back.getLocation(1);

                //Checks if it's Lava Or Water.
                if (deathlocation.getBlock().isLiquid() || deathlocation.getBlock().getRelative(BlockFace.DOWN).isLiquid()) {
                    deathlocation.getBlock().getRelative(BlockFace.DOWN).setType(Material.OAK_LOG);
                    deathlocation.getBlock().getRelative(BlockFace.EAST).setType(Material.OAK_LOG);
                    deathlocation.getBlock().getRelative(BlockFace.NORTH).setType(Material.OAK_LOG);
                    deathlocation.getBlock().getRelative(BlockFace.WEST).setType(Material.OAK_LOG);
                    deathlocation.getBlock().getRelative(BlockFace.SOUTH).setType(Material.OAK_LOG);
                    deathlocation.getBlock().setType(Material.AIR);
                }

                p.teleport(deathlocation);
                return true;
            } else {
                p.sendMessage(Translate.chat("[&cBack&r] &eLooks like you didn't die?"));
                return true;
            }

            //TP
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (!p.hasPermission("world16.back.tp")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            if (back.getLocation(2) != null) {
                p.teleport(back.getLocation(2));
                return true;
            } else {
                p.sendMessage(Translate.chat("[&cBack&r] &eLooks like you didn't teleport yet."));
                return true;
            }

            //SET
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!p.hasPermission("world16.back.set")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            back.setLocation("set", 3, p.getLocation());
            p.sendMessage(Translate.chat("[&cBack&r] &aYour back location has been set."));
            return true;

            //GOTO
        } else if (args[0].equalsIgnoreCase("goto")) {
            if (!p.hasPermission("world16.back.goto")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            if (back.getLocation(3) != null) {
                p.teleport(back.getLocation(3));
                p.sendMessage(Translate.chat("[&cBack&r] &6Done."));
                return true;
            } else {
                p.sendMessage(Translate.chat("[&cBack&r] &eLooks like you didn't &aset&e a back location yet."));
                return true;
            }
        }
        return true;
    }
}