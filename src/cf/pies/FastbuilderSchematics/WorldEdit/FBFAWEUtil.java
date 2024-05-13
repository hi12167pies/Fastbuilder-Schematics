package cf.pies.FastbuilderSchematics.WorldEdit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.World;

public class FBFAWEUtil {
    public static Vector toVector(Location location) {
        return new Vector(location.getX(), location.getY(), location.getZ());
    }

    public static BukkitWorld toWorld(World world) {
        return new BukkitWorld(world);
    }
}
