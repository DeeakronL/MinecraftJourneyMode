package com.Deeakron.journey_mode.block;

import com.Deeakron.journey_mode.init.UnobtainItemInit;
import net.minecraft.block.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PaintedBarrierBlock extends Block {
    public PaintedBarrierBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }



    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, Random random) {
        List<? extends Player> players = worldIn.players();
        for (PlayerEntity player :players) {
            if (player.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() instanceof BlockItem) {
                if (((BlockItem) player.getItemBySlot(EquipmentSlotType.MAINHAND).getItem()).getBlock() == Blocks.BARRIER){
                    int i = pos.getX();
                    int j = pos.getY();
                    int k = pos.getZ();
                    worldIn.addParticle(ParticleTypes.BARRIER, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 0.0D, 0.0D, 0.0D);
                }
            } else if (player.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() == UnobtainItemInit.PAINT_BUCKET.get()){
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.addParticle(ParticleTypes.BARRIER, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 0.0D, 0.0D, 0.0D);
            }
        }
        super.animateTick(state, worldIn, pos, random);
    }
}
