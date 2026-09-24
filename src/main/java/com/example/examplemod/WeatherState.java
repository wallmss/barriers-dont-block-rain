package com.example.examplemod;

public final class WeatherState {
    public enum ClientWeather {
        VANILLA, CLEAR, RAIN, SNOW, THUNDER
    }

    public static boolean barrierBypass = true;
    public static ClientWeather weather = ClientWeather.VANILLA;

    /** null = cor vanilla (padrão). Caso contrário, RGB 0xRRGGBB. */
    private static Integer particleColor = null;

    private WeatherState() {}

    public static boolean hasWeatherOverride() {
        return weather != ClientWeather.VANILLA;
    }

    public static void setWeather(ClientWeather w) {
        weather = w;
        HeightCache.clear();
    }

    public static void setBarrierBypass(boolean b) {
        barrierBypass = b;
        HeightCache.clear();
    }

    public static boolean hasParticleColor() {
        return particleColor != null;
    }

    public static int getParticleColor() {
        return particleColor == null ? 0xFFFFFF : particleColor;
    }

    public static void setParticleColor(int rgb) {
        particleColor = rgb & 0xFFFFFF;
    }

    public static void clearParticleColor() {
        particleColor = null;
    }

    /** Retorna a cor atual como hex sem "#", ou "" se estiver no padrão. */
    public static String getParticleColorHex() {
        return particleColor == null ? "" : String.format("%06X", particleColor);
    }
}