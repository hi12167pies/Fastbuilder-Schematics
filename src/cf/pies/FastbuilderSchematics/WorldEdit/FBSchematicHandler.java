package cf.pies.FastbuilderSchematics.WorldEdit;

import cf.pies.FastbuilderSchematics.Main;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class FBSchematicHandler {
    private String fileName;
    private File file;
    public FBSchematicHandler(String fileName) {
        this.fileName = fileName;
        this.reloadFile();
    }

    public void setFileName(String string) {
        this.fileName = string;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void reloadFile() {
        this.file = new File(Main.getPlugin(Main.class).getDataFolder() + "/" + this.fileName + ".schematic");
    }


    public File getFile() {
        return this.file;
    }

    public void save(Location _pos1, Location _pos2, Location origin) {
        try {
            Vector pos1 = FBFAWEUtil.toVector(_pos1);
            Vector pos2 = FBFAWEUtil.toVector(_pos2);
            CuboidRegion region = new CuboidRegion(FBFAWEUtil.toWorld(_pos1.getWorld()), pos1, pos2);
            Schematic schematic = new Schematic(region);
            if (schematic.getClipboard() != null) {
                schematic.getClipboard().setOrigin(FBFAWEUtil.toVector(origin));
            }
            schematic.save(this.getFile(), ClipboardFormat.SCHEMATIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(Location origin) {
        try {
            FaweAPI.load(this.getFile()).paste(
                    FBFAWEUtil.toWorld(origin.getWorld()),
                    FBFAWEUtil.toVector(origin),
                    false,
                    true,
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
