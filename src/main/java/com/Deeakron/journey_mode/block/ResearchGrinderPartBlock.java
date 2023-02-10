package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.init.ItemInit;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.client.event.ResearchEvent;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class ResearchGrinderPartBlock extends HorizontalDirectionalBlock {
    private final int part;
    protected static final VoxelShape BASE_SLAB = Block.box(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_WEST = Block.box(1.0D,1.0D,1.0D,16.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_EAST = Block.box(0.0D,1.0D,1.0D,15.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHWEST = Block.box(1.0D,1.0D,0.0D,16.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHEAST = Block.box(0.0D,1.0D,0.0D,15.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SHAPE = Shapes.or(BASE_SLAB, BASE_SLOPE_WEST);
    protected static final VoxelShape BASE_SHAPE_EAST = Shapes.or(BASE_SLAB, BASE_SLOPE_EAST);
    protected static final VoxelShape BASE_SHAPE_SOUTHWEST = Shapes.or(BASE_SLAB, BASE_SLOPE_SOUTHWEST);
    protected static final VoxelShape BASE_SHAPE_SOUTHEAST = Shapes.or(BASE_SLAB, BASE_SLOPE_SOUTHEAST);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final String type;
    private BlockPos pos1;
    private BlockPos pos2;
    private BlockPos pos3;


    public ResearchGrinderPartBlock(Properties properties, int part, String type){
        super(properties);
        this.part = part;
        this.type = type;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
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
            ServerPlayer player = players.getPlayer(id);
            MinecraftForge.EVENT_BUS.post(new ResearchEvent((ItemEntity) entityIn, player));
            worldIn.playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundSource.BLOCKS, 0.10f, 1.0f);
            entityIn.remove(Entity.RemovalReason.DISCARDED);


        }
        super.stepOn(worldIn, pos, state, entityIn);
    }

    public PushReaction getPistonPushReaction(BlockState state){
        return PushReaction.BLOCK;
    }



    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, BlockPathTypes type) {
        return false;
    }

    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
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

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
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

    public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
        if (type == "wood"){
            return new ItemStack(ItemInit.WOODEN_RESEARCH_GRINDER.get());
        } else if (type == "iron"){
            return new ItemStack(ItemInit.IRON_RESEARCH_GRINDER.get());
        } else if (type == "diamond"){
            return new ItemStack(ItemInit.DIAMOND_RESEARCH_GRINDER.get());
        }
        return null;
    }


    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
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

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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

    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
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
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
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
        return world.getBlockState(pos1).getLightEmission(world, pos1);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
