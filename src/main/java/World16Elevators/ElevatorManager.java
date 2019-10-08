package World16Elevators;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import World16.Utils.Translate;
import World16Elevators.Objects.ElevatorObject;
import World16Elevators.Objects.FloorObject;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class ElevatorManager {

    private Main plugin;

    private CustomYmlManager elevatorsYml;

    private boolean on;

    private Map<String, ElevatorObject> elevatorObjectMap;

    public ElevatorManager(Main plugin, CustomConfigManager customConfigManager, boolean on) {
        this.on = on;
        this.plugin = plugin;
        this.elevatorObjectMap = this.plugin.getSetListMap().getElevatorObjectMap();
        this.elevatorsYml = customConfigManager.getElevatorsYml();
    }

    public void loadAllElevators() {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        ConfigurationSection cs = this.elevatorsYml.getConfig().getConfigurationSection("Elevators");

        //Only runs when elevator.yml is first being created.
        if (cs == null) {
            this.elevatorsYml.getConfig().createSection("Elevators");
            this.elevatorsYml.saveConfigSilent();
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[ElevatorManager]&r&6 Elevators section has been created."));
            return;
        }

        //For each elevator do.
        for (String elevator : cs.getKeys(false)) {
            ConfigurationSection elevatorConfig = cs.getConfigurationSection(elevator);
            ElevatorObject elevatorObject = (ElevatorObject) elevatorConfig.get("ElevatorObject");

            //For each elevator floor do.
            ConfigurationSection elevatorFloors = elevatorConfig.getConfigurationSection("Floors");
            if (elevatorFloors != null) {
                for (String floorNumber : elevatorFloors.getKeys(false)) {
                    elevatorObject.addFloor((FloorObject) elevatorFloors.get(floorNumber));
                }

            }
            elevatorObjectMap.putIfAbsent(elevator, elevatorObject);
        }
    }

    public void saveAllElevators() {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        //For each elevator do.
        for (Map.Entry<String, ElevatorObject> entry : elevatorObjectMap.entrySet()) {
            String k = entry.getKey();
            ElevatorObject v = entry.getValue();

            String elevatorLocation = "Elevators" + "." + k.toLowerCase();

            ConfigurationSection elevator = this.elevatorsYml.getConfig().getConfigurationSection(elevatorLocation);
            if (elevator == null) {
                elevator = this.elevatorsYml.getConfig().createSection(elevatorLocation);
                this.elevatorsYml.saveConfigSilent();
            }

            elevator.set("ElevatorObject", v);

            //Floors
            ConfigurationSection elevatorFloors = elevator.getConfigurationSection("Floors");
            if (elevatorFloors == null) {
                elevatorFloors = elevator.createSection("Floors");
                this.elevatorsYml.saveConfigSilent();
            }

            //For each floor do.
            for (Map.Entry<Integer, FloorObject> e : v.getFloorsMap().entrySet()) {
                Integer k2 = e.getKey();
                FloorObject v2 = e.getValue();
                elevatorFloors.set(String.valueOf(k2), v2);
            }
            this.elevatorsYml.saveConfigSilent();
        }
        this.elevatorsYml.saveConfigSilent();
    }

    public void deleteElevator(String name) {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        if (elevatorObjectMap.get(name.toLowerCase()) != null) {
            elevatorObjectMap.remove(name.toLowerCase());

            ConfigurationSection elevators = this.elevatorsYml.getConfig().getConfigurationSection("Elevators");
            if (elevators != null)
                elevators.set(name.toLowerCase(), null);
            this.elevatorsYml.saveConfigSilent();
        }
    }

    public void deleteFloorOfElevator(String elevatorName, int floorNum) {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        if (elevatorObjectMap.get(elevatorName.toLowerCase()) != null) {
            elevatorObjectMap.get(elevatorName.toLowerCase()).getFloorsMap().remove(floorNum);
            ConfigurationSection elevatorFloors = this.elevatorsYml.getConfig().getConfigurationSection("Elevators." + elevatorName.toLowerCase() + ".Floors");
            if (elevatorFloors != null) {
                elevatorFloors.set(String.valueOf(floorNum), null);
                this.elevatorsYml.saveConfigSilent();
            }
        }
    }
}
