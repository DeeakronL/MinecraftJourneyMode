package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.init.UnobtainItemInit;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

public class PaintedBarrierBlock extends Block {
    public PaintedBarrierBlock(AbstractBlock.Properties properties) {
        super(properties);
    }
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }



    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World worldIn, BlockPos pos, Random random) {
        List<? extends PlayerEntity> players = worldIn.getPlayers();
        for (PlayerEntity player :players) {
            if (player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof BlockItem) {
                if (((BlockItem) player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem()).getBlock() == Blocks.BARRIER){
                    int i = pos.getX();
                    int j = pos.getY();
                    int k = pos.getZ();
                    worldIn.addParticle(ParticleTypes.BARRIER, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 0.0D, 0.0D, 0.0D);
                }
            } else if (player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == UnobtainItemInit.PAINT_BUCKET.get()){
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.addParticle(ParticleTypes.BARRIER, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 0.0D, 0.0D, 0.0D);
            }
        }
        super.animateTick(state, worldIn, pos, random);
    }
}
