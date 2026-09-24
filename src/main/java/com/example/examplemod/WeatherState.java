package com.example.examplemod;

public final class WeatherState {
    public enum ClientWeather {
        VANILLA, CLEAR, RAIN, SNOW, THUNDER
    }

    public static boolean barrierBypass = true;
    public static ClientWeather weather = ClientWeather.VANILLA;

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
}