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
    LIST_MENU,
    STROBES_MENU;
    //...
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
        } else if (this.currentMenu == Menu.MAIN_MENU && line == 1) {

        }

        return true;
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
        sign.setLine(1, "-Lists");
        sign.setLine(2, "-Actvation Points");
        sign.setLine(3, "-");
        fireAlarmScreen.updateSign(sign);
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }
}
