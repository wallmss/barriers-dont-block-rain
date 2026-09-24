package com.example.examplemod.mixins;

import com.example.examplemod.WeatherState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityFX.class)
public abstract class EntityFXMixin extends EntityFX {

    // Construtor obrigatório, pois estamos estendendo EntityFX
    protected EntityFXMixin() {
        super(null, 0, 0, 0);
    }

    /**
     * Injeta no início do método renderParticle para forçar a cor da partícula.
     * Isso garante que a cor seja aplicada antes de qualquer cálculo de renderização.
     */
    @Inject(
        method = "renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V",
        at = @At("HEAD")
    )
    private void bdbr$forceParticleColor(
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
        // Se não houver cor definida pelo usuário, não faz nada
        if (!WeatherState.hasParticleColor()) return;

        int rgb = WeatherState.getParticleColor();
        float r = ((rgb >> 16) & 0xFF) / 255.0F;
        float g = ((rgb >>  8) & 0xFF) / 255.0F;
        float b = ( rgb        & 0xFF) / 255.0F;

        // Agora que estendemos EntityFX, podemos acessar os campos diretamente
        this.particleRed   = r;
        this.particleGreen = g;
        this.particleBlue  = b;
    }
}