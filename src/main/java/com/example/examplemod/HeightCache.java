package com.example.examplemod;

import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public final class HeightCache {
    // 500x500 = 250.000 colunas. Deixamos folga para mais de um mundo/área.
    private static final int MAX_SIZE = 600_000;

    private static final Map<Long, Integer> CACHE = new HashMap<>();
    private static World cachedWorld = null;

    private HeightCache() {}

    private static long key(int x, int z) {
        return ((long) x & 0xFFFFFFFFL) | (((long) z) << 32);
    }

    private static void checkWorld(World world) {
        if (world != cachedWorld) {
            CACHE.clear();
            cachedWorld = world;
        }
    }

    public static int get(World world, int x, int z) {
        checkWorld(world);
        Integer v = CACHE.get(key(x, z));
        return v != null ? v : Integer.MIN_VALUE;
    }

    public static void put(World world, int x, int z, int y) {
        checkWorld(world);
        if (CACHE.size() >= MAX_SIZE) CACHE.clear();
        CACHE.put(key(x, z), y);
    }

    public static void invalidate(int x, int z) {
        CACHE.remove(key(x, z));
    }

    public static void clear() {
        CACHE.clear();
    }
}