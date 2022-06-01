package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.container.UnobtainiumAntikytheraContainer;
import com.Deeakron.journey_mode.container.UnobtainiumAntikytheraContainerProvider;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class UnobtainiumAntikytheraBlock extends Block {
    private static final Component CONTAINER_NAME = new TranslatableComponent("container.journey_mode.unobtainium_antikythera");

    public UnobtainiumAntikytheraBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            NetworkHooks.openGui((ServerPlayer) player, new UnobtainiumAntikytheraContainerProvider());
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) ->
                new UnobtainiumAntikytheraContainer(id, inventory, ContainerLevelAccess.create(worldIn, pos)), CONTAINER_NAME);
    }
}
