package com.Deeakron.journey_mode.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockRenderType;
import net.minecraft.world.level.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.StructureMode;

public class InertStructureBlock extends Block {
    public static final EnumProperty<StructureMode> MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;
    public InertStructureBlock(AbstractBlock.Properties properties) {
        super(properties);
    }
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(MODE, StructureMode.DATA);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(MODE);
    }

}
