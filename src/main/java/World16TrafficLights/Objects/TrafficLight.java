package World16TrafficLights.Objects;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficLight implements ConfigurationSerializable {

    private Location location;

    public TrafficLight(Location location) {
        this.location = location;
    }

    public boolean doLight(TrafficLightState trafficLightState) {
        switch (trafficLightState) {
            case GREEN:
                return Green();
            case YELLOW:
                return Yellow();
            case RED:
                return Red();
        }
        return false;
    }

    public boolean Green() {
        Banner banner = (Banner) location.getBlock().getState();
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.LIME, PatternType.TRIANGLE_BOTTOM));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        banner.setPatterns(patterns);
        banner.update();
        return true;
    }

    public boolean Yellow() {
        Banner banner = (Banner) location.getBlock().getState();
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        banner.setPatterns(patterns);
        banner.update();
        return true;
    }

    public boolean Red() {
        Banner banner = (Banner) location.getBlock().getState();
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.RED, PatternType.TRIANGLE_TOP));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        banner.setPatterns(patterns);
        banner.update();
        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Location", this.location);
        return map;
    }

    public static TrafficLight deserialize(Map<String, Object> map) {
        return new TrafficLight((Location) map.get("Location"));
    }
}
