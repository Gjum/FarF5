package gjum.minecraft.forge.farf5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public final class Config {
    @Expose()
    public boolean modEnabled = false;
    @Expose()
    public int farDistance = 64;

    private File configFile;

    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    public void load(File configFile) throws IOException {
        this.configFile = configFile;
        FileReader reader = new FileReader(configFile);
        copyFrom(gson.fromJson(reader, this.getClass()));
        reader.close();
    }

    private void copyFrom(Config newConf) {
        modEnabled = newConf.modEnabled;
        farDistance = newConf.farDistance;
    }

    public void save() {
        try {
            save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            LogManager.getLogger().log(Level.WARN, "Failed to save config to %s", configFile);
        }
    }

    public void save(File configFile) throws IOException {
        String json = gson.toJson(this);
        FileOutputStream fos = new FileOutputStream(configFile);
        fos.write(json.getBytes());
        fos.close();

        this.configFile = configFile;
    }
}
