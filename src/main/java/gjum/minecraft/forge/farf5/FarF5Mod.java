package gjum.minecraft.forge.farf5;

import gjum.minecraft.forge.farf5.gui.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PatchedEntityRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.getPrivateValue;

@SuppressWarnings("unused")
@Mod(
        modid = FarF5Mod.MOD_ID,
        name = FarF5Mod.MOD_NAME,
        version = FarF5Mod.VERSION,
        guiFactory = "gjum.minecraft.forge.farf5.gui.ConfigGuiFactory",
        clientSideOnly = true)
public class FarF5Mod {
    public static final String MOD_ID = "farf5";
    public static final String MOD_NAME = "Far F5";
    public static final String VERSION = "@VERSION@";
    public static final String BUILD_TIME = "@BUILD_TIME@";

    private static final Minecraft mc = Minecraft.getMinecraft();

    @Mod.Instance(MOD_ID)
    public static FarF5Mod instance;

    public final KeyBinding openGuiKey = new KeyBinding(MOD_ID + ".key.openGui", Keyboard.KEY_NONE, MOD_NAME);
    public final KeyBinding toggleEnabledKey = new KeyBinding(MOD_ID + ".key.toggleEnabled", Keyboard.KEY_NONE, MOD_NAME);
    public final KeyBinding zoomTowardsKey = new KeyBinding(MOD_ID + ".key.zoomTowards", Keyboard.KEY_NONE, MOD_NAME);
    public final KeyBinding zoomAwayKey = new KeyBinding(MOD_ID + ".key.zoomAway", Keyboard.KEY_NONE, MOD_NAME);

    public final Config config = new Config();

    private static void patchRenderer() {
        final SimpleReloadableResourceManager mcResourceManager = (SimpleReloadableResourceManager) mc.getResourceManager();
        final List<IResourceManagerReloadListener> reloadListeners = getPrivateValue(SimpleReloadableResourceManager.class, mcResourceManager, "reloadListeners");

        // remove old renderer
        reloadListeners.remove(mc.entityRenderer);

        // add patched renderer with custom camera perspective
        mc.entityRenderer = new PatchedEntityRenderer(mc, mcResourceManager);
        mcResourceManager.registerReloadListener(mc.entityRenderer);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogManager.getLogger().info(String.format("%s version %s built at %s", MOD_NAME, VERSION, BUILD_TIME));

        File configFile = event.getSuggestedConfigurationFile();
        configFile = new File(configFile.getAbsolutePath().replaceAll("\\.[^.]+$", ".json"));
        try {
            config.load(configFile);
            LogManager.getLogger().info("Loaded config from " + configFile);
        } catch (IOException e) {
            e.printStackTrace();
            LogManager.getLogger().warn("Failed to load config from " + configFile);
        }
        config.save();

        ClientRegistry.registerKeyBinding(openGuiKey);
        ClientRegistry.registerKeyBinding(toggleEnabledKey);
        ClientRegistry.registerKeyBinding(zoomTowardsKey);
        ClientRegistry.registerKeyBinding(zoomAwayKey);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        patchRenderer();
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (openGuiKey.isPressed()) {
            mc.displayGuiScreen(new GuiConfig(mc.currentScreen));
        }
        if (toggleEnabledKey.isPressed()) {
            config.modEnabled = !config.modEnabled;
            config.save();
        }
        if (zoomTowardsKey.isPressed()) {
            config.farDistance /= config.zoomFactor;
            config.save();
        }
        if (zoomAwayKey.isPressed()) {
            config.farDistance *= config.zoomFactor;
            config.save();
        }
    }
}
