package World16Elevators.Objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("BoundingBox")
public class BoundingBox implements ConfigurationSerializable {

    private Vector vectorDOWN;
    private Vector vectorUP;
    private Vector vectorDOWNMIN;
    private Vector vectorUPMAX;

    public BoundingBox(Vector vectorDOWN, Vector vectorUP) {
        this.vectorDOWN = vectorDOWN;
        this.vectorUP = vectorUP;

        this.vectorDOWNMIN = Vector.getMinimum(vectorDOWN, vectorUP);
        this.vectorUPMAX = Vector.getMaximum(vectorDOWN, vectorUP);
    }

    public boolean isInAABB(Vector vector) {
        return vector.isInAABB(vectorDOWNMIN, vectorUPMAX);
    }

    public Vector getMidpoint() {
        return vectorDOWN.getMidpoint(vectorUP);
    }

    public Vector getMidPointOnFloor() {
        Vector vector = getMidpoint();
        vector.setY(vectorDOWN.getY());
        return vector;
    }

    public BoundingBox add(Vector add) {
        vectorUP.add(add);
        vectorDOWN.add(add);

        vectorDOWNMIN = Vector.getMinimum(vectorDOWN, vectorUP);
        vectorUPMAX = Vector.getMaximum(vectorDOWN, vectorUP);
        return this;
    }

    public BoundingBox subtract(Vector subtract) {
        vectorUP.subtract(subtract);
        vectorDOWN.subtract(subtract);

        vectorDOWNMIN = Vector.getMinimum(vectorDOWN, vectorUP);
        vectorUPMAX = Vector.getMaximum(vectorDOWN, vectorUP);
        return this;
    }

    //GETTERS
    public Vector getVectorDOWN() {
        return vectorDOWN;
    }

    public Vector getVectorUP() {
        return vectorUP;
    }

    public Vector getVectorDOWNMIN() {
        return vectorDOWNMIN;
    }

    public Vector getVectorUPMAX() {
        return vectorUPMAX;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("vectorDOWN", vectorDOWN);
        map.put("vectorUP", vectorUP);
        return map;
    }

    public static BoundingBox deserialize(Map<String, Object> map) {
        return new BoundingBox((Vector) map.get("vectorDOWN"), (Vector) map.get("vectorUP"));
    }
}