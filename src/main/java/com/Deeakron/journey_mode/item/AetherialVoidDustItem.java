package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.block.InertCommandBlock;
import com.Deeakron.journey_mode.block.InertJigsawBlock;
import com.Deeakron.journey_mode.block.InertStructureBlock;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;

public class AetherialVoidDustItem extends Item {
    public AetherialVoidDustItem(Properties properties) {
        super(properties);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.COMMAND_BLOCK)) {
            Direction facing = world.getBlockState(pos).get(DirectionalBlock.FACING);
            Boolean conditional = world.getBlockState(pos).get(BlockStateProperties.CONDITIONAL);
            world.setBlockState(pos, UnobtainBlockInit.INERT_COMMAND_BLOCK.get().getDefaultState().with(InertCommandBlock.FACING, facing).with(InertCommandBlock.CONDITIONAL, conditional));
            if(!world.isRemote){player.playSound(JMSounds.AETHERIAL_DEACTIVATE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}
            return ActionResultType.func_233537_a_(world.isRemote);
        } else if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.CHAIN_COMMAND_BLOCK)) {
            Direction facing = world.getBlockState(pos).get(DirectionalBlock.FACING);
            Boolean conditional = world.getBlockState(pos).get(BlockStateProperties.CONDITIONAL);
            world.setBlockState(pos, UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get().getDefaultState().with(InertCommandBlock.FACING, facing).with(InertCommandBlock.CONDITIONAL, conditional));
            if(!world.isRemote){player.playSound(JMSounds.AETHERIAL_DEACTIVATE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}
            return ActionResultType.func_233537_a_(world.isRemote);
        } else if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.REPEATING_COMMAND_BLOCK)) {
            Direction facing = world.getBlockState(pos).get(DirectionalBlock.FACING);
            Boolean conditional = world.getBlockState(pos).get(BlockStateProperties.CONDITIONAL);
            world.setBlockState(pos, UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get().getDefaultState().with(InertCommandBlock.FACING, facing).with(InertCommandBlock.CONDITIONAL, conditional));
            if(!world.isRemote){player.playSound(JMSounds.AETHERIAL_DEACTIVATE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}
            return ActionResultType.func_233537_a_(world.isRemote);
        } else if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.JIGSAW)) {
            JigsawOrientation orientation = world.getBlockState(pos).get(BlockStateProperties.ORIENTATION);
            world.setBlockState(pos, UnobtainBlockInit.INERT_JIGSAW_BLOCK.get().getDefaultState().with(InertJigsawBlock.ORIENTATION, orientation));
            if(!world.isRemote){player.playSound(JMSounds.AETHERIAL_DEACTIVATE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}
            return ActionResultType.func_233537_a_(world.isRemote);
        } else if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.STRUCTURE_BLOCK)) {
            StructureMode mode = world.getBlockState(pos).get(BlockStateProperties.STRUCTURE_BLOCK_MODE);
            world.setBlockState(pos, UnobtainBlockInit.INERT_STRUCTURE_BLOCK.get().getDefaultState().with(InertStructureBlock.MODE, mode));
            if(!world.isRemote){player.playSound(JMSounds.AETHERIAL_DEACTIVATE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return ActionResultType.PASS;
    }
}