package com.example.examplemod.mixins;

import com.example.examplemod.WeatherState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityFX.class)
public abstract class EntityFXMixin {

    @Shadow protected float particleRed;
    @Shadow protected float particleGreen;
    @Shadow protected float particleBlue;

    @Inject(
        method = "renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V",
        at = @At("HEAD")
    )
    private void bdbr$forceParticleColor(
            WorldRenderer w, Entity e, float pt,
            float rx, float rz, float ryz, float rxy, float rxz, CallbackInfo ci) {
        if (!WeatherState.hasParticleColor()) return;

        int rgb = WeatherState.getCurrentRGB();
        this.particleRed   = ((rgb >> 16) & 0xFF) / 255.0F;
        this.particleGreen = ((rgb >>  8) & 0xFF) / 255.0F;
        this.particleBlue  = ( rgb        & 0xFF) / 255.0F;
    }
}