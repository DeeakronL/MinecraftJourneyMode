package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.container.UnobtainiumAntikytheraContainer;
import com.Deeakron.journey_mode.container.UnobtainiumAntikytheraContainerProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class UnobtainiumAntikytheraBlock extends Block {
    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("container.journey_mode.unobtainium_antikythera");

    public UnobtainiumAntikytheraBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            player.openContainer(state.getContainer(worldIn, pos));
            NetworkHooks.openGui((ServerPlayerEntity) player, new UnobtainiumAntikytheraContainerProvider());
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inventory, player) ->
                new UnobtainiumAntikytheraContainer(id, inventory, IWorldPosCallable.of(worldIn, pos)), CONTAINER_NAME);
    }
}
