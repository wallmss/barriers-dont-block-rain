package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public final class BdbrConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File file;

    private BdbrConfig() {}

    public static void init(File configDir) {
        file = new File(configDir, "bdbr.json");
        load();
    }

    public static void load() {
        if (file == null || !file.exists()) {
            BdbrLog.log("Config file not found, using defaults.");
            return;
        }
        try (FileReader r = new FileReader(file)) {
            Data d = GSON.fromJson(r, Data.class);
            if (d != null) {
                apply(d);
                BdbrLog.log("Config loaded from " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            BdbrLog.log("Config load FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void save() {
        if (file == null) return;
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();
            try (FileWriter w = new FileWriter(file)) {
                GSON.toJson(collect(), w);
            }
            BdbrLog.log("Config saved to " + file.getAbsolutePath());
        } catch (Exception e) {
            BdbrLog.log("Config save FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Data collect() {
        Data d = new Data();
        d.barrierBypass = WeatherState.barrierBypass;
        d.weatherThroughBlocks = WeatherState.weatherThroughBlocks;
        d.weather = WeatherState.weather.name();
        d.colorMode = WeatherState.getColorMode().name();
        d.particleColor = WeatherState.particleColor;
        d.mixColors = new ArrayList<>(WeatherState.mixColors);
        d.rainStrength = WeatherState.rainStrength;
        d.colorSpeed = WeatherState.colorSpeed;
        return d;
    }

    private static void apply(Data d) {
        WeatherState.barrierBypass = d.barrierBypass;
        WeatherState.weatherThroughBlocks = d.weatherThroughBlocks;
        try { WeatherState.weather = WeatherState.ClientWeather.valueOf(d.weather); } catch (Exception ignored) {}
        WeatherState.particleColor = d.particleColor == null ? 0xFFFFFF : (d.particleColor & 0xFFFFFF);
        try { WeatherState.colorMode = WeatherState.ColorMode.valueOf(d.colorMode); } catch (Exception ignored) {}

        WeatherState.mixColors.clear();
        if (d.mixColors != null) {
            for (Integer c : d.mixColors) if (c != null) WeatherState.mixColors.add(c & 0xFFFFFF);
        }

        WeatherState.rainStrength = clamp(d.rainStrength, 0.05f, 1.0f);
        WeatherState.colorSpeed   = clamp(d.colorSpeed,   0.25f, 4.0f);
    }

    private static float clamp(float v, float lo, float hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    public static class Data {
        public boolean barrierBypass = true;
        public boolean weatherThroughBlocks = false;
        public String weather = "VANILLA";
        public String colorMode = "SOLID";
        public Integer particleColor = 0xFFFFFF;
        public List<Integer> mixColors = new ArrayList<>();
        public float rainStrength = 1.0f;
        public float colorSpeed = 1.0f;
    }
}