package World16FireAlarms.interfaces;

import World16FireAlarms.Screen.FireAlarmScreen;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface IScreenTech {

    boolean onLine(FireAlarmScreen fireAlarmScreen, Player player, Sign sign, int line);
}