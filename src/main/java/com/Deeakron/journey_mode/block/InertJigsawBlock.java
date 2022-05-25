package com.Deeakron.journey_mode.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;



import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.FrontAndTop;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class InertJigsawBlock extends Block {
    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public InertJigsawBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ORIENTATION, JigsawOrientation.NORTH_UP));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(ORIENTATION, rot.rotation().rotate(state.getValue(ORIENTATION)));
    }


    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(ORIENTATION, mirrorIn.rotation().rotate(state.getValue(ORIENTATION)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getClickedFace();
        Direction direction1;
        if (direction.getAxis() == Direction.Axis.Y) {
            direction1 = context.getHorizontalDirection().getOpposite();
        } else {
            direction1 = Direction.UP;
        }

        return this.defaultBlockState().setValue(ORIENTATION, JigsawOrientation.fromFrontAndTop(direction, direction1));
    }

}