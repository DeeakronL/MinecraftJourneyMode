package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.block.ResearchGrinderBlock;
import com.Deeakron.journey_mode.block.ResearchGrinderPartBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
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
                    () -> new ResearchGrinderBlock(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(2f, 2f)
                    .harvestTool(ToolType.AXE).harvestLevel(0)
                    .sound(SoundType.WOOD)
                    .noOcclusion(), "wood"));
    public static final RegistryObject<Block>WOODEN_RESEARCH_GRINDER_PART_0 = BLOCKS
            .register("wooden_research_grinder_part_0",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.WOOD)
                            .strength(2f, 2f)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .sound(SoundType.WOOD)
                            .noOcclusion(), 0, "wood"));
    public static final RegistryObject<Block>WOODEN_RESEARCH_GRINDER_PART_1 = BLOCKS
            .register("wooden_research_grinder_part_1",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.WOOD)
                            .strength(2f, 2f)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .sound(SoundType.WOOD)
                            .noOcclusion(),1, "wood"));
    public static final RegistryObject<Block>WOODEN_RESEARCH_GRINDER_PART_2 = BLOCKS
            .register("wooden_research_grinder_part_2",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.WOOD)
                            .strength(2f, 2f)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .sound(SoundType.WOOD)
                            .noOcclusion(),2, "wood"));

    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER = BLOCKS
            .register("iron_research_grinder",
                    () -> new ResearchGrinderBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .noOcclusion(), "iron"));
    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER_PART_0 = BLOCKS
            .register("iron_research_grinder_part_0",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .noOcclusion(),0, "iron"));
    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER_PART_1 = BLOCKS
            .register("iron_research_grinder_part_1",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .noOcclusion(),1, "iron"));
    public static final RegistryObject<Block>IRON_RESEARCH_GRINDER_PART_2 = BLOCKS
            .register("iron_research_grinder_part_2",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(5f, 6f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(1)
                            .sound(SoundType.METAL)
                            .noOcclusion(),2, "iron"));

    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER = BLOCKS
            .register("diamond_research_grinder",
                    () -> new ResearchGrinderBlock(AbstractBlock.Properties.of(Material.STONE)
                            .strength(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .noOcclusion(), "diamond"));
    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER_PART_0 = BLOCKS
            .register("diamond_research_grinder_part_0",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.STONE)
                            .strength(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .noOcclusion(),0, "diamond"));
    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER_PART_1 = BLOCKS
            .register("diamond_research_grinder_part_1",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.STONE)
                            .strength(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .noOcclusion(),1, "diamond"));
    public static final RegistryObject<Block>DIAMOND_RESEARCH_GRINDER_PART_2 = BLOCKS
            .register("diamond_research_grinder_part_2",
                    () -> new ResearchGrinderPartBlock(AbstractBlock.Properties.of(Material.STONE)
                            .strength(5f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                            .sound(SoundType.STONE)
                            .noOcclusion(),2, "diamond"));
}
