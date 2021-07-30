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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ResearchGrinderPartBlock extends Block {
    private final int part;

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


}
