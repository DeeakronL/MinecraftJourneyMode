package com.Deeakron.journey_mode.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class InertCommandBlock extends Block {
    public static final BooleanProperty CONDITIONAL = BlockStateProperties.CONDITIONAL;
    public static final DirectionProperty FACING = DirectionalBlock.FACING;


    public InertCommandBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(CONDITIONAL, false);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(CONDITIONAL);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(CONDITIONAL, false);
    }
}
