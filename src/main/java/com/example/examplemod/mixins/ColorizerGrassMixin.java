package com.example.examplemod.mixins;

import com.example.examplemod.WeatherState;
import net.minecraft.world.ColorizerGrass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ColorizerGrass.class)
public class ColorizerGrassMixin {

    @Inject(
        method = "getGrassColor(DD)I",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void bdbr$forceGrassColor(double temperature, double humidity, CallbackInfoReturnable<Integer> cir) {
        // Se o clima for neve, força a cor da grama para o padrão (plains)
        if (WeatherState.weather == WeatherState.ClientWeather.SNOW) {
            // Cor da grama do bioma plains (0x79C05A)
            cir.setReturnValue(0x79C05A);
        }
    }
}