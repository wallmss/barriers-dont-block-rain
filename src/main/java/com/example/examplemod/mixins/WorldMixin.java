package com.example.examplemod.mixins;

import com.example.examplemod.HeightCache;
import com.example.examplemod.WeatherState;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {

    @Inject(
        method = "getPrecipitationHeight(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/util/BlockPos;",
        at = @At("RETURN"),
        cancellable = true
    )
    private void bdbr$adjustForBarriers(BlockPos pos, CallbackInfoReturnable<BlockPos> cir) {
        if (!WeatherState.barrierBypass) return;

        BlockPos vanilla = cir.getReturnValue();
        if (vanilla == null) return;

        World world = (World)(Object)this;
        BlockPos below = vanilla.down();
        if (below.getY() < 0) return;

        IBlockState belowState = world.getBlockState(below);
        if (!(belowState.getBlock() instanceof BlockBarrier)) return;

        int x = pos.getX();
        int z = pos.getZ();

        int cached = HeightCache.get(world, x, z);
        if (cached != Integer.MIN_VALUE) {
            cir.setReturnValue(new BlockPos(x, cached, z));
            return;
        }

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int result = 0;

        for (int y = below.getY(); y >= 0; y--) {
            mutable.set(x, y, z);
            IBlockState s = world.getBlockState(mutable);
            Material m = s.getBlock().getMaterial();

            if ((m.blocksMovement() || m.isLiquid()) && !(s.getBlock() instanceof BlockBarrier)) {
                result = y + 1;
                break;
            }
        }

        HeightCache.put(world, x, z, result);
        cir.setReturnValue(new BlockPos(x, result, z));
    }

    @Inject(
        method = "getRainStrength(F)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private void bdbr$getRainStrength(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (!WeatherState.hasWeatherOverride()) return;

        World world = (World)(Object)this;
        if (!world.isRemote) return;
        if (!"Client thread".equals(Thread.currentThread().getName())) return;

        switch (WeatherState.weather) {
            case CLEAR:
                cir.setReturnValue(0.0F);
                return;
            case RAIN:
            case SNOW:
            case THUNDER:
                cir.setReturnValue(1.0F);
                return;
            case VANILLA:
            default:
                return;
        }
    }

    @Inject(
        method = "getThunderStrength(F)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private void bdbr$getThunderStrength(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (!WeatherState.hasWeatherOverride()) return;

        World world = (World)(Object)this;
        if (!world.isRemote) return;
        if (!"Client thread".equals(Thread.currentThread().getName())) return;

        switch (WeatherState.weather) {
            case CLEAR:
            case RAIN:
            case SNOW:
                cir.setReturnValue(0.0F);
                return;
            case THUNDER:
                cir.setReturnValue(1.0F);
                return;
            case VANILLA:
            default:
                return;
        }
    }
}