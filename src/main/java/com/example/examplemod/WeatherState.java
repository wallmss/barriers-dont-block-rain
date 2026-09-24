package com.example.examplemod;

import java.util.ArrayList;
import java.util.List;

public final class WeatherState {
    public enum ClientWeather { VANILLA, CLEAR, RAIN, SNOW, THUNDER }
    public enum ColorMode { SOLID, RAINBOW, MIX }

    public static boolean barrierBypass = true;
    public static boolean weatherThroughBlocks = false;
    public static ClientWeather weather = ClientWeather.VANILLA;

    public static ColorMode colorMode = ColorMode.SOLID;
    public static Integer particleColor = 0xFFFFFF;
    public static final List<Integer> mixColors = new ArrayList<>();

    public static float rainStrength = 1.0f;
    /** Velocidade do ciclo de cores (rainbow + mix). 0.25x .. 4.0x */
    public static float colorSpeed = 1.0f;

    private WeatherState() {}

    public static boolean hasWeatherOverride() {
        return weather != ClientWeather.VANILLA;
    }

    public static void setWeather(ClientWeather w) {
        weather = w;
        HeightCache.clear();
        BdbrLog.log("Weather = " + w.name());
    }

    public static void setBarrierBypass(boolean b) {
        barrierBypass = b;
        HeightCache.clear();
        BdbrLog.log("barrierBypass = " + b);
    }

    public static void setWeatherThroughBlocks(boolean b) {
        weatherThroughBlocks = b;
        BdbrLog.log("weatherThroughBlocks = " + b);
    }

    public static boolean hasParticleColor() {
        if (colorMode == ColorMode.RAINBOW) return true;
        if (colorMode == ColorMode.MIX) return !mixColors.isEmpty();
        return particleColor != null;
    }

    public static ColorMode getColorMode() { return colorMode; }

    public static void setColorMode(ColorMode m) {
        ColorMode old = colorMode;
        colorMode = m;
        if (m == ColorMode.SOLID && old != ColorMode.SOLID) {
            mixColors.clear();
            particleColor = 0xFFFFFF;
            BdbrLog.log("Switched to SOLID: mix cleared, default = #FFFFFF");
        }
        BdbrLog.log("colorMode = " + m.name());
    }

    public static void setParticleColor(int rgb) {
        particleColor = rgb & 0xFFFFFF;
        BdbrLog.log("setParticleColor = #" + String.format("%06X", particleColor));
    }

    public static void clearParticleColor() {
        particleColor = 0xFFFFFF;
        BdbrLog.log("particleColor reset to white");
    }

    public static String getParticleColorHex() {
        return particleColor == null ? "" : String.format("%06X", particleColor);
    }

    public static void toggleMixColor(int rgb) {
        rgb = rgb & 0xFFFFFF;
        if (mixColors.contains(rgb)) {
            mixColors.remove(Integer.valueOf(rgb));
            BdbrLog.log("Mix: removed #" + String.format("%06X", rgb)
                    + " (now " + mixColors.size() + ")");
        } else {
            mixColors.add(rgb);
            BdbrLog.log("Mix: added #" + String.format("%06X", rgb)
                    + " (now " + mixColors.size() + ")");
        }
    }

    public static boolean isMixColorSelected(int rgb) {
        return mixColors.contains(rgb & 0xFFFFFF);
    }

    public static void clickPreset(int rgb) {
        rgb = rgb & 0xFFFFFF;
        switch (colorMode) {
            case MIX:
                toggleMixColor(rgb);
                break;
            case RAINBOW:
                colorMode = ColorMode.SOLID;
                particleColor = rgb;
                BdbrLog.log("Preset clicked in RAINBOW → switched to SOLID #"
                        + String.format("%06X", rgb));
                break;
            default:
                colorMode = ColorMode.SOLID;
                particleColor = rgb;
                BdbrLog.log("Preset clicked → SOLID #" + String.format("%06X", rgb));
        }
    }

    public static int getCurrentRGB() {
        switch (colorMode) {
            case RAINBOW: return rainbowRGB();
            case MIX:     return mixRGB();
            default:      return particleColor == null ? 0xFFFFFF : particleColor;
        }
    }

    private static float safeSpeed() {
        float s = colorSpeed;
        if (s < 0.05f) s = 0.05f;
        if (s > 10f)   s = 10f;
        return s;
    }

    private static int rainbowRGB() {
        long cycle = (long)(8000L / safeSpeed());
        if (cycle < 100) cycle = 100;
        float t = (System.currentTimeMillis() % cycle) / (float) cycle;
        return hsvToRgb(t, 1f, 1f);
    }

    private static int mixRGB() {
        int n = mixColors.size();
        if (n == 0) return 0xFFFFFF;
        if (n == 1) return mixColors.get(0);

        float timePer = 1500f / safeSpeed();
        if (timePer < 80f) timePer = 80f;

        long total = (long)(timePer * n);
        float t = (System.currentTimeMillis() % total) / timePer;
        int i1 = (int) t;
        int i2 = (i1 + 1) % n;
        float f = t - i1;

        int c1 = mixColors.get(i1);
        int c2 = mixColors.get(i2);
        int r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int r = (int)(r1 + (r2 - r1) * f);
        int g = (int)(g1 + (g2 - g1) * f);
        int b = (int)(b1 + (b2 - b1) * f);
        return (r << 16) | (g << 8) | b;
    }

    private static int hsvToRgb(float h, float s, float v) {
        int i = (int)(h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        float r, g, b;
        switch (i % 6) {
            case 0: r=v; g=t; b=p; break;
            case 1: r=q; g=v; b=p; break;
            case 2: r=p; g=v; b=t; break;
            case 3: r=p; g=q; b=v; break;
            case 4: r=t; g=p; b=v; break;
            default: r=v; g=p; b=q; break;
        }
        return ((int)(r*255)<<16) | ((int)(g*255)<<8) | (int)(b*255);
    }
}