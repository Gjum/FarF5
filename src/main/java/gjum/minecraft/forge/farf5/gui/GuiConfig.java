package gjum.minecraft.forge.farf5.gui;

import gjum.minecraft.forge.farf5.Config;
import gjum.minecraft.forge.farf5.FarF5Mod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiConfig extends GuiScreen {
    private final GuiScreen parentScreen;
    private final Config config;

    private GuiTextField txtFarDistance;
    private GuiButton btnToggleEnabled;
    private GuiButton btnClose;

    private final ArrayList<GuiTextField> textFieldList = new ArrayList<>();
    private int idCounter = 0;

    private static final int DEFAULT_HEIGHT = 20;
    private static final int ROW_HEIGHT = DEFAULT_HEIGHT * 2;
    private static final int FULL_WIDTH = 240;
    private static final int HALF_WIDTH = FULL_WIDTH / 2;

    public GuiConfig(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        config = FarF5Mod.instance.config;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        labelList.clear();
        textFieldList.clear();

        final int rowsNum = 3;
        final int topEdge = (height - ROW_HEIGHT * rowsNum) / 2;
        final int leftEdge = (width - FULL_WIDTH) / 2;
        int currentRow = 0;

        GuiLabel lblTitle = new GuiLabel(fontRendererObj, nextId(),
                leftEdge, topEdge + ROW_HEIGHT * currentRow,
                FULL_WIDTH, DEFAULT_HEIGHT,
                Color.WHITE.getRGB());
        labelList.add(lblTitle);
        lblTitle.setCentered();
        lblTitle.addLine("Far F5 Settings");

        currentRow++;

        GuiLabel lblFarDistance = new GuiLabel(fontRendererObj, nextId(),
                leftEdge, topEdge + ROW_HEIGHT * currentRow,
                HALF_WIDTH, DEFAULT_HEIGHT,
                Color.WHITE.getRGB());
        labelList.add(lblFarDistance);
        lblFarDistance.addLine("Far distance:");

        textFieldList.add(txtFarDistance = new GuiTextField(nextId(), fontRendererObj,
                leftEdge + HALF_WIDTH, topEdge + ROW_HEIGHT * currentRow,
                HALF_WIDTH, DEFAULT_HEIGHT));
        txtFarDistance.setText(String.valueOf(config.farDistance));

        currentRow++;

        buttonList.add(btnToggleEnabled = new GuiButton(nextId(),
                leftEdge, topEdge + ROW_HEIGHT * currentRow,
                HALF_WIDTH, DEFAULT_HEIGHT,
                config.modEnabled ? "Disable" : "Enable"));

        buttonList.add(btnClose = new GuiButton(nextId(),
                leftEdge + HALF_WIDTH, topEdge + ROW_HEIGHT * currentRow,
                HALF_WIDTH, DEFAULT_HEIGHT,
                "Save and close"));

        currentRow++;

        if (currentRow != rowsNum) {
            LogManager.getLogger().warn(String.format("Mismatch in GUI height: expected %s, is %s", rowsNum, currentRow));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        for (GuiTextField txt : textFieldList) {
            txt.drawTextBox();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        boolean valid = true;

        try {
            Float.parseFloat(txtFarDistance.getText());
        } catch (NumberFormatException e) {
            valid = false;
        }

        btnClose.enabled = valid;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (!button.enabled) return;

        if (button.id == btnToggleEnabled.id) {
            config.modEnabled = !config.modEnabled;
            btnToggleEnabled.displayString = config.modEnabled ? "Disable" : "Enable";
        }
        if (button.id == btnClose.id) {
            saveAndLeave();
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        for (GuiTextField txt : textFieldList) {
            if (txt.isFocused()) {
                txt.textboxKeyTyped(keyChar, keyCode);
            }
        }
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (btnClose.enabled) {
                saveAndLeave();
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);
        for (GuiTextField field : textFieldList) {
            field.mouseClicked(x, y, mouseButton);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private void saveAndLeave() {
        config.farDistance = Float.parseFloat(txtFarDistance.getText());

        config.save();

        mc.displayGuiScreen(parentScreen);
    }

    private int nextId() {
        return idCounter++;
    }
}
