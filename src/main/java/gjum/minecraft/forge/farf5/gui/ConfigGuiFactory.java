package gjum.minecraft.forge.farf5.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

@SuppressWarnings("unused")
public class ConfigGuiFactory implements IModGuiFactory {
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiConfig.class;
    }

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}
