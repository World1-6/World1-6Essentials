package com.andrew121410.World16Elevators.Objects;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.SmoothTeleport;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ElevatorRunnable extends BukkitRunnable {

    private Main plugin;
    private ElevatorObject elevatorObject;

    private boolean goUP;
    private int floorNum;
    private FloorObject floorObject;
    private ElevatorStatus elevatorStatus;

    int counter;

    public ElevatorRunnable(Main plugin, ElevatorObject elevatorObject, boolean goUP, int floorNum, ElevatorStatus elevatorStatus, int counter) {
        this.plugin = plugin;
        this.elevatorObject = elevatorObject;
        this.goUP = goUP;
        this.floorNum = floorNum;
        this.floorObject = elevatorObject.getFloor(floorNum);
        this.elevatorStatus = elevatorStatus;
        this.counter = counter;
    }

    public ElevatorRunnable(Main plugin, ElevatorObject elevatorObject, boolean goUp, int floorNum, ElevatorStatus elevatorStatus) {
        this(plugin, elevatorObject, goUp, floorNum, elevatorStatus, (int) elevatorObject.getElevatorMovement().getTicksPerSecond());
    }

    @Override
    public void run() {
        if (elevatorObject.isIdling()) return;
        elevatorObject.reCalculateFloorBuffer(goUP);
        FloorObject stopByFloor = !elevatorObject.getStopBy().getStopByQueue().isEmpty() ? elevatorObject.getFloor(elevatorObject.getStopBy().getStopByQueue().peek()) : null;

//                Check's if at floor if so then stop the elvator.
        if (elevatorObject.getElevatorMovement().getAtDoor().getY() == floorObject.getMainDoor().getY()) {
            counter = 0;
            this.cancel();
            elevatorObject.floorStop(floorNum, floorObject, elevatorStatus);
            return;
        } else if (stopByFloor != null && elevatorObject.getElevatorMovement().getAtDoor().getY() == stopByFloor.getMainDoor().getY()) {
            elevatorObject.floorStop(floorNum, elevatorStatus, elevatorObject.getStopBy(), stopByFloor);
            return;
        }

//                Stop's the elevator if emergencyStop is on.
        if (elevatorObject.isEmergencyStop()) {
            elevatorObject.setIdling(false);
            elevatorObject.setGoing(false);
            elevatorObject.setEmergencyStop(false);
            this.cancel();
            return;
        }

        if (goUP) {
            elevatorObject.worldEditMoveUP();

            //TP THEM UP 1
            for (Player player : elevatorObject.getPlayers()) {
                SmoothTeleport.teleport(player, player.getLocation().add(0, 1, 0));
            }

            int x = elevatorObject.getElevatorMovement().getAtDoor().getBlockY();
            int z = floorObject.getMainDoor().getBlockY();
            x += 5;
            if (x >= z) {
                counter += 1;
            }

        } else {
            elevatorObject.worldEditMoveDOWN();

            //TP THEM DOWN 1
            for (Player player : elevatorObject.getPlayers()) {
                SmoothTeleport.teleport(player, player.getLocation().subtract(0, 1, 0));
            }

            int x = elevatorObject.getElevatorMovement().getAtDoor().getBlockY();
            int z = floorObject.getMainDoor().getBlockY();
            x -= 5;
            if (x <= z) {
                counter += 1;
            }
        }
        this.cancel();
        new ElevatorRunnable(plugin, elevatorObject, goUP, floorNum, elevatorStatus, counter).runTaskLater(plugin, counter);
    }
}
