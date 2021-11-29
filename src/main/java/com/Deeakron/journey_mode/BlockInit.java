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

    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER = BLOCKS
            .register("iron_research_grinder",
                    () -> new ResearchGrinderBlock(AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .notSolid(), "iron"));
    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER_PART_0 = BLOCKS
            .register("iron_research_grinder_part_0",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .notSolid(),0, "iron"));
    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER_PART_1 = BLOCKS
            .register("iron_research_grinder_part_1",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .notSolid(),1, "iron"));
    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER_PART_2 = BLOCKS
            .register("iron_research_grinder_part_2",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .notSolid(),2, "iron"));

    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER = BLOCKS
            .register("diamond_research_grinder",
                    () -> new ResearchGrinderBlock(AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .notSolid(), "diamond"));
    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER_PART_0 = BLOCKS
            .register("diamond_research_grinder_part_0",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .notSolid(),0, "diamond"));
    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER_PART_1 = BLOCKS
            .register("diamond_research_grinder_part_1",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .notSolid(),1, "diamond"));
    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER_PART_2 = BLOCKS
            .register("diamond_research_grinder_part_2",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .notSolid(),2, "diamond"));
}
