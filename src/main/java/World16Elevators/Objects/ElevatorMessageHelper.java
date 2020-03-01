package World16Elevators.Objects;

import World16.Main.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ElevatorMessageHelper {

    private Main plugin;
    private ElevatorObject elevatorObject;

    private boolean isRunning;
    private List<UUID> players;
    private int counter;

    public ElevatorMessageHelper(Main plugin, ElevatorObject elevatorObject) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.elevatorObject = elevatorObject;
    }

    private void messageSetup() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isRunning) {
                    this.cancel();
                    return;
                }
                counter++;

                //When the player isn't in the elevator remove them from the list.
                Iterator<UUID> iterator = players.iterator();
                while (iterator.hasNext()) {
                    UUID uuid = iterator.next();
                    ArrayList<UUID> uuidArrayList = (ArrayList<UUID>) elevatorObject.getPlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
                    if (!uuidArrayList.contains(uuid)) iterator.remove();
                }

                //20 seconds -> After 20 seconds check the elevator if no one is in it and it's not running or idling.
                if (counter >= 20) {
                    if (players.isEmpty() && !elevatorObject.isGoing() && !elevatorObject.isIdling()) {
                        stop();
                        return;
//                    } else if (!players.isEmpty() && !elevatorObject.isGoing() && !elevatorObject.isIdling()) {
//                        elevatorObject.getPlayers().forEach(player -> player.sendMessage(Translate.chat("&cYou have been in the elevator for 20 seconds or higher without the elevator moving &eSending you back to floor 0.")));
//                        elevatorObject.goToFloor(0, ElevatorStatus.DONT_KNOW);
//                        stop();
//                        return;
                    }
                }

                for (Player player : elevatorObject.getPlayers()) {
                    //The player already got the message.
                    if (players.contains(player.getUniqueId())) {
                        return;
                    }

                    elevatorObject.clickMessageGoto(player);
                    players.add(player.getUniqueId());
                }
            }
        }.runTaskTimer(plugin, 1, 20);
    }

    public void start() {
        if (isRunning) return;
        this.counter = 0;
        this.isRunning = true;
        messageSetup();
    }

    public void stop() {
        this.isRunning = false;
    }
}