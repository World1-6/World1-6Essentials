package World16Elevators.Objects;

import World16.Utils.SignUtils;
import World16.Utils.Translate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("SignObject")
public class SignObject implements ConfigurationSerializable {

    private Location location;

    public SignObject(Location location) {
        this.location = location;
    }

    public boolean isSign() {
        if (location == null) {
            return false;
        }

        return location.getBlock().getType() == Material.OAK_WALL_SIGN || location.getBlock().getType() == Material.LEGACY_SIGN_POST;
    }

    public Sign getSign() {
        if (isSign()) {
            return (Sign) location.getBlock().getState();
        }
        return null;
    }

    public void doUpArrow() {
        if (!isSign()) {
            return;
        }

        Sign sign = getSign();
        String text = SignUtils.centerText("/\\", 16);
        String text1 = SignUtils.centerText("//\\\\", 16);
        sign.setLine(0, Translate.chat("&a&l" + text));
        sign.setLine(1, Translate.chat("&a&l" + text1));
        sign.update();
    }

    public void doDownArrow() {
        if (!isSign()) {
            return;
        }

        Sign sign = getSign();
        String text = SignUtils.centerText("\\\\//", 16);
        String text1 = SignUtils.centerText("\\/", 16);
        sign.setLine(2, Translate.chat("&c&l" + text));
        sign.setLine(3, Translate.chat("&c&l" + text1));
        sign.update();
    }

    public void clearSign() {
        if (!isSign()) {
            return;
        }

        Sign sign = getSign();
        sign.setLine(0, "");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Location", location);
        return map;
    }

    public static SignObject deserialize(Map<String, Object> map) {
        return new SignObject((Location) map.get("Location"));
    }
}