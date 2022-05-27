package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.init.ItemInit;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.client.event.ResearchEvent;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.world.level.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

import net.minecraft.block.AbstractBlock.Properties;

public class ResearchGrinderPartBlock extends HorizontalDirectionalBlock {
    private final int part;
    protected static final VoxelShape BASE_SLAB = Block.box(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_WEST = Block.box(1.0D,1.0D,1.0D,16.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_EAST = Block.box(0.0D,1.0D,1.0D,15.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHWEST = Block.box(1.0D,1.0D,0.0D,16.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHEAST = Block.box(0.0D,1.0D,0.0D,15.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SHAPE = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_WEST);
    protected static final VoxelShape BASE_SHAPE_EAST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_EAST);
    protected static final VoxelShape BASE_SHAPE_SOUTHWEST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_SOUTHWEST);
    protected static final VoxelShape BASE_SHAPE_SOUTHEAST = VoxelShapes.or(BASE_SLAB, BASE_SLOPE_SOUTHEAST);
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
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
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    public void stepOn(World worldIn, BlockPos pos, Entity entityIn) {
        if(entityIn instanceof LivingEntity) {
            float damage = 1.0F;
            if (type == "wood") {
                damage = 1.0F;
            } else if (type == "iron") {
                damage = 1.5F;
            } else if (type == "diamond") {
                damage = 1.75F;
            }
            entityIn.hurt(JMDamageSources.RESEARCH_GRINDER, damage);
        }
        if(entityIn instanceof ItemEntity){
            UUID id = ((ItemEntity) entityIn).getThrower();
            PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            ServerPlayerEntity player = players.getPlayer(id);
            MinecraftForge.EVENT_BUS.post(new ResearchEvent((ItemEntity) entityIn, player));
            worldIn.playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundCategory.BLOCKS, 0.10f, 1.0f);
            entityIn.remove();


        }
        super.stepOn(worldIn, pos, entityIn);
    }

    public PushReaction getPistonPushReaction(BlockState state){
        return PushReaction.BLOCK;
    }



    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;

        switch (part) {
            case 0:
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)){
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
            worldIn.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
        }
        if(worldIn.getBlockState(pos2).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockAndUpdate(pos2, Blocks.AIR.defaultBlockState());
        }
        if(worldIn.getBlockState(pos3).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockAndUpdate(pos3, Blocks.AIR.defaultBlockState());
        }
        super.playerWillDestroy(worldIn, pos, state, player);
        if (type == "wood" && !player.isCreative()) {
            dropResources(state, worldIn, pos);
        } else {
            if(player.getMainHandItem().isCorrectToolForDrops(state) && !player.isCreative()) {
                dropResources(state, worldIn, pos);
            }
        }
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
    }

    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;

        switch (part) {
            case 0:
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)){
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
            worldIn.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
        }
        if(worldIn.getBlockState(pos2).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockAndUpdate(pos2, Blocks.AIR.defaultBlockState());
        }
        if(worldIn.getBlockState(pos3).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockAndUpdate(pos3, Blocks.AIR.defaultBlockState());
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
    }

    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        if (type == "wood"){
            return new ItemStack(ItemInit.WOODEN_RESEARCH_GRINDER.get());
        } else if (type == "iron"){
            return new ItemStack(ItemInit.IRON_RESEARCH_GRINDER.get());
        } else if (type == "diamond"){
            return new ItemStack(ItemInit.DIAMOND_RESEARCH_GRINDER.get());
        }
        return null;
    }


    public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        switch (part) {
            case 0:
                switch (state.getValue(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.getValue(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.getValue(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.getValue(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.getValue(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHEAST;
                    case SOUTH: return BASE_SHAPE;
                    case WEST: return BASE_SHAPE_EAST;
                    case EAST: return BASE_SHAPE_SOUTHWEST;
                }
        }
        return null;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        switch (part) {
            case 0:
                switch (state.getValue(FACING)) {
                    case NORTH: return BASE_SHAPE_EAST;
                    case SOUTH: return BASE_SHAPE_SOUTHWEST;
                    case WEST: return BASE_SHAPE;
                    case EAST: return BASE_SHAPE_SOUTHEAST;
                }

            case 1:
                switch (state.getValue(FACING)){
                    case NORTH: return BASE_SHAPE_SOUTHWEST;
                    case SOUTH: return BASE_SHAPE_EAST;
                    case WEST: return BASE_SHAPE_SOUTHEAST;
                    case EAST: return BASE_SHAPE;
                }
            case 2:
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)){
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
                switch (state.getValue(FACING)){
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
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
