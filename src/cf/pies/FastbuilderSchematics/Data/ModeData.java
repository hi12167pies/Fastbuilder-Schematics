package cf.pies.FastbuilderSchematics.Data;

import org.bukkit.Location;

public class ModeData {
    public ModeData(
            int startIslandLen, int endIslandLen,
            int spacing,
            int distance, int up,
            int zOffset,
            String schematicStart, String schematicEnd,
            ModeType type,
            Location spawn, Location pos1, Location pos2) {
        this.startIslandLength = startIslandLen;
        this.endIslandLength = endIslandLen;
        this.islandSpacing = spacing;
        this.distance = distance;
        this.distanceUp = up;
        this.zOffset = zOffset;
        this.schematicStart = schematicStart;
        this.schematicEnd = schematicEnd;
        this.type = type;
        this.spawn = spawn;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public final int startIslandLength;
    public final int endIslandLength;

    public final int islandSpacing;
    public final int distance;

    public final int distanceUp;
    public final int zOffset;

    public String schematicStart;
    public String schematicEnd;
    public ModeType type;
    public Location spawn;
    public Location pos1;
    public Location pos2;
}
