package com.Deeakron.journey_mode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;

public class ResearchGrinderItem extends BlockItem {
    public ResearchGrinderItem(Block blockIn, Properties builder) {super(blockIn, builder);}

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        if(context.getWorld().getBlockState(context.getPos().east()).isAir()  &&
                context.getWorld().getBlockState(context.getPos().south()).isAir() &&
                context.getWorld().getBlockState(context.getPos().east().south()).isAir()){
            return super.placeBlock(context, state);
        }
        return false;
    }
}
