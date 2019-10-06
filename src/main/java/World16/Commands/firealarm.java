package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import World16FireAlarms.Objects.Simple.SimpleFireAlarm;
import World16FireAlarms.Objects.Simple.SimpleStrobe;
import World16FireAlarms.Objects.TroubleReason;
import World16FireAlarms.Objects.Screen.FireAlarmScreen;
import World16FireAlarms.interfaces.IFireAlarm;
import World16FireAlarms.interfaces.IStrobe;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class firealarm implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    //Maps
    private Map<String, IFireAlarm> fireAlarmMap;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    //...

    public firealarm(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        this.plugin.getCommand("firealarm").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("world16.firealarm")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (!this.plugin.getApi().isFireAlarmsEnabled()) {
            p.sendMessage(Translate.chat("Looks like fire alarms aren't enabled!"));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("/firealarm register"));
            p.sendMessage(Translate.chat("/firealarm delete"));
            p.sendMessage(Translate.chat("/firealarm alarm <FireAlarmName>"));
            p.sendMessage(Translate.chat("/firealarm reset <FireAlarmName>"));
            return true;
        } else if (args[0].equalsIgnoreCase("register")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm register firealarm <Name>"));
                p.sendMessage(Translate.chat("/firealarm register sign <FireAlarmName> <Name>"));
                p.sendMessage(Translate.chat("/firealarm register strobe <FireAlarmName> <StrobeName>"));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("firealarm")) {
                String name = args[2].toLowerCase();
                IFireAlarm iFireAlarm = new SimpleFireAlarm(plugin, name);
                this.fireAlarmMap.putIfAbsent(name, iFireAlarm);
                p.sendMessage(Translate.chat("Fire Alarm: " + name + " is now registered."));
                return true;
            } else if (args.length == 4 && args[1].equalsIgnoreCase("sign")) {
                String fireAlarmName = args[2].toLowerCase();
                String signName = args[3].toLowerCase();

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }

                Location location = api.getBlockPlayerIsLookingAt(p).getLocation();
                FireAlarmScreen fireAlarmScreen = new FireAlarmScreen(plugin, signName, fireAlarmName, location);
                this.fireAlarmScreenMap.putIfAbsent(location, fireAlarmScreen);
                this.fireAlarmMap.get(fireAlarmName).registerSign(signName, location);
                p.sendMessage(Translate.chat("The sign: " + signName + " is now registered to " + fireAlarmName));
                return true;
            } else if (args.length == 4 && args[1].equalsIgnoreCase("strobe")) {
                String firealarmName = args[2].toLowerCase();
                String strobeName = args[3].toLowerCase();

                if (this.fireAlarmMap.get(firealarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + firealarmName));
                    return true;
                }

                Location location = api.getBlockPlayerIsLookingAt(p).getLocation();
                this.fireAlarmMap.get(firealarmName).registerStrobe(new SimpleStrobe(location, strobeName, null));
                p.sendMessage(Translate.chat("Strobe: " + strobeName + " is now registered with the fire alarm: " + firealarmName));
                return true;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm delete firealarm <Name>"));
                p.sendMessage(Translate.chat("/fiealarm delete strobe <FireAlarmName> <Name>"));
                p.sendMessage(Translate.chat("/firealarm delete strobe <FireAlarmName>"));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("firealarm")) {
                String fireAlarmName = args[2].toLowerCase();

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }

                this.plugin.getFireAlarmManager().deleteFireAlarm(fireAlarmName);
                p.sendMessage(Translate.chat("Fire alarm: " + fireAlarmName + " has been deleted."));
                return true;
            } else if (args.length == 4 && args[1].equalsIgnoreCase("strobe")) {
                String fireAlarmName = args[2].toLowerCase();
                String strobeName = args[3].toLowerCase();

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }

                if (this.fireAlarmMap.get(fireAlarmName).getStrobesMap().get(strobeName) == null) {
                    p.sendMessage(Translate.chat("THere's no such strobe named: " + strobeName));
                    return true;
                }

                this.plugin.getFireAlarmManager().deleteFireAlarmStrobe(fireAlarmName, strobeName);
                p.sendMessage(Translate.chat("The strobe: " + strobeName + " for the fire alarm: " + fireAlarmName + " has been removed."));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("strobe")) {
                String fireAlarmName = args[2].toLowerCase();
                Location location = api.getBlockPlayerIsLookingAt(p).getLocation();

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }

                IStrobe iStrobe = null;

                for (Map.Entry<String, IStrobe> entry : this.fireAlarmMap.get(fireAlarmName).getStrobesMap().entrySet()) {
                    String k = entry.getKey();
                    IStrobe v = entry.getValue();
                    int x = v.getLocation().getBlockX();
                    int y = v.getLocation().getBlockY();
                    int z = v.getLocation().getBlockZ();

                    int x1 = location.getBlockX();
                    int y1 = location.getBlockY();
                    int z1 = location.getBlockZ();

                    if (x == x1 && y == y1 && z == z1) {
                        p.sendMessage(Translate.chat("FOUND"));
                        iStrobe = v;
                    }
                }

                if (iStrobe == null) {
                    p.sendMessage(Translate.chat("Could not find..."));
                    return true;
                }
                this.plugin.getFireAlarmManager().deleteFireAlarmStrobe(fireAlarmName, iStrobe.getName());
                p.sendMessage(Translate.chat("The strobe: " + iStrobe.getName() + " has been deleted from fire alarm: " + fireAlarmName));
                return true;
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("alarm")) {
            String name = args[1].toLowerCase();

            if (this.fireAlarmMap.get(name) == null) {
                p.sendMessage(Translate.chat("There's no such fire alarm called: " + name));
                return true;
            }

            this.fireAlarmMap.get(name).alarm(java.util.Optional.<World16FireAlarms.Objects.Zone>empty(), TroubleReason.PANEL_TEST);
            p.sendMessage(Translate.chat("Alright, the fire alarm should be going off currently."));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            String name = args[1].toLowerCase();

            if (this.fireAlarmMap.get(name) == null) {
                p.sendMessage(Translate.chat("There's no such fire alarm called: " + name));
                return true;
            }

            this.fireAlarmMap.get(name).reset(Optional.empty());
            p.sendMessage(Translate.chat("The fire alarm: " + name + " has been resetedede"));
            return true;
        }
        return true;
    }
}