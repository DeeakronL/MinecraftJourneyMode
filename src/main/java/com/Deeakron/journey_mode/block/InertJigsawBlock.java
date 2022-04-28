package com.Deeakron.journey_mode.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;



import net.minecraft.state.EnumProperty;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;

public class InertJigsawBlock extends Block {
    public static final EnumProperty<JigsawOrientation> ORIENTATION = BlockStateProperties.ORIENTATION;

    public InertJigsawBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(ORIENTATION, JigsawOrientation.NORTH_UP));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(ORIENTATION, rot.getOrientation().func_235531_a_(state.get(ORIENTATION)));
    }


    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.with(ORIENTATION, mirrorIn.getOrientation().func_235531_a_(state.get(ORIENTATION)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getFace();
        Direction direction1;
        if (direction.getAxis() == Direction.Axis.Y) {
            direction1 = context.getPlacementHorizontalFacing().getOpposite();
        } else {
            direction1 = Direction.UP;
        }

        return this.getDefaultState().with(ORIENTATION, JigsawOrientation.func_239641_a_(direction, direction1));
    }

}