package World16.Utils;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static net.minecraft.server.v1_12_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT;
import static net.minecraft.server.v1_12_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT;

public class SmoothTeleport {

    private static final Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> TELEPORT_FLAGS = Collections.unmodifiableSet(EnumSet.of(X_ROT, Y_ROT));

    private static Field justTeleportedField;
    private static Field teleportPosField;
    private static Field lastPosXField;
    private static Field lastPosYField;
    private static Field lastPosZField;
    private static Field teleportAwaitField;
    private static Field AField;
    private static Field eField;

    static {
        try {
            justTeleportedField = getField("justTeleported");
            teleportPosField = getField("teleportPos");
            lastPosXField = getField("lastPosX");
            lastPosYField = getField("lastPosY");
            lastPosZField = getField("lastPosZ");
            teleportAwaitField = getField("teleportAwait");
            AField = getField("A");
            eField = getField("e");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static Field getField(String name) throws NoSuchFieldException {
        Field field = PlayerConnection.class.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static void teleport(Player player, Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        if (handle.activeContainer != handle.defaultContainer) handle.closeInventory();
        handle.setLocation(x, y, z, handle.yaw, handle.pitch);
        PlayerConnection connection = handle.playerConnection;
        int teleportAwait = 0;
        try {
            justTeleportedField.set(connection, true);
            teleportPosField.set(connection, new Vec3D(x, y, z));
            lastPosXField.set(connection, x);
            lastPosYField.set(connection, y);
            lastPosZField.set(connection, z);
            teleportAwait = teleportAwaitField.getInt(connection) + 1;
            if (teleportAwait == 2147483647) teleportAwait = 0;
            teleportAwaitField.set(connection, teleportAwait);
            AField.set(connection, eField.get(connection));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        connection.sendPacket(new PacketPlayOutPosition(x, y, z, 0, 0, TELEPORT_FLAGS, teleportAwait));
    }
}
