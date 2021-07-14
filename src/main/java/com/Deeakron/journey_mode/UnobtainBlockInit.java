package com.Deeakron.journey_mode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.Deeakron.journey_mode.journey_mode.MODID;

public class UnobtainBlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            MODID);

    public static final RegistryObject<Block> UNOBTAINIUM_BLOCK = BLOCKS
            .register("unobtainium_block",
                    () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.NETHERITE)
                            .setRequiresTool()));

    public static final RegistryObject<Block> UNOBTAINIUM_RAW_BLOCK = BLOCKS
            .register("unobtainium_raw_block",
                    () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.ANCIENT_DEBRIS)
                            .setRequiresTool()));
}
