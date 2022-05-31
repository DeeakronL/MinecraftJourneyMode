package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.init.BlockInit;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.client.sound.ResearchGrinderSound;
import com.Deeakron.journey_mode.client.event.ResearchEvent;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.UUID;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class ResearchGrinderBlock extends HorizontalDirectionalBlock {
    protected static final VoxelShape BASE_SLAB = Block.box(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_WEST = Block.box(1.0D,1.0D,1.0D,16.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_EAST = Block.box(0.0D,1.0D,1.0D,15.0D,12.0D,16.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHWEST = Block.box(1.0D,1.0D,0.0D,16.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SLOPE_SOUTHEAST = Block.box(0.0D,1.0D,0.0D,15.0D,12.0D,15.0D);
    protected static final VoxelShape BASE_SHAPE = Shapes.or(BASE_SLAB, BASE_SLOPE_WEST);
    protected static final VoxelShape BASE_SHAPE_EAST = Shapes.or(BASE_SLAB, BASE_SLOPE_EAST);
    protected static final VoxelShape BASE_SHAPE_SOUTHWEST = Shapes.or(BASE_SLAB, BASE_SLOPE_SOUTHWEST);
    protected static final VoxelShape BASE_SHAPE_SOUTHEAST = Shapes.or(BASE_SLAB, BASE_SLOPE_SOUTHEAST);
    private final String type;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private BlockPos pos1;
    private BlockPos pos2;
    private BlockPos pos3;

    private ResearchGrinderSound grinderSound = null;
    private int isGrinding = 0;


    public ResearchGrinderBlock(Properties properties, String type) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
        this.type = type;
    }

    public PushReaction getPistonPushReaction(BlockState state){
        return PushReaction.BLOCK;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public void setPlacedBy(Level worldin, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack){
        if(!worldin.isClientSide){
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;
        switch (state.getValue(FACING)) {
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
            worldin.setBlockAndUpdate(pos1, BlockInit.WOODEN_RESEARCH_GRINDER_PART_0.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            worldin.setBlockAndUpdate(pos2, BlockInit.WOODEN_RESEARCH_GRINDER_PART_1.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            worldin.setBlockAndUpdate(pos3, BlockInit.WOODEN_RESEARCH_GRINDER_PART_2.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            super.setPlacedBy(worldin, pos, state, placer, stack);
        } else if (this.type == "iron"){
            worldin.setBlockAndUpdate(pos1, BlockInit.IRON_RESEARCH_GRINDER_PART_0.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            worldin.setBlockAndUpdate(pos2, BlockInit.IRON_RESEARCH_GRINDER_PART_1.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            worldin.setBlockAndUpdate(pos3, BlockInit.IRON_RESEARCH_GRINDER_PART_2.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            super.setPlacedBy(worldin, pos, state, placer, stack);
        } else if (this.type == "diamond"){
            worldin.setBlockAndUpdate(pos1, BlockInit.DIAMOND_RESEARCH_GRINDER_PART_0.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            worldin.setBlockAndUpdate(pos2, BlockInit.DIAMOND_RESEARCH_GRINDER_PART_1.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            worldin.setBlockAndUpdate(pos3, BlockInit.DIAMOND_RESEARCH_GRINDER_PART_2.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
            super.setPlacedBy(worldin, pos, state, placer, stack);
        }
        }

    }

    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;
        switch (state.getValue(FACING)) {
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

    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        BlockPos pos3 = null;
        switch (state.getValue(FACING)) {
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
            worldIn.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
        }
        if(worldIn.getBlockState(pos2).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockAndUpdate(pos2, Blocks.AIR.defaultBlockState());
        }
        if(worldIn.getBlockState(pos3).getBlock() instanceof ResearchGrinderPartBlock){
            worldIn.setBlockAndUpdate(pos3, Blocks.AIR.defaultBlockState());
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @OnlyIn(Dist.CLIENT)
    public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
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
            entityIn.hurt(JMDamageSources.RESEARCH_GRINDER, damage);
        }
        if(entityIn instanceof ItemEntity){
            UUID id = ((ItemEntity) entityIn).getThrower();
            PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            ServerPlayer player = players.getPlayer(id);
            MinecraftForge.EVENT_BUS.post(new ResearchEvent((ItemEntity) entityIn, player));
            entityIn.remove(Entity.RemovalReason.DISCARDED);
            worldIn.playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundSource.BLOCKS, 0.10f, 1.0f);

        }

        super.stepOn(worldIn, pos, state, entityIn);
    }

    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, BlockPathTypes type) {
        return false;
    }

    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        switch (state.getValue(FACING)) {
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

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
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

    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
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
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
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

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
