package com.Deeakron.journey_mode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ResearchGrinderPartBlock extends Block {
    private final int part;
    protected static final VoxelShape BASE_SLAB = Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_0 = Block.makeCuboidShape(0.0D,1.0D,1.0D,15.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_1 = Block.makeCuboidShape(1.0D,1.0D,0.0D,16.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SLOPE_2 = Block.makeCuboidShape(0.0D,1.0D,0.0D,15.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SHAPE_0 = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_0);
    protected static final VoxelShape BASE_SHAPE_1 = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_1);
    protected static final VoxelShape BASE_SHAPE_2 = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_2);


    public ResearchGrinderPartBlock(Properties properties, int part){
        super(properties);
        this.part = part;
    }


    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if(entityIn instanceof LivingEntity) {
            entityIn.attackEntityFrom(DamageSource.GENERIC, 1.0F);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    public PushReaction getPushReaction(BlockState state){
        return PushReaction.BLOCK;
    }



    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if(part == 0){
            worldIn.setBlockState(pos.west(), Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos.south(), Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos.west().south(), Blocks.AIR.getDefaultState());
            super.onBlockHarvested(worldIn, pos, state, player);}
        else if (part == 1){
            worldIn.setBlockState(pos.north(), Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos.east(), Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos.east().north(), Blocks.AIR.getDefaultState());
            super.onBlockHarvested(worldIn, pos, state, player);}
        else if (part == 2) {worldIn.setBlockState(pos.west(), Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos.north(), Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos.west().north(), Blocks.AIR.getDefaultState());
            super.onBlockHarvested(worldIn, pos, state, player);}
        else {
            super.onBlockHarvested(worldIn, pos, state, player);
        }


    }

    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        switch (part) {
            case 0:
                return BASE_SHAPE_0;
            case 1:
                return BASE_SHAPE_1;
            case 2:
                return BASE_SHAPE_2;
        }
        return null;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (part) {
            case 0:
                return BASE_SHAPE_0;
            case 1:
                return BASE_SHAPE_1;
            case 2:
                return BASE_SHAPE_2;
        }
        return null;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (this.part) {
            case 0:
                return BASE_SHAPE_0;
            case 1:
                return BASE_SHAPE_1;
            case 2:
                return BASE_SHAPE_2;
        }
        return null;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        switch (this.part) {
            case 0:
                return BASE_SHAPE_0;
            case 1:
                return BASE_SHAPE_1;
            case 2:
                return BASE_SHAPE_2;
        }
        return null;
    }

    public VoxelShape getBaseShape() {
        switch (this.part) {
            case 0:
                return BASE_SHAPE_0;
            case 1:
                return BASE_SHAPE_1;
            case 2:
                return BASE_SHAPE_2;
        }
        return null;
    }


}
