package com.example.examplemod;

import com.example.examplemod.commands.CommandBdbr;
import com.example.examplemod.commands.CommandCredito;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
        modid = BuildConfig.MODID,
        name = BuildConfig.MODNAME,
        version = BuildConfig.VERSION)
public class ExampleMod {

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // nada por enquanto
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // nada por enquanto
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBdbr());
        event.registerServerCommand(new CommandCredito());
    }
}