package com.example.examplemod.mixins;

import com.example.examplemod.WeatherState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeGenBase.class)
public abstract class BiomeGenBaseMixin {

    @Inject(
        method = "getEnableSnow()Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void bdbr$forceEnableSnow(CallbackInfoReturnable<Boolean> cir) {
        if (!WeatherState.hasWeatherOverride()) return;
        if (!"Client thread".equals(Thread.currentThread().getName())) return;

        switch (WeatherState.weather) {
            case SNOW:
                cir.setReturnValue(true);
                return;
            case RAIN:
            case THUNDER:
                cir.setReturnValue(false);
                return;
            case CLEAR:
            case VANILLA:
            default:
                return;
        }
    }

    @Inject(
        method = "getFloatTemperature(Lnet/minecraft/util/BlockPos;)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private void bdbr$forceTemperature(BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (!WeatherState.hasWeatherOverride()) return;
        if (!"Client thread".equals(Thread.currentThread().getName())) return;

        switch (WeatherState.weather) {
            case SNOW:
                cir.setReturnValue(0.0F);
                return;
            case RAIN:
            case THUNDER:
                cir.setReturnValue(1.0F);
                return;
            case CLEAR:
            case VANILLA:
            default:
                return;
        }
    }
}