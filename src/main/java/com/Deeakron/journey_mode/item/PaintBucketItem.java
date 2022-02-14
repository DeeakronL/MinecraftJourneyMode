package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PaintBucketItem extends Item {
    public PaintBucketItem(Properties properties) {
        super(properties);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.BARRIER)) {
            world.setBlockState(pos, UnobtainBlockInit.PAINTED_BARRIER.get().getDefaultState());
            if(!world.isRemote){context.getPlayer().playSound(JMSounds.BARRIER_PAINT.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}
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
                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, UnobtainBlockInit.PAINTED_BARRIER.get().getDefaultState()), d6, d7, d8, d2, d3, d4);
            }
            context.getItem().shrink(1);
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return ActionResultType.PASS;
    }

}
