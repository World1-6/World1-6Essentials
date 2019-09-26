package World16FireAlarms.Screen;

import org.bukkit.block.Sign;

public interface IScreenTech {

    boolean onLine(FireAlarmScreen fireAlarmScreen, Sign sign, int line);
}