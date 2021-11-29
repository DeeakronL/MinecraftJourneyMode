package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.client.event.ResearchEvent;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class ResearchGrinderPartBlock extends HorizontalBlock {
    private final int part;
    protected static final VoxelShape BASE_SLAB = Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_WEST = Block.makeCuboidShape(1.0D,1.0D,1.0D,16.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_EAST = Block.makeCuboidShape(0.0D,1.0D,1.0D,15.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHWEST = Block.makeCuboidShape(1.0D,1.0D,0.0D,16.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHEAST = Block.makeCuboidShape(0.0D,1.0D,0.0D,15.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SHAPE = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_WEST);
    protected static final VoxelShape BASE_SHAPE_EAST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_EAST);
    protected static final VoxelShape BASE_SHAPE_SOUTHWEST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_SOUTHWEST);
    protected static final VoxelShape BASE_SHAPE_SOUTHEAST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_SOUTHEAST);
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private final String type;
    private BlockPos pos1;
    private BlockPos pos2;
    private BlockPos pos3;


    public ResearchGrinderPartBlock(Properties properties, int part, String type){
        super(properties);
        this.part = part;
        this.type = type;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }


    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
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
            //worldIn.playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundCategory.BLOCKS, 0.10f, 1.0f);
        }
        if(entityIn instanceof ItemEntity){
            UUID id = ((ItemEntity) entityIn).getThrowerId();
            PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            ServerPlayerEntity player = players.getPlayerByUUID(id);
            MinecraftForge.EVENT_BUS.post(new ResearchEvent((ItemEntity) entityIn, player));
            worldIn.playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundCategory.BLOCKS, 0.10f, 1.0f);
            ((ItemEntity) entityIn).remove();


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
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;

        switch (part) {
            case 0:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.west();
                        pos2 = pos.west().south();
                        pos3 = pos.south();
                        break;
                    case SOUTH:
                        pos1 = pos.east();
                        pos2 = pos.east().north();
                        pos3 = pos.north();
                        break;
                    case WEST:
                        pos1 = pos.south();
                        pos2 = pos.south().east();
                        pos3 = pos.east();
                        break;
                    case EAST:
                        pos1 = pos.north();
                        pos2 = pos.north().west();
                        pos3 = pos.west();
                        break;
                }
                break;
            case 1:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.north();
                        pos2 = pos.north().east();
                        pos3 = pos.east();
                        break;
                    case SOUTH:
                        pos1 = pos.south();
                        pos2 = pos.south().west();
                        pos3 = pos.west();
                        break;
                    case WEST:
                        pos1 = pos.west();
                        pos2 = pos.west().north();
                        pos3 = pos.north();
                        break;
                    case EAST:
                        pos1 = pos.east();
                        pos2 = pos.east().south();
                        pos3 = pos.south();
                        break;
                }
                break;
            case 2:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.north().west();
                        pos2 = pos.north();
                        pos3 = pos.west();
                        break;
                    case SOUTH:
                        pos1 = pos.south().east();
                        pos2 = pos.south();
                        pos3 = pos.east();
                        break;
                    case WEST:
                        pos1 = pos.west().south();
                        pos2 = pos.west();
                        pos3 = pos.south();
                        break;
                    case EAST:
                        pos1 = pos.east().north();
                        pos2 = pos.east();
                        pos3 = pos.north();
                        break;
                }
                break;
        }
        if(worldIn.getBlockState(pos1).getBlock() instanceof ResearchGrinderBlock){
            journey_mode.LOGGER.info(worldIn.getBlockState(pos1).getBlock());
            worldIn.setBlockState(pos1, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos2).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos2, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos3).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos3, Blocks.AIR.getDefaultState());
        }
        super.onBlockHarvested(worldIn, pos, state, player);
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;

        switch (part) {
            case 0:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.west();
                        pos2 = pos.west().south();
                        pos3 = pos.south();
                        break;
                    case SOUTH:
                        pos1 = pos.east();
                        pos2 = pos.east().north();
                        pos3 = pos.north();
                        break;
                    case WEST:
                        pos1 = pos.south();
                        pos2 = pos.south().east();
                        pos3 = pos.east();
                        break;
                    case EAST:
                        pos1 = pos.north();
                        pos2 = pos.north().west();
                        pos3 = pos.west();
                        break;
                }
                break;
            case 1:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.north();
                        pos2 = pos.north().east();
                        pos3 = pos.east();
                        break;
                    case SOUTH:
                        pos1 = pos.south();
                        pos2 = pos.south().west();
                        pos3 = pos.west();
                        break;
                    case WEST:
                        pos1 = pos.west();
                        pos2 = pos.west().north();
                        pos3 = pos.north();
                        break;
                    case EAST:
                        pos1 = pos.east();
                        pos2 = pos.east().south();
                        pos3 = pos.south();
                        break;
                }
                break;
            case 2:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.north().west();
                        pos2 = pos.north();
                        pos3 = pos.west();
                        break;
                    case SOUTH:
                        pos1 = pos.south().east();
                        pos2 = pos.south();
                        pos3 = pos.east();
                        break;
                    case WEST:
                        pos1 = pos.west().south();
                        pos2 = pos.west();
                        pos3 = pos.south();
                        break;
                    case EAST:
                        pos1 = pos.east().north();
                        pos2 = pos.east();
                        pos3 = pos.north();
                        break;
                }
                break;
        }
        if(worldIn.getBlockState(pos1).getBlock() instanceof ResearchGrinderBlock){
            journey_mode.LOGGER.info(worldIn.getBlockState(pos1).getBlock());
            worldIn.setBlockState(pos1, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos2).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos2, Blocks.AIR.getDefaultState());
        }
        if(worldIn.getBlockState(pos3).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockState(pos3, Blocks.AIR.getDefaultState());
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
    }

    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        if (type == "wood"){
            return new ItemStack(ItemInit.WOODEN_RESEARCH_GRINDER.get());
        } else if (type == "iron"){
            return new ItemStack(ItemInit.IRON_RESEARCH_GRINDER.get());
        } else if (type == "diamond"){
            return new ItemStack(ItemInit.DIAMOND_RESEARCH_GRINDER.get());
        }
        return null;
    }


    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        switch (part) {
            case 0:
                switch (state.get(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHEAST;
                    case SOUTH: return BASE_SHAPE;
                    case WEST: return BASE_SHAPE_EAST;
                    case EAST: return BASE_SHAPE_SOUTHWEST;
                }
        }
        return null;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (part) {
            case 0:
                switch (state.get(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHEAST;
                    case SOUTH: return BASE_SHAPE;
                    case WEST: return BASE_SHAPE_EAST;
                    case EAST: return BASE_SHAPE_SOUTHWEST;
                }
        }
        return null;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (part) {
            case 0:
                switch (state.get(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHEAST;
                    case SOUTH: return BASE_SHAPE;
                    case WEST: return BASE_SHAPE_EAST;
                    case EAST: return BASE_SHAPE_SOUTHWEST;
                }
        }
        return null;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        switch (part) {
            case 0:
                switch (state.get(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.get(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHEAST;
                    case SOUTH: return BASE_SHAPE;
                    case WEST: return BASE_SHAPE_EAST;
                    case EAST: return BASE_SHAPE_SOUTHWEST;
                }
        }
        return null;
    }

    public VoxelShape getBaseShape() {
        switch (part) {
            case 0:
                return BASE_SHAPE_EAST;
            case 1:
                return BASE_SHAPE_SOUTHWEST;
            case 2:
                return BASE_SHAPE_SOUTHEAST;
        }
        return null;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        BlockPos pos1 = null;

        switch (part) {
            case 0:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.west();
                        break;
                    case SOUTH:
                        pos1 = pos.east();
                        break;
                    case WEST:
                        pos1 = pos.south();
                        break;
                    case EAST:
                        pos1 = pos.north();
                        break;
                }
                break;
            case 1:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.north();
                        break;
                    case SOUTH:
                        pos1 = pos.south();
                        break;
                    case WEST:
                        pos1 = pos.west();
                        break;
                    case EAST:
                        pos1 = pos.east();
                        break;
                }
                break;
            case 2:
                switch (state.get(FACING)){
                    case NORTH:
                        pos1 = pos.north().west();
                        break;
                    case SOUTH:
                        pos1 = pos.south().east();
                        break;
                    case WEST:
                        pos1 = pos.west().south();
                        break;
                    case EAST:
                        pos1 = pos.east().north();
                        break;
                }
                break;
        }
        return world.getBlockState(pos1).getLightValue(world, pos1);
        //return super.getLightValue(state, world, pos);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
