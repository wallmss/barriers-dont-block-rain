package com.example.examplemod.mixins;

import com.example.examplemod.HeightCache;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldBlockMixin {

    @Inject(
        method = "setBlockState(Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z",
        at = @At("HEAD")
    )
    private void bdbr$onSetBlockState(BlockPos pos, IBlockState state, int flags,
                                      CallbackInfoReturnable<Boolean> cir) {
        World world = (World)(Object)this;
        if (!world.isRemote) return;

        IBlockState old = world.getBlockState(pos);
        boolean wasBarrier = old.getBlock() instanceof BlockBarrier;
        boolean isBarrier  = state.getBlock() instanceof BlockBarrier;

        if (wasBarrier || isBarrier) {
            HeightCache.invalidate(pos.getX(), pos.getZ());
        }
    }
}