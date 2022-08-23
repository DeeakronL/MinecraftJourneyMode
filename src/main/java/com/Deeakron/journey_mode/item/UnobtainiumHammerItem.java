package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class UnobtainiumHammerItem extends Item {
    public UnobtainiumHammerItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Boolean broken = false;
        if(!world.isClientSide){context.getPlayer().playNotifySound(JMSounds.HAMMER_USE.get(), SoundSource.PLAYERS, 0.2F, 1.0F);}
        for (int i = -3; i < 4; i++) {
            if (pos.getY() + i < 0) {
                continue;
            }
            for (int j = -3; j < 4; j++) {
                for (int k = -3; k < 4; k++) {
                    BlockPos temp = new BlockPos(pos.getX() + j, pos.getY() + i, pos.getZ() + k);
                    if (world.getBlockState(temp).is(Blocks.LIGHT)) {
                        world.setBlockAndUpdate(temp, UnobtainBlockInit.BROKEN_LIGHT.get().defaultBlockState());
                        if (!broken) {
                            broken = true;
                        }
                    }
                }
            }
        }
        if (broken) {
            if(!world.isClientSide){context.getPlayer().playNotifySound(JMSounds.LIGHT_BREAK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);}
        }
        return InteractionResult.SUCCESS;
    }
}
