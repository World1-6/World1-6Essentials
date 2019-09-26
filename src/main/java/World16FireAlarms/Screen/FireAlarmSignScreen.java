package World16FireAlarms.Screen;

import World16.Main.Main;
import World16FireAlarms.interfaces.IScreenTech;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

enum Menu {
    WAITING,
    OFF,
    MAIN_MENU,
    //1
    SETTINGS_MENU;
}

public class FireAlarmSignScreen implements IScreenTech {

    private Main plugin;

    private Menu currentMenu;

    public FireAlarmSignScreen(Main plugin) {
        this.plugin = plugin;
        this.currentMenu = Menu.OFF;
    }

    public boolean onLine(FireAlarmScreen fireAlarmScreen, Player player, Sign sign, int line) {

        //Loads for the first time.
        if (this.currentMenu == Menu.OFF) {
            loadFirstTime(fireAlarmScreen, sign);
            //Settings
        } else if (this.currentMenu == Menu.MAIN_MENU && line == 1) {
            settings_menu(fireAlarmScreen, sign);
        } else if (this.currentMenu == Menu.SETTINGS_MENU && line == 0)
            backReverse(currentMenu, fireAlarmScreen, sign, line);

        return true;
    }

    private void backReverse(Menu menu, FireAlarmScreen fireAlarmScreen, Sign sign, int line) {
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
        this.currentMenu = Menu.WAITING;

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
        this.currentMenu = Menu.MAIN_MENU;
        sign.setLine(0, "Fire Alarm V.01");
        sign.setLine(1, "-Settings");
        sign.setLine(2, "");
        sign.setLine(3, "");
        fireAlarmScreen.setMin(1);
        fireAlarmScreen.setLine(1);
        fireAlarmScreen.updateSign(sign);
    }

    private void settings_menu(FireAlarmScreen fireAlarmScreen, Sign sign) {
        this.currentMenu = Menu.SETTINGS_MENU;
        sign.setLine(0, "Settings/MENU");
        sign.setLine(1, "-Test Fire Alarm");
        sign.setLine(2, "");
        sign.setLine(3, "");
        fireAlarmScreen.setMin(0);
        fireAlarmScreen.setLine(0);
        fireAlarmScreen.updateSign(sign);
    }
}
