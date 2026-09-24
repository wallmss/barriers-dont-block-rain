package com.example.examplemod.mixins;

import com.example.examplemod.WeatherState;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRainFX.class)
public abstract class EntityRainFXMixin implements EntityFXAccessor {

    @Inject(
        method = "renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V",
        at = @At("HEAD")
    )
    private void bdbr$forceColorOnRender(
            WorldRenderer worldRendererIn,
            Entity entityIn,
            float partialTicks,
            float rotationX,
            float rotationZ,
            float rotationYZ,
            float rotationXY,
            float rotationXZ,
            CallbackInfo ci
    ) {
        if (!WeatherState.hasParticleColor()) return;

        int rgb = WeatherState.getParticleColor();
        float r = ((rgb >> 16) & 0xFF) / 255.0F;
        float g = ((rgb >>  8) & 0xFF) / 255.0F;
        float b = ( rgb        & 0xFF) / 255.0F;

        // Força a cor usando os métodos do Accessor
        this.bdbr$setParticleRed(r);
        this.bdbr$setParticleGreen(g);
        this.bdbr$setParticleBlue(b);
    }
}