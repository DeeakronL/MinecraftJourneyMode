package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.init.BlockInit;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.client.sound.ResearchGrinderSound;
import com.Deeakron.journey_mode.client.event.ResearchEvent;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.server.management.PlayerList;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class ResearchGrinderBlock extends HorizontalBlock {
    protected static final VoxelShape BASE_SLAB = Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_WEST = Block.makeCuboidShape(1.0D,1.0D,1.0D,16.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_EAST = Block.makeCuboidShape(0.0D,1.0D,1.0D,15.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHWEST = Block.makeCuboidShape(1.0D,1.0D,0.0D,16.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHEAST = Block.makeCuboidShape(0.0D,1.0D,0.0D,15.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SHAPE = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_WEST);
    protected static final VoxelShape BASE_SHAPE_EAST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_EAST);
    protected static final VoxelShape BASE_SHAPE_SOUTHWEST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_SOUTHWEST);
    protected static final VoxelShape BASE_SHAPE_SOUTHEAST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_SOUTHEAST);
    private final String type;
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private BlockPos pos1;
    private BlockPos pos2;
    private BlockPos pos3;

    private ResearchGrinderSound grinderSound = null;
    private int isGrinding = 0;


    public ResearchGrinderBlock(Properties properties, String type) {
        super(properties);
        setDefaultState(this.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
        this.type = type;
    }

    public PushReaction getPushReaction(BlockState state){
        return PushReaction.BLOCK;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World worldin, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack){
        if(!worldin.isRemote){
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;
        switch (state.get(FACING)) {
            case NORTH:
                pos1 = pos.east();
                pos2 = pos.south();
                pos3 = pos.east().south();
                break;
            case SOUTH:
                pos1 = pos.west();
                pos2 = pos.north();
                pos3 = pos.west().north();
                break;
            case WEST:
                pos1 = pos.north();
                pos2 = pos.east();
                pos3 = pos.north().east();
                break;
            case EAST:
                pos1 = pos.south();
                pos2 = pos.west();
                pos3 = pos.south().west();
                break;
        }

        if (this.type == "wood"){
            worldin.setBlockState(pos1, BlockInit.WOODEN_RESEARCH_GRINDER_PART_0.get().getDefaultState().with(FACING, state.get(FACING)));
            worldin.setBlockState(pos2, BlockInit.WOODEN_RESEARCH_GRINDER_PART_1.get().getDefaultState().with(FACING, state.get(FACING)));
            worldin.setBlockState(pos3, BlockInit.WOODEN_RESEARCH_GRINDER_PART_2.get().getDefaultState().with(FACING, state.get(FACING)));
            super.onBlockPlacedBy(worldin, pos, state, placer, stack);
        } else if (this.type == "iron"){
            worldin.setBlockState(pos1, BlockInit.IRON_RESEARCH_GRINDER_PART_0.get().getDefaultState().with(FACING, state.get(FACING)));
            worldin.setBlockState(pos2, BlockInit.IRON_RESEARCH_GRINDER_PART_1.get().getDefaultState().with(FACING, state.get(FACING)));
            worldin.setBlockState(pos3, BlockInit.IRON_RESEARCH_GRINDER_PART_2.get().getDefaultState().with(FACING, state.get(FACING)));
            super.onBlockPlacedBy(worldin, pos, state, placer, stack);
        } else if (this.type == "diamond"){
            worldin.setBlockState(pos1, BlockInit.DIAMOND_RESEARCH_GRINDER_PART_0.get().getDefaultState().with(FACING, state.get(FACING)));
            worldin.setBlockState(pos2, BlockInit.DIAMOND_RESEARCH_GRINDER_PART_1.get().getDefaultState().with(FACING, state.get(FACING)));
            worldin.setBlockState(pos3, BlockInit.DIAMOND_RESEARCH_GRINDER_PART_2.get().getDefaultState().with(FACING, state.get(FACING)));
            super.onBlockPlacedBy(worldin, pos, state, placer, stack);
        }
        }

    }

    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;
        switch (state.get(FACING)) {
            case NORTH:
                pos1 = pos.east();
                pos2 = pos.south();
                pos3 = pos.east().south();
                break;
            case SOUTH:
                pos1 = pos.west();
                pos2 = pos.north();
                pos3 = pos.west().north();
                break;
            case WEST:
                pos1 = pos.north();
                pos2 = pos.east();
                pos3 = pos.north().east();
                break;
            case EAST:
                pos1 = pos.south();
                pos2 = pos.west();
                pos3 = pos.south().west();
                break;
        }
        if(worldIn.getBlockState(pos1).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos1, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos2).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos2, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos3).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos3, Blocks.AIR.getDefaultState());
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;
        switch (state.get(FACING)) {
            case NORTH:
                pos1 = pos.east();
                pos2 = pos.south();
                pos3 = pos.east().south();
                break;
            case SOUTH:
                pos1 = pos.west();
                pos2 = pos.north();
                pos3 = pos.west().north();
                break;
            case WEST:
                pos1 = pos.north();
                pos2 = pos.east();
                pos3 = pos.north().east();
                break;
            case EAST:
                pos1 = pos.south();
                pos2 = pos.west();
                pos3 = pos.south().west();
                break;
        }
        if(worldIn.getBlockState(pos1).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos1, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos2).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos2, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos3).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos3, Blocks.AIR.getDefaultState());
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @OnlyIn(Dist.CLIENT)
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if(grinderSound == null) {
            grinderSound = new ResearchGrinderSound(this, pos);
        }

        if(entityIn instanceof LivingEntity) {
            float damage = 1.0F;
            if (type == "wood") {
                damage = 1.0F;
            } else if (type == "iron") {
                damage = 1.5F;
            } else if (type == "diamond") {
                damage = 1.75F;
            }
            entityIn.attackEntityFrom(JMDamageSources.RESEARCH_GRINDER, damage);
        }
        if(entityIn instanceof ItemEntity){
            UUID id = ((ItemEntity) entityIn).getThrowerId();
            PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            ServerPlayerEntity player = players.getPlayerByUUID(id);
            MinecraftForge.EVENT_BUS.post(new ResearchEvent((ItemEntity) entityIn, player));
            entityIn.remove();
            worldIn.playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundCategory.BLOCKS, 0.10f, 1.0f);

        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        switch (state.get(FACING)) {
            case NORTH:
                return BASE_SHAPE;
            case SOUTH:
                return BASE_SHAPE_SOUTHEAST;
            case WEST:
                return BASE_SHAPE_SOUTHWEST;
            case EAST:
                return BASE_SHAPE_EAST;

        }
        return null;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return BASE_SHAPE;
            case SOUTH:
                return BASE_SHAPE_SOUTHEAST;
            case WEST:
                return BASE_SHAPE_SOUTHWEST;
            case EAST:
                return BASE_SHAPE_EAST;

        }
        return null;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return BASE_SHAPE;
            case SOUTH:
                return BASE_SHAPE_SOUTHEAST;
            case WEST:
                return BASE_SHAPE_SOUTHWEST;
            case EAST:
                return BASE_SHAPE_EAST;

        }
        return null;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return BASE_SHAPE;
            case SOUTH:
                return BASE_SHAPE_SOUTHEAST;
            case WEST:
                return BASE_SHAPE_SOUTHWEST;
            case EAST:
                return BASE_SHAPE_EAST;

        }
        return null;
    }

    public static VoxelShape getBaseShape() {
        return BASE_SHAPE;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
