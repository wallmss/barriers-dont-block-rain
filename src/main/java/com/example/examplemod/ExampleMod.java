package com.example.examplemod;

import com.example.examplemod.commands.CommandBdbr;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

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
        // Registra o comando no CLIENTE — funciona em singleplayer E multiplayer
        if (event.getSide() == Side.CLIENT) {
            ClientCommandHandler.instance.registerCommand(new CommandBdbr());
        }
    }
}