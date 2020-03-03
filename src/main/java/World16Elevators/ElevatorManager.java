package World16Elevators;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import World16.Utils.Translate;
import World16Elevators.Objects.ElevatorController;
import World16Elevators.Objects.ElevatorObject;
import World16Elevators.Objects.FloorObject;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class ElevatorManager {

    private Main plugin;

    private Map<String, ElevatorController> elevatorObjectMap;

    private CustomYmlManager elevatorsYml;
    private boolean on;

    public ElevatorManager(Main plugin, CustomConfigManager customConfigManager, boolean on) {
        this.on = on;
        this.plugin = plugin;
        this.elevatorObjectMap = this.plugin.getSetListMap().getElevatorObjectMap();
        this.elevatorsYml = customConfigManager.getElevatorsYml();
    }

    public void loadAllElevators() {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        //This runs when elevator.yml is first created.
        ConfigurationSection elevatorControllersSection = this.elevatorsYml.getConfig().getConfigurationSection("ElevatorControllers");
        if (elevatorControllersSection == null) {
            elevatorControllersSection = this.elevatorsYml.getConfig().createSection("ElevatorControllers");
            this.elevatorsYml.saveConfigSilent();
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[ElevatorManager]&r&6 ElevatorControllers section has been created."));
            return;
        }

        for (String elevatorControllerName : elevatorControllersSection.getKeys(false)) {
            ConfigurationSection elevatorControllerSection = elevatorControllersSection.getConfigurationSection(elevatorControllerName);
            ElevatorController elevatorController = (ElevatorController) elevatorControllerSection.get("ElevatorController");

            //Elevators
            ConfigurationSection elevatorsSection = elevatorControllerSection.getConfigurationSection("Elevators");
            for (String elevatorName : elevatorsSection.getKeys(false)) {
                //Elevator
                ConfigurationSection elevatorSection = elevatorsSection.getConfigurationSection(elevatorName);
                ElevatorObject elevatorObject = (ElevatorObject) elevatorSection.get("ElevatorObject");

                //Floors
                ConfigurationSection elevatorFloors = elevatorSection.getConfigurationSection("Floors");
                for (String floorNumber : elevatorFloors.getKeys(false)) {
                    elevatorObject.addFloor((FloorObject) elevatorFloors.get(floorNumber));
                }
                elevatorController.registerElevator(elevatorName, elevatorObject);
            }
            this.elevatorObjectMap.put(elevatorControllerName.toLowerCase(), elevatorController);
        }
    }

    public void saveAllElevators() {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        ConfigurationSection elevatorControllersSection = this.elevatorsYml.getConfig().getConfigurationSection("ElevatorControllers");
        if (elevatorControllersSection == null) {
            elevatorControllersSection = this.elevatorsYml.getConfig().createSection("ElevatorControllers");
            this.elevatorsYml.saveConfigSilent();
        }

        //For each elevator controller.
        for (Map.Entry<String, ElevatorController> mapEntry : this.elevatorObjectMap.entrySet()) {
            String controllerName = mapEntry.getKey();
            ElevatorController elevatorController = mapEntry.getValue();

            //Elevator controller.
            ConfigurationSection elevatorControllerSection = elevatorControllersSection.getConfigurationSection(controllerName);
            if (elevatorControllerSection == null) {
                elevatorControllerSection = elevatorControllersSection.createSection(controllerName);
                this.elevatorsYml.saveConfigSilent();
            }

            elevatorControllerSection.set("ElevatorController", elevatorController);

            //Elevators
            String elevatorsLocation = "Elevators";
            ConfigurationSection elevatorsSection = elevatorControllerSection.getConfigurationSection(elevatorsLocation);
            if (elevatorsSection == null) {
                elevatorsSection = elevatorControllerSection.createSection(elevatorsLocation);
                this.elevatorsYml.saveConfigSilent();
            }

            //For each elevator.
            for (Map.Entry<String, ElevatorObject> entry : elevatorController.getElevatorsMap().entrySet()) {
                String elevatorName = entry.getKey();
                ElevatorObject elevatorObject = entry.getValue();

                //Elevator
                ConfigurationSection elevatorSection = elevatorsSection.getConfigurationSection(elevatorName);
                if (elevatorSection == null) {
                    elevatorSection = elevatorsSection.createSection(elevatorName);
                    this.elevatorsYml.saveConfigSilent();
                }

                elevatorSection.set("ElevatorObject", elevatorObject);

                //Floors
                ConfigurationSection elevatorFloors = elevatorSection.getConfigurationSection("Floors");
                if (elevatorFloors == null) {
                    elevatorFloors = elevatorSection.createSection("Floors");
                    this.elevatorsYml.saveConfigSilent();
                }

                //For each floor do.
                for (Map.Entry<Integer, FloorObject> e : elevatorObject.getFloorsMap().entrySet()) {
                    Integer k2 = e.getKey();
                    FloorObject v2 = e.getValue();
                    elevatorFloors.set(String.valueOf(k2), v2);
                }
                this.elevatorsYml.saveConfigSilent();
            }
            this.elevatorsYml.saveConfigSilent();
        }
    }

    public void deleteElevatorController(String name) {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        this.elevatorObjectMap.remove(name.toLowerCase());

        ConfigurationSection elevatorControllersSection = this.elevatorsYml.getConfig().getConfigurationSection("ElevatorControllers");
        if (elevatorControllersSection == null) return;

        elevatorControllersSection.set(name.toLowerCase(), null);
        this.elevatorsYml.saveConfigSilent();
    }

    public void deleteElevator(String elevatorControllerName, String elevatorName) {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        ElevatorController elevatorController = this.elevatorObjectMap.get(elevatorControllerName);
        if (elevatorController == null) return;
        elevatorController.getElevatorsMap().remove(elevatorName);

        ConfigurationSection elevatorControllersSection = this.elevatorsYml.getConfig().getConfigurationSection("ElevatorControllers");
        if (elevatorControllersSection == null) return;
        ConfigurationSection elevatorControllerSection = elevatorControllersSection.getConfigurationSection(elevatorControllerName.toLowerCase());
        if (elevatorControllerSection == null) return;
        ConfigurationSection elevatorsSection = elevatorControllerSection.getConfigurationSection("Elevators");
        if (elevatorsSection == null) return;

        elevatorsSection.set(elevatorName, null);
        this.elevatorsYml.saveConfigSilent();
    }

    public void deleteFloorOfElevator(String elevatorControllerName, String elevatorName, int floorNum) {
        if (!on || !this.plugin.getOtherPlugins().hasWorldEdit()) return;

        ElevatorController elevatorController = this.elevatorObjectMap.get(elevatorControllerName);
        if (elevatorController == null) return;
        ElevatorObject elevatorObject = elevatorController.getElevatorsMap().get(elevatorName);
        elevatorObject.deleteFloor(floorNum);

        ConfigurationSection elevatorControllersSection = this.elevatorsYml.getConfig().getConfigurationSection("ElevatorControllers");
        if (elevatorControllersSection == null) return;
        ConfigurationSection elevatorControllerSection = elevatorControllersSection.getConfigurationSection(elevatorControllerName.toLowerCase());
        if (elevatorControllerSection == null) return;
        ConfigurationSection elevatorsSection = elevatorControllerSection.getConfigurationSection("Elevators");
        if (elevatorsSection == null) return;
        ConfigurationSection elevatorSection = elevatorsSection.getConfigurationSection(elevatorName);
        if (elevatorSection == null) return;
        ConfigurationSection elevatorFloors = elevatorSection.getConfigurationSection("Floors");
        if (elevatorFloors == null) return;

        elevatorFloors.set(String.valueOf(floorNum), null);
        this.elevatorsYml.saveConfigSilent();
    }
}
