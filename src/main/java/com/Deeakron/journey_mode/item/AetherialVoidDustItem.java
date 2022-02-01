package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherialVoidDustItem extends Item {
    public AetherialVoidDustItem(Properties properties) {
        super(properties);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.COMMAND_BLOCK)) {
            world.setBlockState(pos, UnobtainBlockInit.INERT_COMMAND_BLOCK.get().getDefaultState());
            return ActionResultType.func_233537_a_(world.isRemote);
        } else if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.CHAIN_COMMAND_BLOCK)) {
            world.setBlockState(pos, UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get().getDefaultState());
            return ActionResultType.func_233537_a_(world.isRemote);
        } else if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.REPEATING_COMMAND_BLOCK)) {
            world.setBlockState(pos, UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get().getDefaultState());
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return ActionResultType.PASS;
    }
}