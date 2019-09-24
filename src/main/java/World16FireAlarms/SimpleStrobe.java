package World16FireAlarms;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class SimpleStrobe implements IStrobe{

    private String name;
    private Location location;
    private Zone zone;

    public SimpleStrobe(Location block,String name, Zone zone){
        this.location = block;
        this.zone = zone;
        this.name = name;
    }

    public SimpleStrobe(Block block,String name, Zone zone){
        this(block.getLocation(), name, zone);
    }

    public void on(){

    }

    public void off(){

    }

    public void sound(){

    }

    public Location getLocation(){
        return this.location;
    }

    public String getName(){
        return this.name;
    }

    public Zone getZone(){
        return this.zone;
    }
}