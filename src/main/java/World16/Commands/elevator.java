package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.TabComplete.ElevatorTab;
import World16.Utils.API;
import World16.Utils.SimpleMath;
import World16.Utils.Translate;
import World16Elevators.ElevatorManager;
import World16Elevators.Objects.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;

public class elevator implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;
    private WorldEditPlugin worldEditPlugin;
    private ElevatorManager elevatorManager;

    private Map<String, ElevatorObject> elevatorObjectMap;

    private SimpleMath simpleMath;

    public elevator(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);
        this.simpleMath = new SimpleMath(this.plugin);

        this.worldEditPlugin = this.plugin.getOtherPlugins().getWorldEditPlugin();
        this.elevatorObjectMap = this.plugin.getSetListMap().getElevatorObjectMap();
        this.elevatorManager = this.plugin.getElevatorManager();

        this.plugin.getCommand("elevator").setExecutor(this);
        this.plugin.getCommand("elevator").setTabCompleter(new ElevatorTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!this.plugin.getOtherPlugins().hasWorldEdit()) {
            return true;
        }

        if (!(sender instanceof Player)) {

            if (!(sender instanceof BlockCommandSender)) {
                return true;
            }

            BlockCommandSender cmdblock = (BlockCommandSender) sender;
            Block commandblock = cmdblock.getBlock();

            if (args.length == 3 && args[0].equalsIgnoreCase("click")) {
                String elevatorName = args[1].toLowerCase();
                String key = args[2].toLowerCase();

                if (elevatorObjectMap.get(elevatorName) == null) {
                    return true;
                }
                ElevatorObject elevatorObject = elevatorObjectMap.get(elevatorName);

                if (key.equalsIgnoreCase("@dxdydz")) {
                    elevatorObject.getPlayers().forEach(elevatorObject::clickMessageGoto);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("call")) {
                if (args.length == 1) {
                    return true;
                } else if (args.length == 3) {
                    String elevatorName = args[1].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[2], 0);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null) {
                        return true;
                    }

                    elevatorObjectMap.get(elevatorName).goToFloor(floorNum, ElevatorStatus.DONT_KNOW);
                    return true;
                } else if (args.length == 4 && api.isBoolean(args[3])) {
                    String elevatorName = args[1].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[2], 0);
                    boolean goUp = api.asBooleanOrDefault(args[3], false);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null) {
                        return true;
                    }

                    ElevatorStatus elevatorStatus = ElevatorStatus.DONT_KNOW;
                    elevatorObjectMap.get(elevatorName).goToFloor(floorNum, elevatorStatus.upOrDown(goUp));
                    return true;
                } else if (args.length == 4 && !api.isBoolean(args[3])) {
                    String elevatorName = args[1].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[2], 0);
                    int toFloorNum = api.asIntOrDefault(args[3], 0);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null || elevatorObjectMap.get(elevatorName).getFloorsMap().get(toFloorNum) == null) {
                        return true;
                    }

                    elevatorObjectMap.get(elevatorName).callElevator(floorNum, toFloorNum);
                    return true;
                }
                return true;
            }
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("world16.elevator")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (!this.plugin.getApi().isElevatorsEnabled() || !this.plugin.getOtherPlugins().hasWorldEdit()) {
            p.sendMessage(Translate.chat("Looks like elevators aren't enabled!"));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("Elevator Help"));
            p.sendMessage(Translate.chat("/elevator create <Shows help for creation of a elevator.>"));
            p.sendMessage(Translate.chat("/elevator delete <ElevatorName>"));
            p.sendMessage(Translate.chat("/elevator floor <Shows help for the floor."));
            p.sendMessage(Translate.chat("/elevator call <Shows help to call the elevator."));
            p.sendMessage(Translate.chat("/elevator rename <ElevatorName> <TOElevatorName>"));
            return true;
        } else {

            //CREATE
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    p.sendMessage(Translate.chat("[Elevator creation]"));
                    p.sendMessage(Translate.chat("/elevator create <ElevatorName> <XAX> <XAY> <XAZ> <XBX> <XBY> <XAZ>"));
                    return true;
                }
                if (args.length == 8) {
                    String elevatorName = args[1].toLowerCase();
                    int XAX = api.asIntOrDefault(args[2], 0);
                    int XAY = api.asIntOrDefault(args[3], 0);
                    int XAZ = api.asIntOrDefault(args[4], 0);

                    int XBX = api.asIntOrDefault(args[5], 0);
                    int XBY = api.asIntOrDefault(args[6], 0);
                    int XBZ = api.asIntOrDefault(args[7], 0);

                    Location down = new Location(p.getWorld(), XAX, XAY, XAZ);
                    Location up = new Location(p.getWorld(), XBX, XBY, XBZ);

                    Region region = getSelection(p);

                    if (region == null) {
                        p.sendMessage("Please make a selection with WorldEdit and then redo the command please.");
                        return true;
                    }

                    Vector one = new Vector(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
                    Vector two = new Vector(region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());
                    FloorObject floorObject = new FloorObject(0, api.getBlockPlayerIsLookingAt(p).getLocation(), simpleMath.toBoundingBox(one, two));

                    BoundingBox boundingBox = simpleMath.toBoundingBox(new Vector(XAX, XAY, XAZ), new Vector(XBX, XBY, XBZ));
                    ElevatorObject elevatorObject = new ElevatorObject(plugin, p.getWorld().getName(), elevatorName, floorObject, boundingBox);
                    elevatorObjectMap.putIfAbsent(elevatorName, elevatorObject);
                    p.sendMessage(Translate.chat("Elevator: " + elevatorName + " has been created"));
                    return true;
                }
            }

            //FLOOR
            if (args[0].equalsIgnoreCase("floor")) {
                if (args.length == 1) {
                    p.sendMessage(Translate.chat("[Elevator Floor Setup]"));
                    p.sendMessage(Translate.chat("/elevator floor create <ElevatorName> <FloorNumber>"));
                    p.sendMessage(Translate.chat("/elevator floor easycreate <ElevatorName> <FloorNumber>"));
                    p.sendMessage(Translate.chat("/elevator floor delete <ElevatorName> <FloorNumber>"));
                    p.sendMessage(Translate.chat("/elevator floor sign <ElevatorName> <FloorNumber>"));
                    return true;
                }
                if (args.length == 4 && args[1].equalsIgnoreCase("create")) {
                    String elevatorName = args[2].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[3], 0);

                    Region region = getSelection(p);
                    if (region == null) {
                        p.sendMessage("Please make a selection with WorldEdit and then redo the command please.");
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That elevator doesn't exist."));
                        return true;
                    }

                    Vector one = new Vector(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
                    Vector two = new Vector(region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());
                    BoundingBox boundingBox = simpleMath.toBoundingBox(one, two);
                    elevatorObjectMap.get(elevatorName).addFloor(new FloorObject(floorNum, api.getBlockPlayerIsLookingAt(p).getLocation(), boundingBox));
                    p.sendMessage(Translate.chat("Floor: " + floorNum + " has been added to the elevator: " + elevatorName));
                    return true;
                } else if (args.length == 4 && args[1].equalsIgnoreCase("easycreate")) {
                    String elevatorName = args[2].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[3], 0);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That elevator doesn't exist."));
                        return true;
                    }

                    elevatorObjectMap.get(elevatorName).addFloor(new FloorObject(floorNum, api.getBlockPlayerIsLookingAt(p).getLocation()));
                    p.sendMessage(Translate.chat("[EasyCreate] Floor: " + floorNum + " has been added to the elevator: " + elevatorName));
                    return true;
                } else if (args.length == 4 && args[1].equalsIgnoreCase("delete")) {
                    String elevatorName = args[2].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[3], 0);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That elevator doesn't exist."));
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null) {
                        p.sendMessage(Translate.chat("This floor doesn't exist."));
                        return true;
                    }

                    this.elevatorManager.deleteFloorOfElevator(elevatorName, floorNum);
                    p.sendMessage(Translate.chat("The floor: " + floorNum + " has been removed from the elevator: " + elevatorName));
                    return true;
                } else if (args.length == 4 && args[1].equalsIgnoreCase("sign")) {
                    String elevatorName = args[2].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[3], 0);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That elevator doesn't exist."));
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null) {
                        p.sendMessage(Translate.chat("This floor doesn't exist."));
                        return true;
                    }

                    elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum).setSignObject(new SignObject(this.api.getBlockPlayerIsLookingAt(p).getLocation()));
                    p.sendMessage(Translate.chat("Sign has been set"));
                    return true;
                }
                return true;
            }
            //END OF FLOOR

            if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
                String elevatorName = args[1].toLowerCase();

                if (elevatorObjectMap.get(elevatorName) == null) {
                    p.sendMessage(Translate.chat("That elevator doesn't exist."));
                    return true;
                }
                this.plugin.getElevatorManager().deleteElevator(elevatorName);
                p.sendMessage(Translate.chat("Elevator: " + elevatorName + " has been deleted."));
                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
                this.plugin.getElevatorManager().saveAllElevators();
                p.sendMessage(Translate.chat("All elevators have been saved."));
                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("load")) {
                this.plugin.getElevatorManager().loadAllElevators();
                p.sendMessage(Translate.chat("All elevators have been loaded in memory."));
                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
                elevatorObjectMap.clear();
                p.sendMessage(Translate.chat("All elevators have been cleared in memory."));
                return true;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("toString")) {
                String elevatorName = args[1].toLowerCase();

                if (elevatorObjectMap.get(elevatorName) == null) {
                    p.sendMessage(Translate.chat("That elevator doesn't exist."));
                    return true;
                }

                this.plugin.getServer().getConsoleSender().sendMessage(elevatorObjectMap.get(elevatorName).toString());
                return true;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("stop")) {
                String elevatorName = args[1].toLowerCase();

                if (elevatorObjectMap.get(elevatorName) == null) {
                    p.sendMessage(Translate.chat("That elevator doesn't exist."));
                    return true;
                }

                elevatorObjectMap.get(elevatorName).emergencyStop();
                p.sendMessage(Translate.chat("emergency stop has been activated."));
                return true;
            }

            if (args[0].equalsIgnoreCase("queue")) {
                if (args.length == 1) {
                    p.sendMessage(Translate.chat("Elevator queue HELP"));
                    p.sendMessage(Translate.chat("/elevator queue list"));
                    p.sendMessage(Translate.chat("/elevator queue clear"));
                    return true;
                }

                if (args.length == 3 && args[1].equalsIgnoreCase("list")) {
                    String elevatorName = args[2].toLowerCase();

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That elevator doesn't exist."));
                        return true;
                    }
                    Queue<FloorQueueObject> floorQ = elevatorObjectMap.get(elevatorName).getFloorQueueBuffer();
                    List<Integer> integerList = new ArrayList<>();
                    for (FloorQueueObject floorQueueObject : floorQ) {
                        integerList.add(floorQueueObject.getFloorNumber());
                    }
                    Integer[] integers = integerList.toArray(new Integer[0]);
                    p.sendMessage(Arrays.toString(integers));
                    return true;
                }
                if (args.length == 3 && args[1].equalsIgnoreCase("clear")) {
                    String elevatorName = args[2].toLowerCase();

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That elevator doesn't exist."));
                        return true;
                    }

                    elevatorObjectMap.get(elevatorName).getFloorQueueBuffer().clear();
                    p.sendMessage(Translate.chat("The floor queue for the elevator: " + elevatorName + " has been cleared."));
                    return true;
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("click")) {
                String elevatorName = args[1].toLowerCase();

                if (elevatorObjectMap.get(elevatorName) == null) {
                    p.sendMessage(Translate.chat("That elevator doesn't exist."));
                    return true;
                }

                elevatorObjectMap.get(elevatorName).clickMessageGoto(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("call")) {
                if (args.length == 1) {
                    p.sendMessage(Translate.chat("/elevator call <ElevatorName> <FloorNumber>"));
                    p.sendMessage(Translate.chat("/elevator call <ElevatorName <FloorNumber> <GoUp?>"));
                    p.sendMessage(Translate.chat("/elevator call <ElevatorName> <FloorNumber> <FloorNumberToGoTo>"));
                } else if (args.length == 3) {
                    String elevatorName = args[1].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[2], 0);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That isn't a floor."));
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null) {
                        p.sendMessage(Translate.chat("That floor does not exist."));
                        return true;
                    }

                    elevatorObjectMap.get(elevatorName).goToFloor(floorNum, ElevatorStatus.DONT_KNOW);
                    p.sendMessage(Translate.chat("Going to floor: " + floorNum + " for the Elevator: " + elevatorName));
                    return true;
                } else if (args.length == 4 && api.isBoolean(args[3])) {
                    String elevatorName = args[1].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[2], 0);
                    boolean goUp = api.asBooleanOrDefault(args[3], false);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That isn't a floor."));
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null) {
                        p.sendMessage(Translate.chat("That floor does not exist."));
                        return true;
                    }

                    ElevatorStatus elevatorStatus = ElevatorStatus.DONT_KNOW;
                    elevatorObjectMap.get(elevatorName).goToFloor(floorNum, elevatorStatus.upOrDown(goUp));
                    p.sendMessage(Translate.chat("Going to floor: " + floorNum + " for the Elevator: " + elevatorName + " ElevatorStatus: " + elevatorStatus.upOrDown(goUp)));
                    return true;
                } else if (args.length == 4 && !api.isBoolean(args[3])) {
                    String elevatorName = args[1].toLowerCase();
                    int floorNum = api.asIntOrDefault(args[2], 0);
                    int toFloorNum = api.asIntOrDefault(args[3], 0);

                    if (elevatorObjectMap.get(elevatorName) == null) {
                        p.sendMessage(Translate.chat("That elevator doesn't exist."));
                        return true;
                    }

                    if (elevatorObjectMap.get(elevatorName).getFloorsMap().get(floorNum) == null || elevatorObjectMap.get(elevatorName).getFloorsMap().get(toFloorNum) == null) {
                        p.sendMessage(Translate.chat("Floor doesn't exist on this elevator"));
                        return true;
                    }

                    elevatorObjectMap.get(elevatorName).callElevator(floorNum, toFloorNum);
                    p.sendMessage(Translate.chat("The elevator: " + elevatorName + " has been called to " + floorNum + " to go to " + toFloorNum));
                    return true;
                }
                return true;
            } else if (args.length == 3 && args[0].equalsIgnoreCase("rename")) {
                String elevatorName = args[1].toLowerCase();
                String toElevatorName = args[2].toLowerCase();

                if (elevatorObjectMap.get(elevatorName) == null) {
                    p.sendMessage(Translate.chat("That elevator doesn't exist."));
                    return true;
                }

                ElevatorObject elevatorObject = elevatorObjectMap.get(elevatorName);
                elevatorManager.deleteElevator(elevatorName);
                elevatorObject.setElevatorName(toElevatorName);
                elevatorObjectMap.putIfAbsent(toElevatorName, elevatorObject);
                p.sendMessage(Translate.chat("Old Name: " + elevatorName + " new Name: " + toElevatorName));
                return true;
            }
            return true;
        }
    }

    public Region getSelection(Player player) {
        Region region;
        try {
            region = worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
        } catch (Exception ex) {
            return null;
        }
        return region;
    }
}