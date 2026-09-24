package com.example.examplemod.gui;

import com.example.examplemod.WeatherState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiBdbr extends GuiScreen {

    // IDs dos botões
    private static final int BTN_RAIN    = 100;
    private static final int BTN_SNOW    = 101;
    private static final int BTN_THUNDER = 102;
    private static final int BTN_CLEAR   = 103;
    private static final int BTN_VANILLA = 104;
    private static final int BTN_BARRIER = 110;
    private static final int BTN_COLOR_DEFAULT = 200;
    private static final int BTN_COLOR_PRESET_BASE = 201; // 201..208
    private static final int BTN_HEX_APPLY = 300;
    private static final int BTN_CLOSE = 400;

    /** Cores dos 8 presets (na ordem dos botões). */
    private static final int[] PRESET_COLORS = {
            0xFFFFFF, // 0 Branco
            0x1A1A1A, // 1 Preto (não 100% pra não sumir)
            0xFF3B3B, // 2 Vermelho
            0x3B9BFF, // 3 Azul
            0x3BFF5F, // 4 Verde
            0xA855F7, // 5 Roxo
            0xFFD447, // 6 Dourado
            0xFF6FB0  // 7 Rosa
    };

    /** Labels originais dos botões (sem cor) — usados para reconstruir o texto. */
    private static final String LABEL_RAIN    = "☂ Chuva";
    private static final String LABEL_SNOW    = "❄ Neve";
    private static final String LABEL_THUNDER = "⚡ Trov.";
    private static final String LABEL_CLEAR   = "☀ Limpo";
    private static final String LABEL_VANILLA = "⟲ Vanilla";

    // Dimensões do painel
    private static final int PANEL_W = 340;
    private static final int PANEL_H = 240;

    private GuiTextField hexField;

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        int cx = this.width / 2;
        int top = this.height / 2 - PANEL_H / 2;

        // ===== Clima =====
        int y = top + 38;
        int wBtn = 62, gap = 4;
        int totalW = 5 * wBtn + 4 * gap;
        int sx = cx - totalW / 2;

        this.buttonList.add(new GuiButton(BTN_RAIN,    sx,                     y, wBtn, 20, LABEL_RAIN));
        this.buttonList.add(new GuiButton(BTN_SNOW,    sx + (wBtn + gap),      y, wBtn, 20, LABEL_SNOW));
        this.buttonList.add(new GuiButton(BTN_THUNDER, sx + (wBtn + gap) * 2,  y, wBtn, 20, LABEL_THUNDER));
        this.buttonList.add(new GuiButton(BTN_CLEAR,   sx + (wBtn + gap) * 3,  y, wBtn, 20, LABEL_CLEAR));
        this.buttonList.add(new GuiButton(BTN_VANILLA, sx + (wBtn + gap) * 4,  y, wBtn, 20, LABEL_VANILLA));

        // ===== Barreira =====
        y = top + 74;
        this.buttonList.add(new GuiButton(BTN_BARRIER, cx - 160, y, 320, 20, ""));

        // ===== Cor da partícula =====
        y = top + 110;
        // Linha 1: Default, Branco, Preto, Vermelho, Azul
        String[] row1 = { "§7Default", "§fBranco", "§8Preto", "§cVermelho", "§9Azul" };
        int cBtn = 62;
        int cTotalW = 5 * cBtn + 4 * gap;
        int csx = cx - cTotalW / 2;
        for (int i = 0; i < 5; i++) {
            int id = (i == 0) ? BTN_COLOR_DEFAULT : BTN_COLOR_PRESET_BASE + (i - 1);
            this.buttonList.add(new GuiButton(id, csx + (cBtn + gap) * i, y, cBtn, 20, row1[i]));
        }

        // Linha 2: Verde, Roxo, Dourado, Rosa
        y = top + 134;
        String[] row2 = { "§aVerde", "§5Roxo", "§6Dourado", "§dRosa" };
        int r2TotalW = 4 * cBtn + 3 * gap;
        int r2sx = cx - r2TotalW / 2;
        for (int i = 0; i < 4; i++) {
            this.buttonList.add(new GuiButton(BTN_COLOR_PRESET_BASE + 4 + i, r2sx + (cBtn + gap) * i, y, cBtn, 20, row2[i]));
        }

        // ===== Hex =====
        y = top + 162;
        this.hexField = new GuiTextField(0, this.fontRendererObj, cx - 80, y, 130, 20);
        this.hexField.setMaxStringLength(7);
        this.hexField.setText(WeatherState.getParticleColorHex());
        this.buttonList.add(new GuiButton(BTN_HEX_APPLY, cx + 55, y, 60, 20, "§aAplicar"));

        // ===== Fechar =====
        y = top + 192;
        this.buttonList.add(new GuiButton(BTN_CLOSE, cx - 50, y, 100, 20, "§7Fechar"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int cx = this.width / 2;
        int left = cx - PANEL_W / 2;
        int top = this.height / 2 - PANEL_H / 2;

        // Painel
        drawRect(left, top, left + PANEL_W, top + PANEL_H, 0xD0101010);
        drawRect(left, top, left + PANEL_W, top + 1, 0xFF808080);
        drawRect(left, top + PANEL_H - 1, left + PANEL_W, top + PANEL_H, 0xFF808080);
        drawRect(left, top, left + 1, top + PANEL_H, 0xFF808080);
        drawRect(left + PANEL_W - 1, top, left + PANEL_W, top + PANEL_H, 0xFF808080);

        // Título
        drawCenteredString(this.fontRendererObj, "§6§lBarriers Don't Block Rain", cx, top + 10, 0xFFFFFF);

        // Labels de seção
        drawString(this.fontRendererObj, "§7§lCLIMA",             left + 10, top + 28, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7§lBARREIRA",          left + 10, top + 64, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7§lCOR DA PARTÍCULA",  left + 10, top + 100, 0xAAAAAA);
        drawString(this.fontRendererObj, "§7Hex:",                left + 20, top + 168, 0xAAAAAA);

        // Atualiza os botões (barreira + clima ativo)
        for (Object obj : this.buttonList) {
            GuiButton b = (GuiButton) obj;

            // Botão da barreira
            if (b.id == BTN_BARRIER) {
                boolean on = WeatherState.barrierBypass;
                b.displayString = (on ? "§a§lBarreira: ON" : "§c§lBarreira: OFF")
                        + " §7— clique pra alternar";
                continue;
            }

            // Botões de clima — destaca o ativo
            boolean isWeatherButton = b.id >= BTN_RAIN && b.id <= BTN_VANILLA;
            if (!isWeatherButton) continue;

            boolean isActive = false;
            String label = "";
            switch (b.id) {
                case BTN_RAIN:    isActive = WeatherState.weather == WeatherState.ClientWeather.RAIN;    label = LABEL_RAIN;    break;
                case BTN_SNOW:    isActive = WeatherState.weather == WeatherState.ClientWeather.SNOW;    label = LABEL_SNOW;    break;
                case BTN_THUNDER: isActive = WeatherState.weather == WeatherState.ClientWeather.THUNDER; label = LABEL_THUNDER; break;
                case BTN_CLEAR:   isActive = WeatherState.weather == WeatherState.ClientWeather.CLEAR;   label = LABEL_CLEAR;   break;
                case BTN_VANILLA: isActive = WeatherState.weather == WeatherState.ClientWeather.VANILLA; label = LABEL_VANILLA; break;
            }

            b.displayString = isActive ? "§a§l✔ " + label : label;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        this.hexField.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case BTN_RAIN:    WeatherState.setWeather(WeatherState.ClientWeather.RAIN);    return;
            case BTN_SNOW:    WeatherState.setWeather(WeatherState.ClientWeather.SNOW);    return;
            case BTN_THUNDER: WeatherState.setWeather(WeatherState.ClientWeather.THUNDER); return;
            case BTN_CLEAR:   WeatherState.setWeather(WeatherState.ClientWeather.CLEAR);   return;
            case BTN_VANILLA: WeatherState.setWeather(WeatherState.ClientWeather.VANILLA); return;
            case BTN_BARRIER: WeatherState.setBarrierBypass(!WeatherState.barrierBypass);  return;
            case BTN_HEX_APPLY: applyHex(); return;
            case BTN_CLOSE:   this.mc.displayGuiScreen(null); return;
        }

        if (button.id == BTN_COLOR_DEFAULT) {
            WeatherState.clearParticleColor();
            this.hexField.setText("");
            return;
        }
        if (button.id >= BTN_COLOR_PRESET_BASE && button.id < BTN_COLOR_PRESET_BASE + PRESET_COLORS.length) {
            int idx = button.id - BTN_COLOR_PRESET_BASE;
            int rgb = PRESET_COLORS[idx];
            WeatherState.setParticleColor(rgb);
            this.hexField.setText(String.format("%06X", rgb));
            return;
        }
    }

    private void applyHex() {
        String text = this.hexField.getText().trim();
        if (text.startsWith("#")) text = text.substring(1);
        if (text.isEmpty()) {
            WeatherState.clearParticleColor();
            return;
        }
        if (text.length() > 6) text = text.substring(0, 6);
        try {
            int rgb = Integer.parseInt(text, 16);
            WeatherState.setParticleColor(rgb);
        } catch (NumberFormatException ignored) {
            // Hex inválido — ignora silenciosamente
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.hexField.textboxKeyTyped(typedChar, keyCode)) {
            if (keyCode == Keyboard.KEY_RETURN) applyHex();
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.hexField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}