package com.Deeakron.journey_mode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class ResearchGrinderItem extends BlockItem {
    public ResearchGrinderItem(Block blockIn, Properties builder) {super(blockIn, builder);}

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        Direction facing = context.getPlacementHorizontalFacing().getOpposite();
        BlockPos pos = context.getPos();
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
        if(context.getWorld().getBlockState(pos1).isReplaceable(context)  &&
                context.getWorld().getBlockState(pos2).isReplaceable(context) &&
                context.getWorld().getBlockState(pos3).isReplaceable(context)){
            return super.placeBlock(context, state);
        }

        return false;
    }
}
