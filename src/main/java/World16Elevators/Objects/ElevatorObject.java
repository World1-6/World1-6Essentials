package World16Elevators.Objects;

import World16.Main.Main;
import World16.Utils.SimpleMath;
import World16.Utils.SmoothTeleport;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.*;
import java.util.stream.Collectors;

@SerializableAs("ElevatorObject")
public class ElevatorObject implements ConfigurationSerializable {

    private String elevatorName;
    private String world;
    private ElevatorMovement elevatorMovement;

    //Bounding BOX
    private Location locationDownPLUS;
    private Location locationUpPLUS;
    //...

    private Map<Integer, FloorObject> floorsMap;

    //Config
    private final long ticksPerSecond = 6L;
    private final long doorHolderTicksPerSecond = 20L * 5L;
    private final long elevatorWaiterTicksPerSecond = 20L * 6L;
    //...

    //TEMP DON'T SAVE
    private Main plugin;

    private boolean isGoing;
    private boolean isFloorQueueGoing;
    private boolean isIdling;
    private boolean isEmergencyStop;

    private int topFloor = 0;
    private int topBottomFloor = 0;

    private Queue<FloorQueueObject> floorQueueBuffer;
    private Queue<Integer> floorBuffer;
    private StopBy stopBy;

    public ElevatorObject(boolean fromSave, Main plugin, String world, String nameOfElevator, ElevatorMovement elevatorMovement, BoundingBox boundingBox) {
        if (plugin != null) this.plugin = plugin;

        this.world = world; //NEEDS TO BE SECOND.

        this.floorsMap = new HashMap<>();
        this.floorQueueBuffer = new LinkedList<>();
        this.floorBuffer = new LinkedList<>();
        this.stopBy = new StopBy();

        this.elevatorName = nameOfElevator;
        this.elevatorMovement = elevatorMovement;

        this.locationDownPLUS = boundingBox.getMin().toLocation(getBukkitWorld());
        this.locationUpPLUS = boundingBox.getMax().toLocation(getBukkitWorld());

        this.isGoing = false;
        this.isFloorQueueGoing = false;
        this.isIdling = false;
        this.isEmergencyStop = false;

        if (!fromSave) this.floorsMap.putIfAbsent(0, FloorObject.from(elevatorMovement));
    }

    public Collection<Entity> getEntities() {
        return getBukkitWorld().getNearbyEntities(SimpleMath.toBoundingBox(locationDownPLUS.toVector(), locationUpPLUS.toVector()));
    }

    public Collection<Player> getPlayers() {
        return getBukkitWorld().getNearbyEntities(SimpleMath.toBoundingBox(locationDownPLUS.toVector(), locationUpPLUS.toVector())).stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).collect(Collectors.toList());
    }

    public void callElevator(int whatFloor, int toWhatFloor) {
        goToFloor(whatFloor, ElevatorStatus.DONT_KNOW);
        goToFloor(toWhatFloor, ElevatorStatus.DONT_KNOW);
    }

    public void goToFloor(int floorNum, ElevatorStatus elevatorStatus) {
        boolean goUp;

        //Check if the floor is a thing or not.
        if (getFloor(floorNum) == null) return;

        //Add to the queue if elevator is running or idling.
        if (isGoing || isIdling) {
            if (this.floorBuffer.contains(floorNum) && this.stopBy.toElevatorStatus() == elevatorStatus) {
                this.stopBy.getStopByQueue().add(floorNum);
            } else {
                floorQueueBuffer.add(new FloorQueueObject(floorNum, elevatorStatus));
                setupFloorQueue();
            }
            return;
        }
        isGoing = true;
        floorBuffer.clear(); //Clears the floorBuffer

        //Gets the floor before the elevator starts ticking.
        FloorObject floorObject = getFloor(floorNum);

        //Checks if the elevator should go up or down.
        goUp = floorObject.getMainDoor().getY() > this.elevatorMovement.getAtDoor().getY();

        //This caculates what floors it's going to pass going up or down this has to be run before it sets this.elevatorFloor to not a floor.
        calculateFloorBuffer(floorNum, goUp);

        this.stopBy.setGoUp(goUp);

        this.elevatorMovement.setFloor(null); //Not on a floor.

        //Tell the elevator to go down instead of up.
        if (!goUp) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (isIdling) return;
                    reCalculateFloorBuffer(goUp);
                    FloorObject stopByFloor = null;
                    if (!stopBy.getStopByQueue().isEmpty()) stopByFloor = getFloor(stopBy.getStopByQueue().peek());

                    //Check's if at floor if so then stop the elvator.
                    if (elevatorMovement.getAtDoor().getY() == floorObject.getMainDoor().getY()) {
                        this.cancel();
                        elevatorMovement.setFloor(floorNum);
                        floorDone(floorObject, elevatorStatus);
                        doFloorIdle();
                        isGoing = false;
                        return;
                    } else if (stopByFloor != null && elevatorMovement.getAtDoor().getY() == stopByFloor.getMainDoor().getY()) {
                        isIdling = true;
                        stopBy.getStopByQueue().remove();
                        elevatorMovement.setFloor(floorNum);
                        floorDone(stopByFloor, elevatorStatus);
                        doFloorIdle();
                        return;
                    }

//                    Stop's the elevator if emergencyStop is on.
                    if (isEmergencyStop) {
                        isIdling = false;
                        isGoing = false;
                        isEmergencyStop = false;
                        this.cancel();
                        return;
                    }

                    worldEditMoveDOWN(floorNum);

                    //TP THEM DOWN 1
                    for (Player player : getPlayers()) {
                        SmoothTeleport.teleport(player, player.getLocation().subtract(0, 1, 0));
                    }

                }
            }.runTaskTimer(plugin, ticksPerSecond, ticksPerSecond);
            return;
        }

        //Tell the elevator to go up instead of down.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isIdling) return;
                reCalculateFloorBuffer(goUp);
                FloorObject stopByFloor = null;
                if (!stopBy.getStopByQueue().isEmpty()) stopByFloor = getFloor(stopBy.getStopByQueue().peek());

