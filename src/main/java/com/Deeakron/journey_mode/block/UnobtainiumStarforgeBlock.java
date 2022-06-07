package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.init.JMTileEntityTypes;
import com.Deeakron.journey_mode.container.StarforgeItemHandler;
import com.Deeakron.journey_mode.tileentity.UnobtainiumStarforgeTileEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraft.world.InteractionHand;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class UnobtainiumStarforgeBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public UnobtainiumStarforgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    public boolean hasBlockEntity(BlockState state) {
        return true;
    }

    /*public BlockEntity createBlockEntity(BlockState state, BlockGetter world) {
        return JMTileEntityTypes.UNOBTAINIUM_STARFORGE.get().create();
    }*/

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, LIT);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if(stack.hasCustomHoverName()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if(tile instanceof UnobtainiumStarforgeTileEntity) {
                ((UnobtainiumStarforgeTileEntity) tile).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockstate, Level worldIn, BlockPos pos) {
        return super.getAnalogOutputSignal(blockstate, worldIn, pos);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn != null && !worldIn.isClientSide) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof UnobtainiumStarforgeTileEntity) {
                NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tile, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {


        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof UnobtainiumStarforgeTileEntity && state.getBlock() != newState.getBlock()) {
            UnobtainiumStarforgeTileEntity starforge = (UnobtainiumStarforgeTileEntity) tile;
            ((StarforgeItemHandler) starforge.getInventory()).toNonNullList().forEach(item -> {
                ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), item);
                worldIn.addFreshEntity(itemEntity);
            });
        }

        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
            worldIn.removeBlockEntity(pos);
        }
    }

    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (state.getValue(LIT)) {
            worldIn.setBlock(pos, state.cycle(LIT), 2);
        }

    }
}
