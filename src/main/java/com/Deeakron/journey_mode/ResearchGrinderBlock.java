package com.Deeakron.journey_mode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ResearchGrinderBlock extends Block {
    protected static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(0.0D,0.0D,0.0D,32.0D,16.0D,32.0D);
    private final String type;

    public ResearchGrinderBlock(Properties properties, String type) {
        super(properties);
        this.type = type;
    }

    public void onBlockPlacedBy(World worldin, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack){
        if (this.type == "wood"){
            worldin.setBlockState(pos.east(), BlockInit.WOODEN_RESEARCH_GRINDER_PART_0.get().getDefaultState());
            worldin.setBlockState(pos.south(), BlockInit.WOODEN_RESEARCH_GRINDER_PART_1.get().getDefaultState());
            worldin.setBlockState(pos.east().south(), BlockInit.WOODEN_RESEARCH_GRINDER_PART_2.get().getDefaultState());
            super.onBlockPlacedBy(worldin, pos, state, placer, stack);
        }

    }

    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        worldIn.setBlockState(pos.east(), Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.south(), Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.east().south(), Blocks.AIR.getDefaultState());
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.GENERIC, 1.0F);
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return BASE_SHAPE;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BASE_SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BASE_SHAPE;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return BASE_SHAPE;
    }

    public static VoxelShape getBaseShape() {
        return BASE_SHAPE;
    }
}
