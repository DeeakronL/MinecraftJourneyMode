package com.Deeakron.journey_mode.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

public class InertStructureBlock extends Block {
    public InertStructureBlock(AbstractBlock.Properties properties) {
        super(properties);
    }
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
