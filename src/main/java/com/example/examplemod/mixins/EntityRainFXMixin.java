package com.example.examplemod.mixins;

import com.example.examplemod.WeatherState;
import net.minecraft.client.particle.EntityRainFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRainFX.class)
public abstract class EntityRainFXMixin implements EntityFXAccessor {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void bdbr$onInit(CallbackInfo ci) {
        if (!WeatherState.hasParticleColor()) return;
        int rgb = WeatherState.getCurrentRGB();
        this.bdbr$setParticleRed  (((rgb >> 16) & 0xFF) / 255.0F);
        this.bdbr$setParticleGreen(((rgb >>  8) & 0xFF) / 255.0F);
        this.bdbr$setParticleBlue (( rgb        & 0xFF) / 255.0F);
    }
}