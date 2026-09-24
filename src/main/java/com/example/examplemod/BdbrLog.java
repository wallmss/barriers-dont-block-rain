package com.example.examplemod;

public final class BdbrLog {
    public static final boolean ENABLED = true;

    public static void log(String msg) {
        if (ENABLED) System.out.println("[BDBR] " + msg);
    }

    private BdbrLog() {}
}