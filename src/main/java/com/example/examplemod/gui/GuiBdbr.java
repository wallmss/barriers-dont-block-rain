package com.example.examplemod.gui;

import com.example.examplemod.BdbrConfig;
import com.example.examplemod.BdbrLog;
import com.example.examplemod.WeatherState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiBdbr extends GuiScreen {

    private static final int BTN_RAIN    = 100;
    private static final int BTN_SNOW    = 101;
    private static final int BTN_THUNDER = 102;
    private static final int BTN_CLEAR   = 103;
    private static final int BTN_VANILLA = 104;

    private static final int BTN_BARRIER = 110;
    private static final int BTN_THROUGH = 111;

    private static final int BTN_MODE_SOLID   = 120;
    private static final int BTN_MODE_RAINBOW = 121;
    private static final int BTN_MODE_MIX     = 122;

    // Presets: 200..207 → índices 0..7
    private static final int BTN_COLOR_PRESET_BASE = 200;

    private static final int BTN_OPACITY_MINUS = 320;
    private static final int BTN_OPACITY_PLUS  = 321;
    private static final int BTN_SPEED_MINUS   = 322;
    private static final int BTN_SPEED_PLUS    = 323;

    private static final int BTN_CLOSE  = 400;

    // Colors aligned with labels:
    private static final int[] PRESET_COLORS = {
            0xFFFFFF, // White
            0x1A1A1A, // Black
            0xFF3B3B, // Red
            0x3B9BFF, // Blue
            0x3BFF5F, // Green
            0xA855F7, // Purple
            0xFFD447, // Gold
            0xFF6FB0  // Pink
    };

    private static final String[] PRESET_LABELS = {
            "§fWhite", "§8Black", "§cRed", "§9Blue",
            "§aGreen", "§5Purple", "§6Gold", "§dPink"
    };

    private static final String L_RAIN    = "\u2602 Rain";
    private static final String L_SNOW    = "\u2744 Snow";
    private static final String L_THUNDER = "\u26A1 Thndr";
    private static final String L_CLEAR   = "\u2600 Clear";
    private static final String L_VANILLA = "\u27F2 Vanilla";

    private static final int PANEL_W = 460;
    private static final int PANEL_H = 270;
    private static final int COL_L_OFF = 10;
    private static final int COL_R_OFF = 240;
    private static final int COL_W = 210;

    private int ghX, ghY, ghW, ghH;

    private int px() { return (this.width  / 2) - PANEL_W / 2; }
    private int py() { return (this.height / 2) - PANEL_H / 2; }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(false);

        int left = px();
        int top  = py();
        int colL = left + COL_L_OFF;
        int colR = left + COL_R_OFF;

        // ---- CLIMATE ----
        int y = top + 36;
        int bw = 42, gap = 2;
        this.buttonList.add(new GuiButton(BTN_RAIN,    colL,                    y, bw, 20, L_RAIN));
        this.buttonList.add(new GuiButton(BTN_SNOW,    colL + (bw + gap),       y, bw, 20, L_SNOW));
        this.buttonList.add(new GuiButton(BTN_THUNDER, colL + (bw + gap) * 2,   y, bw, 20, L_THUNDER));
        this.buttonList.add(new GuiButton(BTN_CLEAR,   colL + (bw + gap) * 3,   y, bw, 20, L_CLEAR));
        this.buttonList.add(new GuiButton(BTN_VANILLA, colL + (bw + gap) * 4,   y, bw, 20, L_VANILLA));

        // ---- BARRIER / BLOCKS ----
        y = top + 80;
        int bh = (COL_W - 4) / 2;
        this.buttonList.add(new GuiButton(BTN_BARRIER, colL,                y, bh, 20, ""));
        this.buttonList.add(new GuiButton(BTN_THROUGH, colL + bh + 4,       y, COL_W - bh - 4, 20, ""));

        // ---- COLOR MODE ----
        y = top + 124;
        int mw = (COL_W - 8) / 3;
        this.buttonList.add(new GuiButton(BTN_MODE_SOLID,   colL,                y, mw, 20, ""));
        this.buttonList.add(new GuiButton(BTN_MODE_RAINBOW, colL + mw + 4,       y, mw, 20, ""));
        this.buttonList.add(new GuiButton(BTN_MODE_MIX,     colL + (mw + 4) * 2, y, COL_W - (mw + 4) * 2, 20, ""));

        // ---- PRESETS (2 rows of 4) ----
        int cw = (COL_W - 12) / 4;
        y = top + 168;
        for (int i = 0; i < 4; i++) {
            this.buttonList.add(new GuiButton(BTN_COLOR_PRESET_BASE + i,
                    colL + (cw + 4) * i, y, cw, 20, ""));
        }
        y = top + 192;
        for (int i = 0; i < 4; i++) {
            this.buttonList.add(new GuiButton(BTN_COLOR_PRESET_BASE + 4 + i,
                    colL + (cw + 4) * i, y, cw, 20, ""));
        }

        // ---- OPACITY (right col) ----
        y = top + 36;
        this.buttonList.add(new GuiButton(BTN_OPACITY_MINUS, colR + 90,  y, 20, 20, "§c-"));
        this.buttonList.add(new GuiButton(BTN_OPACITY_PLUS,  colR + 180, y, 20, 20, "§a+"));

        // ---- COLOR SPEED ----
        y = top + 66;
        this.buttonList.add(new GuiButton(BTN_SPEED_MINUS, colR + 90,  y, 20, 20, "§c-"));
        this.buttonList.add(new GuiButton(BTN_SPEED_PLUS,  colR + 180, y, 20, 20, "§a+"));

        // ---- CLOSE ----
        y = top + PANEL_H - 32;
        this.buttonList.add(new GuiButton(BTN_CLOSE, colR + (COL_W - 100) / 2, y, 100, 20, "§7Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int left = px();
        int top  = py();
        int cx   = this.width / 2;
        int colL = left + COL_L_OFF;
        int colR = left + COL_R_OFF;

        drawRect(left, top, left + PANEL_W, top + PANEL_H, 0xD0101010);
        drawRect(left, top, left + PANEL_W, top + 1, 0xFF808080);
        drawRect(left, top + PANEL_H - 1, left + PANEL_W, top + PANEL_H, 0xFF808080);
        drawRect(left, top, left + 1, top + PANEL_H, 0xFF808080);
        drawRect(left + PANEL_W - 1, top, left + PANEL_W, top + PANEL_H, 0xFF808080);

        int mid = left + 230;
        drawRect(mid, top + 22, mid + 1, top + PANEL_H - 8, 0xFF505050);

        drawCenteredString(this.fontRendererObj, "§6§lBarriers Don't Block Rain", cx, top + 6, 0xFFFFFF);

        drawString(this.fontRendererObj, "§7§lCLIMATE",          colL, top + 24, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7§lBARRIER / BLOCKS", colL, top + 68, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7§lCOLOR MODE",       colL, top + 112, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7§lCOLOR",            colL, top + 156, 0xAAAAAA);

        drawString(this.fontRendererObj, "§7§lTUNING", colR, top + 24, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7Opacity", colR, top + 42, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7Color Speed", colR, top + 72, 0xAAAAAA);

        String valOpacity = "§f" + Math.round(WeatherState.rainStrength * 100) + "%";
        int valOW = this.fontRendererObj.getStringWidth(valOpacity);
        drawString(this.fontRendererObj, valOpacity, colR + 130 - valOW / 2, top + 42, 0xFFFFFF);

        String valSpeed = "§f" + String.format("%.2fx", WeatherState.colorSpeed);
        int valSW = this.fontRendererObj.getStringWidth(valSpeed);
        drawString(this.fontRendererObj, valSpeed, colR + 130 - valSW / 2, top + 72, 0xFFFFFF);

        // Preview
        int prevY = top + 226;
        drawString(this.fontRendererObj, "§7Preview", colL, prevY - 10, 0xAAAAAA);
        drawRect(colL - 1, prevY - 1, colL + COL_W + 1, prevY + 16, 0xFF808080);
        int previewColor = WeatherState.hasParticleColor() ? WeatherState.getCurrentRGB() : 0xFFFFFF;
        drawRect(colL, prevY, colL + COL_W, prevY + 14, 0xFF000000 | previewColor);

        // Credits
        String madeBy = "§7Made by §b§lsx";
        int madeByW = this.fontRendererObj.getStringWidth(madeBy);
        drawString(this.fontRendererObj, madeBy, colR + COL_W - madeByW, top + PANEL_H - 70, 0xFFFFFF);

        String gh = "§9§nGitHub: wallmss";
        int ghW = this.fontRendererObj.getStringWidth(gh);
        ghX = colR + COL_W - ghW;
        ghY = top + PANEL_H - 56;
        ghH = 10;
        this.ghW = ghW;
        drawString(this.fontRendererObj, gh, ghX, ghY, 0xFFFFFF);

        // Dynamic button labels
        for (Object obj : this.buttonList) {
            GuiButton b = (GuiButton) obj;

            if (b.id == BTN_BARRIER) {
                b.displayString = WeatherState.barrierBypass
                        ? "§aBarrier: §a§lON" : "§cBarrier: §c§lOFF";
                continue;
            }
            if (b.id == BTN_THROUGH) {
                b.displayString = WeatherState.weatherThroughBlocks
                        ? "§aBlocks: §a§lON" : "§cBlocks: §c§lOFF";
                continue;
            }
            if (b.id == BTN_MODE_SOLID) {
                boolean act = WeatherState.getColorMode() == WeatherState.ColorMode.SOLID;
                b.displayString = (act ? "§a§l" : "§7") + "Solid";
                continue;
            }
            if (b.id == BTN_MODE_RAINBOW) {
                boolean act = WeatherState.getColorMode() == WeatherState.ColorMode.RAINBOW;
                b.displayString = (act ? "§a§l" : "§d") + "Rainbow";
                continue;
            }
            if (b.id == BTN_MODE_MIX) {
                boolean act = WeatherState.getColorMode() == WeatherState.ColorMode.MIX;
                b.displayString = (act ? "§a§l" : "§b") + "Mix";
                continue;
            }

            // Presets — IDs 200..207 → idx 0..7
            if (b.id >= BTN_COLOR_PRESET_BASE
                    && b.id < BTN_COLOR_PRESET_BASE + PRESET_COLORS.length) {
                int idx = b.id - BTN_COLOR_PRESET_BASE;
                int rgb = PRESET_COLORS[idx];
                String label = PRESET_LABELS[idx];
                boolean selected = WeatherState.getColorMode() == WeatherState.ColorMode.MIX
                        && WeatherState.isMixColorSelected(rgb);
                b.displayString = selected ? "§a\u2714§r " + label : label;
                continue;
            }

            boolean isWeather = b.id >= BTN_RAIN && b.id <= BTN_VANILLA;
            if (!isWeather) continue;

            boolean act = false;
            String label = "";
            switch (b.id) {
                case BTN_RAIN:    act = WeatherState.weather == WeatherState.ClientWeather.RAIN;    label = L_RAIN;    break;
                case BTN_SNOW:    act = WeatherState.weather == WeatherState.ClientWeather.SNOW;    label = L_SNOW;    break;
                case BTN_THUNDER: act = WeatherState.weather == WeatherState.ClientWeather.THUNDER; label = L_THUNDER; break;
                case BTN_CLEAR:   act = WeatherState.weather == WeatherState.ClientWeather.CLEAR;   label = L_CLEAR;   break;
                case BTN_VANILLA: act = WeatherState.weather == WeatherState.ClientWeather.VANILLA; label = L_VANILLA; break;
            }
            b.displayString = (act ? "§a" : "§7") + label;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        BdbrLog.log("Clicked id=" + button.id);

        switch (button.id) {
            case BTN_RAIN:    WeatherState.setWeather(WeatherState.ClientWeather.RAIN);    break;
            case BTN_SNOW:    WeatherState.setWeather(WeatherState.ClientWeather.SNOW);    break;
            case BTN_THUNDER: WeatherState.setWeather(WeatherState.ClientWeather.THUNDER); break;
            case BTN_CLEAR:   WeatherState.setWeather(WeatherState.ClientWeather.CLEAR);   break;
            case BTN_VANILLA: WeatherState.setWeather(WeatherState.ClientWeather.VANILLA); break;

            case BTN_BARRIER: WeatherState.setBarrierBypass(!WeatherState.barrierBypass); break;
            case BTN_THROUGH: WeatherState.setWeatherThroughBlocks(!WeatherState.weatherThroughBlocks); break;

            case BTN_MODE_SOLID:   WeatherState.setColorMode(WeatherState.ColorMode.SOLID);   break;
            case BTN_MODE_RAINBOW: WeatherState.setColorMode(WeatherState.ColorMode.RAINBOW); break;
            case BTN_MODE_MIX:     WeatherState.setColorMode(WeatherState.ColorMode.MIX);     break;

            case BTN_OPACITY_MINUS:
                WeatherState.rainStrength = clamp(WeatherState.rainStrength - 0.05f, 0.05f, 1.0f);
                BdbrLog.log("rainStrength=" + WeatherState.rainStrength); break;
            case BTN_OPACITY_PLUS:
                WeatherState.rainStrength = clamp(WeatherState.rainStrength + 0.05f, 0.05f, 1.0f);
                BdbrLog.log("rainStrength=" + WeatherState.rainStrength); break;

            case BTN_SPEED_MINUS:
                WeatherState.colorSpeed = clamp(WeatherState.colorSpeed - 0.25f, 0.25f, 4.0f);
                BdbrLog.log("colorSpeed=" + WeatherState.colorSpeed); break;
            case BTN_SPEED_PLUS:
                WeatherState.colorSpeed = clamp(WeatherState.colorSpeed + 0.25f, 0.25f, 4.0f);
                BdbrLog.log("colorSpeed=" + WeatherState.colorSpeed); break;

            case BTN_CLOSE: this.mc.displayGuiScreen(null); return;
            default: break;
        }

        // Presets
        if (button.id >= BTN_COLOR_PRESET_BASE
                && button.id < BTN_COLOR_PRESET_BASE + PRESET_COLORS.length) {
            int idx = button.id - BTN_COLOR_PRESET_BASE;
            WeatherState.clickPreset(PRESET_COLORS[idx]);
        }
    }

    private static float clamp(float v, float lo, float hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX >= ghX && mouseX < ghX + ghW && mouseY >= ghY && mouseY < ghY + ghH) {
            openGitHub();
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private static void openGitHub() {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(
                        new java.net.URI("https://github.com/wallmss"));
                BdbrLog.log("Opened GitHub in browser");
            }
        } catch (Throwable t) {
            BdbrLog.log("Failed to open GitHub: " + t);
        }
    }

    @Override
    public void onGuiClosed() {
        BdbrConfig.save();
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}