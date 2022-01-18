package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PrimordialVoidDustItem extends Item {
    public PrimordialVoidDustItem(Properties properties) {
        super(properties);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if (world.getBlockState(pos).getBlock().getDefaultState().matchesBlock(Blocks.BEDROCK)) {
            world.setBlockState(pos, UnobtainBlockInit.CRACKED_BEDROCK.get().getDefaultState());
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return ActionResultType.PASS;
    }
}
