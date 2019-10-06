package World16Elevators;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import World16.Utils.Translate;
import World16Elevators.Objects.ElevatorObject;
import World16Elevators.Objects.FloorObject;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class ElevatorMain {

    private Main plugin;
    private CustomConfigManager customConfigManager;
    private CustomYmlManager eleYml;

    private Map<String, ElevatorObject> elevatorObjectMap;

    public ElevatorMain(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.elevatorObjectMap = this.plugin.getSetListMap().getElevatorObjectMap();
        this.eleYml = this.customConfigManager.getElevatorsYml();
    }

    public void loadAllElevators() {
        //Don't run if there's no WorldEdit.
        if (!this.plugin.getOtherPlugins().hasWorldEdit()) {
            return;
        }

        ConfigurationSection cs = this.eleYml.getConfig().getConfigurationSection("Elevators");
        if (cs == null) {
            this.eleYml.getConfig().createSection("Elevators");
            this.eleYml.saveConfigSilent();
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[ElevatorManager]&r&6 Elevators section has been created."));
            return;
        }

        for (String elevator : cs.getKeys(false)) {
            ConfigurationSection elevatorConfig = cs.getConfigurationSection(elevator);
            ElevatorObject elevatorObject = (ElevatorObject) elevatorConfig.get("ElevatorObject");

            //Floors
            ConfigurationSection elevatorFloors = elevatorConfig.getConfigurationSection("Floors");
            if (elevatorFloors != null) {
                for (String floorNumber : elevatorFloors.getKeys(false)) {
                    elevatorObject.addFloor((FloorObject) elevatorFloors.get(floorNumber));
                }

                //Just a hack since i don't want nothing static.
                elevatorObject.setPlugin(this.plugin);
            }
            elevatorObjectMap.putIfAbsent(elevator, elevatorObject);
        }
    }

    public void saveAllElevators() {
        //Don't run if there's no WorldEdit.
        if (!this.plugin.getOtherPlugins().hasWorldEdit()) {
            return;
        }

        for (Map.Entry<String, ElevatorObject> entry : elevatorObjectMap.entrySet()) {
            String k = entry.getKey();
            ElevatorObject v = entry.getValue();

            String elevatorLocation = "Elevators" + "." + k.toLowerCase();

            ConfigurationSection elevator = this.eleYml.getConfig().getConfigurationSection(elevatorLocation);
            if (elevator == null) {
                elevator = this.eleYml.getConfig().createSection(elevatorLocation);
                this.eleYml.saveConfigSilent();
            }

            elevator.set("ElevatorObject", v);

            ConfigurationSection elevatorFloors = elevator.getConfigurationSection("Floors");
            if (elevatorFloors == null) {
                elevatorFloors = elevator.createSection("Floors");
                this.eleYml.saveConfigSilent();
            }

            for (Map.Entry<Integer, FloorObject> e : v.getFloorsMap().entrySet()) {
                Integer k2 = e.getKey();
                FloorObject v2 = e.getValue();
                elevatorFloors.set(String.valueOf(k2), v2);
            }
            this.eleYml.saveConfigSilent();
        }
        this.eleYml.saveConfigSilent();
    }

    public void deleteElevator(String name) {
        //Don't run if there's no WorldEdit.
        if (!this.plugin.getOtherPlugins().hasWorldEdit()) {
            return;
        }

        if (elevatorObjectMap.get(name.toLowerCase()) != null) {
            elevatorObjectMap.remove(name.toLowerCase());
            ConfigurationSection elevator = this.eleYml.getConfig().getConfigurationSection("Elevators");
            elevator.set(name.toLowerCase(), null);
            this.eleYml.saveConfigSilent();
        }
    }

    public void deleteFloorOfElevator(String elevatorName, int floorNum) {
        //Don't run if there's no WorldEdit.
        if (!this.plugin.getOtherPlugins().hasWorldEdit()) {
            return;
        }

        if (elevatorObjectMap.get(elevatorName.toLowerCase()) != null) {
            elevatorObjectMap.get(elevatorName.toLowerCase()).getFloorsMap().remove(floorNum);
            ConfigurationSection elevatorFloors = this.eleYml.getConfig().getConfigurationSection("Elevators." + elevatorName.toLowerCase() + ".Floors");
            if (elevatorFloors != null) {
                elevatorFloors.set(String.valueOf(floorNum), null);
                this.eleYml.saveConfigSilent();
            }
        }
    }
}