//                Check's if at floor if so then stop the elvator.
                if (elevatorMovement.getAtDoor().getY() == floorObject.getMainDoor().getY()) {
                    this.cancel();
                    elevatorMovement.setFloor(floorNum);
                    floorDone(floorObject, elevatorStatus);
                    doFloorIdle();
                    isGoing = false;
                    return;
                } else if (stopByFloor != null && elevatorMovement.getAtDoor().getY() == stopByFloor.getMainDoor().getY()) {
                    isIdling = true;
                    stopBy.getStopByQueue().remove();
                    elevatorMovement.setFloor(floorNum);
                    floorDone(stopByFloor, elevatorStatus);
                    doFloorIdle();
                    return;
                }

//                Stop's the elevator if emergencyStop is on.
                if (isEmergencyStop) {
                    isIdling = false;
                    isGoing = false;
                    isEmergencyStop = false;
                    this.cancel();
                    return;
                }

                worldEditMoveUP(floorNum);

                //TP THEM UP 1
                for (Player player : getPlayers()) {
                    SmoothTeleport.teleport(player, player.getLocation().add(0, 1, 0));
                }

            }
        }.runTaskTimer(plugin, ticksPerSecond, ticksPerSecond);
    }

    private void worldEditMoveUP(int floor) {
        WorldEditPlugin worldEditPlugin = this.plugin.getOtherPlugins().getWorldEditPlugin();

        World world = BukkitAdapter.adapt(getBukkitWorld());
        BlockVector3 blockVector31 = BlockVector3.at(elevatorMovement.getLocationDOWN().getBlockX(), elevatorMovement.getLocationDOWN().getBlockY(), elevatorMovement.getLocationDOWN().getBlockZ());
        BlockVector3 blockVector32 = BlockVector3.at(elevatorMovement.getLocationUP().getBlockX(), elevatorMovement.getLocationUP().getBlockY(), elevatorMovement.getLocationUP().getBlockZ());

        CuboidRegion cuboidRegion = new CuboidRegion(world, blockVector31, blockVector32);

        BlockVector3 blockVector3DIR = BlockVector3.at(0, 1, 0);

        try (EditSession editSession = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(world, -1)) {
            editSession.moveCuboidRegion(cuboidRegion, blockVector3DIR, 1, false, null);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        elevatorMovement.moveUP();
        locationUpPLUS.add(0, 1, 0);
        locationDownPLUS.add(0, 1, 0);
    }

    private void worldEditMoveDOWN(int floor) {
        WorldEditPlugin worldEditPlugin = this.plugin.getOtherPlugins().getWorldEditPlugin();

        World world = BukkitAdapter.adapt(getBukkitWorld());
        BlockVector3 blockVector31 = BlockVector3.at(elevatorMovement.getLocationDOWN().getBlockX(), elevatorMovement.getLocationDOWN().getBlockY(), elevatorMovement.getLocationDOWN().getBlockZ());
        BlockVector3 blockVector32 = BlockVector3.at(elevatorMovement.getLocationUP().getBlockX(), elevatorMovement.getLocationUP().getBlockY(), elevatorMovement.getLocationUP().getBlockZ());

        CuboidRegion cuboidRegion = new CuboidRegion(world, blockVector31, blockVector32);

        BlockVector3 blockVector3DIR = BlockVector3.at(0, -1, 0);

        try (EditSession editSession = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(world, -1)) {
            editSession.moveCuboidRegion(cuboidRegion, blockVector3DIR, 1, false, null);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        elevatorMovement.moveDOWN();
        locationUpPLUS.subtract(0, 1, 0);
        locationDownPLUS.subtract(0, 1, 0);
    }

    public void emergencyStop() {
        this.isEmergencyStop = true;
    }

    private void floorDone(FloorObject floorObject, ElevatorStatus elevatorStatus) {
        Map<Location, Material> oldBlocks = new HashMap<>();

        for (Location location : floorObject.getDoorList()) {
            oldBlocks.put(location, location.getBlock().getType());
            location.getBlock().setType(Material.REDSTONE_BLOCK);
        }

        Material oldBlock = floorObject.getMainDoor().getBlock().getType();
        oldBlocks.put(floorObject.getMainDoor(), oldBlock);
        floorObject.getMainDoor().getBlock().setType(Material.REDSTONE_BLOCK);

        //SIGNS
        if (elevatorStatus == ElevatorStatus.UP) {
            for (SignObject signObject : floorObject.getSignList()) signObject.doUpArrow();
        } else if (elevatorStatus == ElevatorStatus.DOWN) {
            for (SignObject signObject : floorObject.getSignList()) signObject.doDownArrow();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (SignObject signObject : floorObject.getSignList()) signObject.clearSign();

                oldBlocks.forEach((k, v) -> k.getBlock().setType(v));
                oldBlocks.clear();
            }
        }.runTaskLater(plugin, doorHolderTicksPerSecond);
    }

    private void doFloorIdle() {
        isIdling = true;
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> isIdling = false, elevatorWaiterTicksPerSecond);
    }

    private void calculateFloorBuffer(int floor, boolean isUp) {
        if (isUp) {
            for (int num = this.elevatorMovement.getFloor(); num < floor; num++) {
                if (num != this.elevatorMovement.getFloor()) {
                    floorBuffer.add(num);
                }
            }
        } else {
            for (int num = this.elevatorMovement.getFloor(); num > floor; num--) {
                if (num != this.elevatorMovement.getFloor()) {
                    floorBuffer.add(num);
                }
            }
        }
    }

    private void reCalculateFloorBuffer(boolean goUp) {
        Integer peek = this.floorBuffer.peek();
        if (peek == null) return;

        if (goUp) {
            if (this.elevatorMovement.getAtDoor().getY() >= getFloor(peek).getMainDoor().getY())
                this.floorBuffer.remove();
        } else {
            if (this.elevatorMovement.getAtDoor().getY() <= getFloor(peek).getMainDoor().getY())
                this.floorBuffer.remove();
        }
    }

    private void setupFloorQueue() {
        //Don't run if it's running already
        if (isFloorQueueGoing) {
            return;
        }
        isFloorQueueGoing = true;

        //Checks every 2 seconds to see if the elevator isn't running or idling if not then go to floor.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isGoing && !isIdling && !floorQueueBuffer.isEmpty()) {
                    FloorQueueObject floorQueueObject = floorQueueBuffer.peek();
                    goToFloor(floorQueueObject.getFloorNumber(), floorQueueObject.getElevatorStatus());
                    floorQueueBuffer.remove();
                } else if (floorQueueBuffer.isEmpty()) {
                    isFloorQueueGoing = false;
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 40L, 40L);
    }

    private void arrivalChime(Location location) {
        getBukkitWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 10F, 1.8F);
    }

    private void passingChime(Location location) {
        getBukkitWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 10F, 1.3F);
    }

    public void addFloor(FloorObject floorObject) {
        if (floorObject.getFloor() >= 1) {
            this.topFloor++;
        } else if (floorObject.getFloor() < 0) {
            this.topBottomFloor--;
        } else if (floorObject.getFloor() == 0) {
            this.floorsMap.remove(floorObject.getFloor());
        }

        this.floorsMap.putIfAbsent(floorObject.getFloor(), floorObject);
    }

    public FloorObject getFloor(int floor) {
        if (this.floorsMap.get(floor) != null) {
            return this.floorsMap.get(floor);
        }
        return null;
    }

    private FloorObject getFloor(FloorQueueObject floorQueueObject) {
        if (this.floorsMap.get(floorQueueObject.getFloorNumber()) != null) {
            return this.floorsMap.get(floorQueueObject.getFloorNumber());
        }
        return null;
    }

    public void removeFloor(int floor) {
        floorsMap.remove(floor);
    }

    public String listAllFloors() {
        Set<Integer> homeSet = this.floorsMap.keySet();
        Integer[] integers = homeSet.toArray(new Integer[0]);
        Arrays.sort(integers);
        return Arrays.toString(integers);
    }

    public Integer[] listAllFloorsInt() {
        Set<Integer> homeSet = this.floorsMap.keySet();
        Integer[] integers = homeSet.toArray(new Integer[0]);
        Arrays.sort(integers);
        return integers;
    }

    public void clickMessageGoto(Player player) {
        String messageD = "- Click a Floor to take the elevator to. -";

        List<BaseComponent[]> componentBuilders = new ArrayList<>();
        for (Integer integer : listAllFloorsInt()) {
            componentBuilders.add(new ComponentBuilder(String.valueOf(integer)).color(ChatColor.GOLD).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/elevator call " + elevatorName.toLowerCase() + " " + integer)).create());
        }

        ComponentBuilder componentBuilder = new ComponentBuilder(messageD).color(ChatColor.YELLOW).bold(true).append("\n");
        for (BaseComponent[] builder : componentBuilders) {
            componentBuilder.append(" ");
            componentBuilder.append(builder);
        }

        player.spigot().sendMessage(componentBuilder.create());
    }

    public org.bukkit.World getBukkitWorld() {
        return Bukkit.getServer().getWorld(this.world);
    }

    //GETTERS AND SETTERS
    public String getElevatorName() {
        return elevatorName;
    }

    public void setElevatorName(String elevatorName) {
        this.elevatorName = elevatorName;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Location getLocationDownPLUS() {
        return locationDownPLUS;
    }

    public void setLocationDownPLUS(Location locationDownPLUS) {
        this.locationDownPLUS = locationDownPLUS;
    }

    public Location getLocationUpPLUS() {
        return locationUpPLUS;
    }

    public void setLocationUpPLUS(Location locationUpPLUS) {
        this.locationUpPLUS = locationUpPLUS;
    }

    public Map<Integer, FloorObject> getFloorsMap() {
        return floorsMap;
    }

    public void setFloorsMap(Map<Integer, FloorObject> floorsMap) {
        this.floorsMap = floorsMap;
    }

    public long getTicksPerSecond() {
        return ticksPerSecond;
    }

    public long getDoorHolderTicksPerSecond() {
        return doorHolderTicksPerSecond;
    }

    public long getElevatorWaiterTicksPerSecond() {
        return elevatorWaiterTicksPerSecond;
    }

    public Main getPlugin() {
        return plugin;
    }

    public void setPlugin(Main plugin) {
        this.plugin = plugin;
    }

    public boolean isGoing() {
        return isGoing;
    }

    public void setGoing(boolean going) {
        isGoing = going;
    }

    public boolean isFloorQueueGoing() {
        return isFloorQueueGoing;
    }

    public void setFloorQueueGoing(boolean floorQueueGoing) {
        isFloorQueueGoing = floorQueueGoing;
    }

    public boolean isIdling() {
        return isIdling;
    }

    public void setIdling(boolean idling) {
        isIdling = idling;
    }

    public boolean isEmergencyStop() {
        return isEmergencyStop;
    }

    public void setEmergencyStop(boolean emergencyStop) {
        isEmergencyStop = emergencyStop;
    }

    public int getTopFloor() {
        return topFloor;
    }

    public void setTopFloor(int topFloor) {
        this.topFloor = topFloor;
    }

    public int getTopBottomFloor() {
        return topBottomFloor;
    }

    public void setTopBottomFloor(int topBottomFloor) {
        this.topBottomFloor = topBottomFloor;
    }

    public Queue<FloorQueueObject> getFloorQueueBuffer() {
        return floorQueueBuffer;
    }

    public void setFloorQueueBuffer(Queue<FloorQueueObject> floorQueueBuffer) {
        this.floorQueueBuffer = floorQueueBuffer;
    }

    public Queue<Integer> getFloorBuffer() {
        return floorBuffer;
    }

    public void setFloorBuffer(Queue<Integer> floorBuffer) {
        this.floorBuffer = floorBuffer;
    }

    public ElevatorMovement getElevatorMovement() {
        return elevatorMovement;
    }

    public void setElevatorMovement(ElevatorMovement elevatorMovement) {
        this.elevatorMovement = elevatorMovement;
    }

    public StopBy getStopBy() {
        return stopBy;
    }

    public void setStopBy(StopBy stopBy) {
        this.stopBy = stopBy;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", elevatorName);
        map.put("world", world);
        map.put("shaft", elevatorMovement);
        map.put("shaftPlus", SimpleMath.toBoundingBox(locationDownPLUS.toVector(), locationUpPLUS.toVector()));
        return map;
    }

    public static ElevatorObject deserialize(Map<String, Object> map) {
        return new ElevatorObject(true, Main.getPlugin(), (String) map.get("world"), (String) map.get("name"), (ElevatorMovement) map.get("shaft"), (BoundingBox) map.get("shaftPlus"));
    }

    //JAVA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElevatorObject that = (ElevatorObject) o;
        return ticksPerSecond == that.ticksPerSecond &&
                doorHolderTicksPerSecond == that.doorHolderTicksPerSecond &&
                elevatorWaiterTicksPerSecond == that.elevatorWaiterTicksPerSecond &&
                isGoing == that.isGoing &&
                isFloorQueueGoing == that.isFloorQueueGoing &&
                isIdling == that.isIdling &&
                isEmergencyStop == that.isEmergencyStop &&
                topFloor == that.topFloor &&
                topBottomFloor == that.topBottomFloor &&
                Objects.equals(elevatorName, that.elevatorName) &&
                Objects.equals(world, that.world) &&
                Objects.equals(elevatorMovement, that.elevatorMovement) &&
                Objects.equals(locationDownPLUS, that.locationDownPLUS) &&
                Objects.equals(locationUpPLUS, that.locationUpPLUS) &&
                Objects.equals(floorsMap, that.floorsMap) &&
                Objects.equals(plugin, that.plugin) &&
                Objects.equals(floorQueueBuffer, that.floorQueueBuffer) &&
                Objects.equals(floorBuffer, that.floorBuffer) &&
                Objects.equals(stopBy, that.stopBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elevatorName, world, elevatorMovement, locationDownPLUS, locationUpPLUS, floorsMap, ticksPerSecond, doorHolderTicksPerSecond, elevatorWaiterTicksPerSecond, plugin, isGoing, isFloorQueueGoing, isIdling, isEmergencyStop, topFloor, topBottomFloor, floorQueueBuffer, floorBuffer, stopBy);
    }

    @Override
    public String toString() {
        return "ElevatorObject{" +
                "elevatorName='" + elevatorName + '\'' +
                ", world='" + world + '\'' +
                ", elevatorMovement=" + elevatorMovement.toString() +
                ", locationDownPLUS=" + locationDownPLUS +
                ", locationUpPLUS=" + locationUpPLUS +
                ", floorsMap=" + floorsMap +
                ", ticksPerSecond=" + ticksPerSecond +
                ", doorHolderTicksPerSecond=" + doorHolderTicksPerSecond +
                ", elevatorWaiterTicksPerSecond=" + elevatorWaiterTicksPerSecond +
                ", plugin=" + plugin +
                ", isGoing=" + isGoing +
                ", isFloorQueueGoing=" + isFloorQueueGoing +
                ", isIdling=" + isIdling +
                ", isEmergencyStop=" + isEmergencyStop +
                ", topFloor=" + topFloor +
                ", topBottomFloor=" + topBottomFloor +
                ", floorQueueBuffer=" + floorQueueBuffer +
                ", floorBuffer=" + floorBuffer +
                ", stopBy=" + stopBy +
                '}';
    }
}
