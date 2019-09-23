package World16FireAlarms;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class SimpleStrobe implements IStrobe{

    private Location location;

    public SimpleStrobe(Location block){
        this.location = block;
    }

    public SimpleStrobe(Block block){
        this(block.getLocation());
    }

    public void on(){

    }

    public void off(){

    }

    public void sound(){

    }

    public Location getLocation(){
        return null;
    }
}