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

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block>WOODEN_RESEARCH_GRINDER = BLOCKS
            .register("wooden_research_grinder",
                    () -> new ResearchGrinderBlock(AbstractBlock.Properties.create(Material.WOOD)
                    .hardnessAndResistance(2f, 2f)
                    .harvestTool(ToolType.AXE).harvestLevel(0)
                    .sound(SoundType.WOOD)
                    .notSolid(), "wood"));
    public static final RegistryObject<Block>WOODEN_RESEARCH_GRINDER_PART_0 = BLOCKS
            .register("wooden_research_grinder_part_0",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.WOOD)
                            .hardnessAndResistance(2f, 2f)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .sound(SoundType.WOOD)
                            .notSolid(), 0, "wood"));
    public static final RegistryObject<Block>WOODEN_RESEARCH_GRINDER_PART_1 = BLOCKS
            .register("wooden_research_grinder_part_1",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.WOOD)
                            .hardnessAndResistance(2f, 2f)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .sound(SoundType.WOOD)
                            .notSolid(),1, "wood"));
    public static final RegistryObject<Block>WOODEN_RESEARCH_GRINDER_PART_2 = BLOCKS
            .register("wooden_research_grinder_part_2",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.WOOD)
                            .hardnessAndResistance(2f, 2f)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .sound(SoundType.WOOD)
                            .notSolid(),2, "wood"));
}
