package com.example.examplemod.mixins;

import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityFX.class)
public interface EntityFXAccessor {

    @Accessor("particleRed")
    void bdbr$setParticleRed(float red);

    @Accessor("particleGreen")
    void bdbr$setParticleGreen(float green);

    @Accessor("particleBlue")
    void bdbr$setParticleBlue(float blue);
}