package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.container.UnobtainiumAntikytheraContainer;
import com.Deeakron.journey_mode.container.UnobtainiumAntikytheraContainerProvider;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

public class UnobtainiumAntikytheraBlock extends Block {
    private static final Component CONTAINER_NAME = new TranslatableComponent("container.journey_mode.unobtainium_antikythera");

    public UnobtainiumAntikytheraBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            NetworkHooks.openGui((ServerPlayerEntity) player, new UnobtainiumAntikytheraContainerProvider());
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inventory, player) ->
                new UnobtainiumAntikytheraContainer(id, inventory, IWorldPosCallable.create(worldIn, pos)), CONTAINER_NAME);
    }
}
