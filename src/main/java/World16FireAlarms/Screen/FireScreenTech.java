package World16FireAlarms.Screen;

import World16.Main.Main;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

enum Menu{
    WAITING,
    OFF,
    MAIN_MENU,
    //1
    LIST_MENU,
    STROBES_MENU;
    //...
}

public class FireScreenTech implements IScreenTech{

    private Main plugin;

    private Menu currentMenu;

    public FireScreenTech(Main plugin) {
        this.plugin = plugin;
        this.currentMenu = Menu.OFF;
    }

    public boolean onLine(Sign sign, int line) {

        //Loads for the first time.
        if(this.currentMenu == Menu.OFF){
            loadFirstTime(sign);
        }

        return true;
    }

    private void resetSign(Sign sign, boolean backToMainMenu){
        sign.setLine(0, "");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();

        if(backToMainMenu) main_menu(sign);
    }

    private void loadFirstTime(Sign sign){
        this.currentMenu = Menu.WAITING;

        sign.setLine(0, "Fire Alarm 0.1V");
        sign.setLine(1, "Loading data...");
        sign.setLine(2, "Please wait...");
        sign.update();

          new BukkitRunnable(){
                @Override
                public void run() {
                    resetSign(sign, true);
                }
            }.runTaskLater(this.plugin, 100L);
    }

    private void main_menu(Sign sign){
            this.currentMenu = Menu.MAIN_MENU;
            sign.setLine(0, "--Fire Alarm V.01--");
            sign.setLine(1, "-Lists-");
            sign.setLine(2, "-Actvation Points-");
            sign.setLine(3, "-");
    }

}
