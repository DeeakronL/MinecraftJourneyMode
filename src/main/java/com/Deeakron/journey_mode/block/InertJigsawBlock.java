package com.Deeakron.journey_mode.block;

import net.minecraft.world.level.block.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;



import net.minecraft.world.level.block.state.properties.EnumProperty;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class InertJigsawBlock extends Block {
    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public InertJigsawBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ORIENTATION, FrontAndTop.NORTH_UP));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(ORIENTATION, rot.rotation().rotate(state.getValue(ORIENTATION)));
    }


    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(ORIENTATION, mirrorIn.rotation().rotate(state.getValue(ORIENTATION)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        Direction direction1;
        if (direction.getAxis() == Direction.Axis.Y) {
            direction1 = context.getHorizontalDirection().getOpposite();
        } else {
            direction1 = Direction.UP;
        }

        return this.defaultBlockState().setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, direction1));
    }

}