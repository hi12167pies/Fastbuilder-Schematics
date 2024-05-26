package cf.pies.FastbuilderSchematics;

import cf.pies.FastbuilderSchematics.Data.FBIdentifier;
import cf.pies.FastbuilderSchematics.Data.ModeData;
import cf.pies.FastbuilderSchematics.Data.ModeType;
import org.bukkit.Location;
import pies.FastbuilderAPI.Arena.ArenaManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SchematicManager implements ArenaManager {

    public static int MAPS = 20;
    public static HashMap<String, ModeData> modeData = new HashMap<>();

    public ModeData getModeData(String mode) {
        return modeData.get(mode);
    }


    public SchematicManager() {
        for (String mode : modeData.keySet()) {
            for (int i = 0; i < MAPS; i++) {
                this.cachedMaps.add(this.getIdentifier(mode, ""+i));
            }
        }
    }

    public double inclinedValue(FBIdentifier identifier) {
        return identifier.data.type == ModeType.INCLINED ? identifier.data.startIslandLength + identifier.data.distance : 0;
    }

    public Location getEndIsland(String stringIdentifier) {
        FBIdentifier identifier = this.decodeIdentifier(stringIdentifier);
        // To make inclined on the left use + instead of - with inclinedValue
        return identifier.data.spawn.clone().add(
                (identifier.data.islandSpacing * -identifier.id) - this.inclinedValue(identifier),
                identifier.data.distanceUp,
                identifier.data.startIslandLength + identifier.data.distance + identifier.data.zOffset);
    }


    @Override
    public Location getpos1(String stringIdentifier) {
        FBIdentifier identifier = this.decodeIdentifier(stringIdentifier);
        return identifier.data.pos1.clone().add(identifier.data.islandSpacing * -identifier.id, 0, identifier.data.zOffset);
    }

    @Override
    public Location getpos2(String stringIdentifier) {
        FBIdentifier identifier = this.decodeIdentifier(stringIdentifier);
        // To make inclined on the left use + instead of - with inclinedValue
        return identifier.data.pos2.clone().add((identifier.data.islandSpacing * -identifier.id) - this.inclinedValue(identifier),
                identifier.data.distanceUp,
                identifier.data.startIslandLength + identifier.data.endIslandLength + identifier.data.distance + identifier.data.zOffset);
    }

    @Override
    public Location getSpawn(String stringIdentifier) {
        FBIdentifier identifier = this.decodeIdentifier(stringIdentifier);
        Location loc = identifier.data.spawn.clone().add(
                identifier.data.islandSpacing * -identifier.id, 0, identifier.data.zOffset);
        if (identifier.data.type == ModeType.INCLINED) {
            loc.setYaw(45);
        }
        return loc;
    }


    @Override
    public boolean mapExist(String stringIdentifier) {
        FBIdentifier identifier = this.decodeIdentifier(stringIdentifier);
        return modeData.containsKey(identifier.mode)
                && identifier.id < MAPS
                && identifier.id > 0;
    }

    @Override
    public String getIdentifier(String mode, String island) {
        return mode + "-" + island;
    }

    @Override
    public Set<String> getAllMaps() {
        return cachedMaps;
    }

    @Override
    public String getModeFromIdentifier(String s) {
        return s.split("-")[0];
    }

    @Override
    public String getMapFromIdentifier(String s) {
        return s.split("-")[1];
    }

    // These methods are not implemented as they are handled with the math from schematics.
    @Override
    public void setSpawn(String identifier, Location location) {}
    @Override
    public void setpos1(String identifier, Location location) {}
    @Override
    public void setpos2(String identifier, Location location) {}
    @Override
    public void deleteMap(String identifier) {}

    public HashSet<String> cachedMaps = new HashSet<>();
    public FBIdentifier decodeIdentifier(String identifier) {
        String[] datas = identifier.split("-");

        FBIdentifier iden = new FBIdentifier();

        iden.mode = datas[0];
        iden.id = Integer.parseInt(datas[1]);
        iden.data = this.getModeData(iden.mode);

        return iden;
    }
}
