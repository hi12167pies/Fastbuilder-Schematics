package cf.pies.FastbuilderSchematics;

import cf.pies.FastbuilderSchematics.Data.ModeData;
import cf.pies.FastbuilderSchematics.Data.ModeType;
import cf.pies.FastbuilderSchematics.WorldEdit.FBSchematicHandler;
import cf.pies.fastbuilder.api.FastbuilderAPI;
import cf.pies.fastbuilder.api.FastbuilderProvider;
import cf.pies.fastbuilder.api.events.PlayerJoinArenaEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {
    public static SchematicManager arenaManager;
    public HashMap<String, FBSchematicHandler> spawnSchematic = new HashMap<>();
    public HashMap<String, FBSchematicHandler> endSchematic = new HashMap<>();
    public Location getConfigLoc(World world, ConfigurationSection section) {
        Location location = new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"));
        if (section.contains("yaw")) {
            location.setYaw((float) section.getDouble("yaw"));
        }
        if (section.contains("pitch")) {
            location.setPitch((float) section.getDouble("pitch"));
        }
        return location;
    }
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        this.saveDefaultConfig();

        SchematicManager.MAPS = this.getConfig().getInt("islands.maps");

        World world = Bukkit.getWorld(this.getConfig().getString("world"));
        Location defaultSpawn = this.getConfigLoc(world, this.getConfig().getConfigurationSection("locations.spawn"));
        Location defaultPos1 = this.getConfigLoc(world, this.getConfig().getConfigurationSection("locations.pos1"));
        Location defaultPos2 = this.getConfigLoc(world, this.getConfig().getConfigurationSection("locations.pos2"));

        for (String modeName : this.getConfig().getConfigurationSection("modes").getKeys(false)) {
            if (!spawnSchematic.containsKey(modeName)) {
                spawnSchematic.put(modeName, new FBSchematicHandler(this.getConfig().getString("modes." + modeName + ".schematic_start")));
            }
            if (!endSchematic.containsKey(modeName)) {
                endSchematic.put(modeName, new FBSchematicHandler(this.getConfig().getString("modes." + modeName + ".schematic_end")));
            }
            this.getConfig().getString("modes." + modeName + ".schematic_end");

            ModeType type = ModeType.valueOf(this.getConfig().getString("modes." + modeName + ".type"));
            int spacing = this.getConfig().getInt("islands.spacing");
            int distance = this.getConfig().getInt("modes." + modeName + ".distance");
            SchematicManager.modeData.put(
                    modeName,
                    new ModeData(
                            this.getConfig().getInt("islands.startLength"),
                            this.getConfig().getInt("islands.endLength"),
                            type == ModeType.INCLINED ? spacing + distance : spacing,
                            distance,
                            this.getConfig().getInt("modes." + modeName + ".distance_up"),
                            this.getConfig().getInt("modes." + modeName + ".id") * this.getConfig().getInt("mode_distance"),
                            this.getConfig().getString("modes." + modeName + ".schematic_start"),
                            this.getConfig().getString("modes." + modeName + ".schematic_end"),
                            type,
                            this.getConfig().contains("modes." + modeName + ".spawn") ? this.getConfigLoc(world, this.getConfig().getConfigurationSection("modes." + modeName + ".spawn")) : defaultSpawn,
                            this.getConfig().contains("modes." + modeName + ".pos1") ? this.getConfigLoc(world, this.getConfig().getConfigurationSection("modes." + modeName + ".pos1")) : defaultPos1,
                            this.getConfig().contains("modes." + modeName + ".pos2") ? this.getConfigLoc(world, this.getConfig().getConfigurationSection("modes." + modeName + ".pos2")) : defaultPos2
                    )
            );
        }

        FastbuilderAPI api = FastbuilderProvider.getApi();
        SchematicManager arenaManager = new SchematicManager();
        Main.arenaManager = arenaManager;

        api.setArenaManager(arenaManager);

        this.getCommand("spawnAllFastbuilderMaps").setExecutor(((commandSender, command, s, strings) -> {
            long start = System.currentTimeMillis();
            for (String mode : SchematicManager.modeData.keySet()) {
                spawnSchematic.get(mode).reloadFile();
                endSchematic.get(mode).reloadFile();
            }

            for (String cachedMap : arenaManager.cachedMaps) {
                String mode = cachedMap.split("-")[0];
                spawnSchematic.get(mode).load(arenaManager.getSpawn(cachedMap));
                // If parkour map, do not spawn end island.
                if (SchematicManager.modeData.get(mode).type != ModeType.PARKOUR) {
                    endSchematic.get(mode).load(arenaManager.getEndIsland(cachedMap));
                }
            }
            long end = System.currentTimeMillis() - start;
            commandSender.sendMessage("Done in " + end + "ms");
            return true;
        }));
    }

    @EventHandler
    public void joinArena(PlayerJoinArenaEvent event) {
        String mode = event.getArena().split("-")[0];
        spawnSchematic.get(mode).load(arenaManager.getSpawn(event.getArena()));
        if (SchematicManager.modeData.get(mode).type != ModeType.PARKOUR) {
            endSchematic.get(mode).load(arenaManager.getEndIsland(event.getArena()));
        }
    }
}
