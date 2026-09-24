package com.example.examplemod.mixins;

import com.example.examplemod.BdbrLog;
import com.example.examplemod.WeatherState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    private static int BDBR_COLOR_LOGS = 0;

    @Redirect(
        method = "renderRainSnow(F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/WorldRenderer;color(FFFF)Lnet/minecraft/client/renderer/WorldRenderer;"
        )
    )
    private WorldRenderer bdbr$overrideRainColor(WorldRenderer wr,
                                                 float r, float g, float b, float a) {
        float nr = r, ng = g, nb = b, na = a;

        if (WeatherState.hasParticleColor()) {
            int rgb = WeatherState.getCurrentRGB();
            nr = ((rgb >> 16) & 0xFF) / 255.0F;
            ng = ((rgb >>  8) & 0xFF) / 255.0F;
            nb = ( rgb        & 0xFF) / 255.0F;

            if (BDBR_COLOR_LOGS < 3) {
                BDBR_COLOR_LOGS++;
                BdbrLog.log("renderRainSnow color → #" + String.format("%06X", rgb)
                        + " mode=" + WeatherState.getColorMode().name());
            }
        }

        if (WeatherState.hasWeatherOverride()) {
            na = a * WeatherState.rainStrength;
        }

        return wr.color(nr, ng, nb, na);
    }

    @Redirect(
        method = "renderRainSnow(F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getPrecipitationHeight(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/util/BlockPos;"
        )
    )
    private BlockPos bdbr$forceHeightThroughBlocks(World world, BlockPos pos) {
        if (WeatherState.weatherThroughBlocks) {
            return new BlockPos(pos.getX(), 0, pos.getZ());
        }
        return world.getPrecipitationHeight(pos);
    }
}