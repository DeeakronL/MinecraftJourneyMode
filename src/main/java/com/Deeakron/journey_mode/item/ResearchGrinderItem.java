package com.Deeakron.journey_mode.item;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.item.Item.Properties;

public class ResearchGrinderItem extends BlockItem {
    public ResearchGrinderItem(Block blockIn, Properties builder) {super(blockIn, builder);}

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;
        switch (facing) {
            case NORTH:
                pos1 = pos.east();
                pos2 = pos.south();
                pos3 = pos.east().south();
                break;
            case SOUTH:
                pos1 = pos.west();
                pos2 = pos.north();
                pos3 = pos.west().north();
                break;
            case WEST:
                pos1 = pos.north();
                pos2 = pos.east();
                pos3 = pos.north().east();
                break;
            case EAST:
                pos1 = pos.south();
                pos2 = pos.west();
                pos3 = pos.south().west();
                break;
        }
        if(context.getLevel().getBlockState(pos1).canBeReplaced(context)  &&
                context.getLevel().getBlockState(pos2).canBeReplaced(context) &&
                context.getLevel().getBlockState(pos3).canBeReplaced(context)){
            return super.placeBlock(context, state);
        }

        return false;
    }
}
