package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.block.InertCommandBlock;
import com.Deeakron.journey_mode.block.InertJigsawBlock;
import com.Deeakron.journey_mode.block.InertStructureBlock;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;

import net.minecraft.world.item.Item.Properties;

public class AetherialVoidDustItem extends Item {
    private boolean resultFlag = false;

    public AetherialVoidDustItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        resultFlag = false;
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();
        if (world.getBlockState(pos).getBlock().defaultBlockState().is(Blocks.COMMAND_BLOCK)) {
            Direction facing = world.getBlockState(pos).getValue(DirectionalBlock.FACING);
            Boolean conditional = world.getBlockState(pos).getValue(BlockStateProperties.CONDITIONAL);
            world.setBlockAndUpdate(pos, UnobtainBlockInit.INERT_COMMAND_BLOCK.get().defaultBlockState().setValue(InertCommandBlock.FACING, facing).setValue(InertCommandBlock.CONDITIONAL, conditional));
            resultFlag = true;
        } else if (world.getBlockState(pos).getBlock().defaultBlockState().is(Blocks.CHAIN_COMMAND_BLOCK)) {
            Direction facing = world.getBlockState(pos).getValue(DirectionalBlock.FACING);
            Boolean conditional = world.getBlockState(pos).getValue(BlockStateProperties.CONDITIONAL);
            world.setBlockAndUpdate(pos, UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get().defaultBlockState().setValue(InertCommandBlock.FACING, facing).setValue(InertCommandBlock.CONDITIONAL, conditional));
            resultFlag = true;
        } else if (world.getBlockState(pos).getBlock().defaultBlockState().is(Blocks.REPEATING_COMMAND_BLOCK)) {
            Direction facing = world.getBlockState(pos).getValue(DirectionalBlock.FACING);
            Boolean conditional = world.getBlockState(pos).getValue(BlockStateProperties.CONDITIONAL);
            world.setBlockAndUpdate(pos, UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get().defaultBlockState().setValue(InertCommandBlock.FACING, facing).setValue(InertCommandBlock.CONDITIONAL, conditional));
            resultFlag = true;
        } else if (world.getBlockState(pos).getBlock().defaultBlockState().is(Blocks.JIGSAW)) {
            JigsawOrientation orientation = world.getBlockState(pos).getValue(BlockStateProperties.ORIENTATION);
            world.setBlockAndUpdate(pos, UnobtainBlockInit.INERT_JIGSAW_BLOCK.get().defaultBlockState().setValue(InertJigsawBlock.ORIENTATION, orientation));
            resultFlag = true;
        } else if (world.getBlockState(pos).getBlock().defaultBlockState().is(Blocks.STRUCTURE_BLOCK)) {
            StructureMode mode = world.getBlockState(pos).getValue(BlockStateProperties.STRUCTUREBLOCK_MODE);
            world.setBlockAndUpdate(pos, UnobtainBlockInit.INERT_STRUCTURE_BLOCK.get().defaultBlockState().setValue(InertStructureBlock.MODE, mode));
            resultFlag = true;
        }

        if (resultFlag) {
            if(!world.isClientSide){player.playNotifySound(JMSounds.AETHERIAL_DEACTIVATE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}
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
                world.addParticle(ParticleTypes.SMOKE, d6, d7, d8, d2, d3, d4);
            }
            context.getItemInHand().shrink(1);
            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.PASS;
        }


    }
}