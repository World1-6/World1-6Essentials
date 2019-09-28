package World16FireAlarms.Screen;

import World16.Main.Main;
import World16FireAlarms.Objects.FireAlarmSignMenu;
import World16FireAlarms.interfaces.IFireAlarm;
import World16FireAlarms.interfaces.IScreenTech;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("IScreenTech")
public class FireAlarmSignScreen implements IScreenTech, ConfigurationSerializable {

    private Main plugin;

    private FireAlarmSignMenu currentMenu;

    public FireAlarmSignScreen(Main plugin, FireAlarmSignMenu fireAlarmSignMenu) {
        this.plugin = plugin;

        this.currentMenu = fireAlarmSignMenu;
    }

    public boolean onLine(FireAlarmScreen fireAlarmScreen, IFireAlarm iFireAlarm, Player player, Sign sign, int line) {

        //Loads for the first time.
        if (this.currentMenu == FireAlarmSignMenu.OFF) {
            loadFirstTime(fireAlarmScreen, sign);
            //Settings
        } else if (this.currentMenu == FireAlarmSignMenu.MAIN_MENU && line == 1) {
            settings_menu(fireAlarmScreen, sign);
        } else if (this.currentMenu == FireAlarmSignMenu.SETTINGS_MENU && line == 0)
            backReverse(currentMenu, fireAlarmScreen, sign, line);

        return true;
    }

    private void backReverse(FireAlarmSignMenu menu, FireAlarmScreen fireAlarmScreen, Sign sign, int line) {
        switch (menu) {
            case SETTINGS_MENU:
                main_menu(fireAlarmScreen, sign);
        }
    }

    private void resetSign(FireAlarmScreen fireAlarmScreen, Sign sign, boolean backToMainMenu) {
        sign.setLine(0, "");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
        fireAlarmScreen.updateSign(sign);

        if (backToMainMenu) main_menu(fireAlarmScreen, sign);
    }

    private void loadFirstTime(FireAlarmScreen fireAlarmScreen, Sign sign) {
        this.currentMenu = FireAlarmSignMenu.WAITING;

        sign.setLine(0, "Fire Alarm 0.1V");
        sign.setLine(1, "Loading data...");
        sign.setLine(2, "Please wait...");
        sign.setLine(3, "");
        fireAlarmScreen.updateSign(sign);

        new BukkitRunnable() {
            @Override
            public void run() {
                resetSign(fireAlarmScreen, sign, true);
            }
        }.runTaskLater(this.plugin, 100L);
    }

    private void main_menu(FireAlarmScreen fireAlarmScreen, Sign sign) {
        this.currentMenu = FireAlarmSignMenu.MAIN_MENU;
        sign.setLine(0, "Fire Alarm V.01");
        sign.setLine(1, "-Settings");
        sign.setLine(2, "");
        sign.setLine(3, "");
        fireAlarmScreen.setMin(1);
        fireAlarmScreen.setLine(1);
        fireAlarmScreen.updateSign(sign);
    }

    private void settings_menu(FireAlarmScreen fireAlarmScreen, Sign sign) {
        this.currentMenu = FireAlarmSignMenu.SETTINGS_MENU;
        sign.setLine(0, "Settings/MENU");
        sign.setLine(1, "-Test Fire Alarm");
        sign.setLine(2, "");
        sign.setLine(3, "");
        fireAlarmScreen.setMin(0);
        fireAlarmScreen.setLine(0);
        fireAlarmScreen.updateSign(sign);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("CurrentMenu", this.currentMenu.toString());
        return map;
    }

    public static FireAlarmSignScreen deserialize(Map<String, Object> map) {
        return new FireAlarmSignScreen(Main.getPlugin(), FireAlarmSignMenu.valueOf((String) map.get("CurrentMenu")));
    }
}
