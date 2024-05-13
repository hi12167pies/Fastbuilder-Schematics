package cf.pies.FastbuilderSchematics;

import cf.pies.FastbuilderSchematics.WorldEdit.FBSchematicHandler;

public class FBSchematicAPI {
    public static void changeMap(FBSchematicHandler spawn, FBSchematicHandler end, String identifier, boolean loadEnd) {
        spawn.load(Main.arenaManager.getSpawn(identifier));
        if (loadEnd) {
            end.load(Main.arenaManager.getEndIsland(identifier));
        }
    }

    public static FBSchematicHandler getDefaultSpawnSchematic(String mode) {
        return Main.getPlugin(Main.class).spawnSchematic.get(mode);
    }

    public static FBSchematicHandler getDefaultEndSchematic(String mode) {
        return Main.getPlugin(Main.class).endSchematic.get(mode);
    }
}
