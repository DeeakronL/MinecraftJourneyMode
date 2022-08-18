package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

import java.util.Random;

public class PaintBucketItem extends Item {
    private final Random random = new Random();
    public PaintBucketItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (world.getBlockState(pos).getBlock().defaultBlockState().is(Blocks.BARRIER)) {
            world.setBlockAndUpdate(pos, UnobtainBlockInit.PAINTED_BARRIER.get().defaultBlockState());
            if(!world.isClientSide){context.getPlayer().playNotifySound(JMSounds.BARRIER_PAINT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);}
            for (int i = 0; i < 50; i++) {
                double d0 = 3.0D;
                double d1 = 1.0D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextGaussian() * 0.02D;
                double d4 = random.nextGaussian() * 0.02D;
                double d5 = 0.5D - d0;
                double d6 = (double)pos.getX()  + random.nextDouble();// * d0 - 0.5D;
                double d7 = (double)pos.getY() + random.nextDouble();// * d1;
                double d8 = (double)pos.getZ() + random.nextDouble();// * d0 - 0.5D;
                world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, UnobtainBlockInit.PAINTED_BARRIER.get().defaultBlockState()), d6, d7, d8, d2, d3, d4);
            }
            context.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        if (world.getBlockState(pos).getBlock().defaultBlockState().is(Blocks.LIGHT)) {
            world.setBlockAndUpdate(pos, UnobtainBlockInit.PAINTED_LIGHT.get().defaultBlockState());
            if(!world.isClientSide){context.getPlayer().playNotifySound(JMSounds.LIGHT_PAINT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);}
            for (int i = 0; i < 50; i++) {
                double d0 = 3.0D;
                double d1 = 1.0D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextGaussian() * 0.02D;
                double d4 = random.nextGaussian() * 0.02D;
                double d5 = 0.5D - d0;
                double d6 = (double)pos.getX()  + random.nextDouble();// * d0 - 0.5D;
                double d7 = (double)pos.getY() + random.nextDouble();// * d1;
                double d8 = (double)pos.getZ() + random.nextDouble();// * d0 - 0.5D;
                world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, UnobtainBlockInit.PAINTED_LIGHT.get().defaultBlockState()), d6, d7, d8, d2, d3, d4);
            }
            context.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return InteractionResult.PASS;
    }

}
