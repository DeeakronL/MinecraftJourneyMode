package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.block.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.word.level.block.SoundType;;
import net.minecraft.world.level.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

import static com.Deeakron.journey_mode.journey_mode.MODID;

public class UnobtainBlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            MODID);

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
        };
    }

    public static final RegistryObject<Block> UNOBTAINIUM_BLOCK = BLOCKS
            .register("unobtainium_block",
                    () -> new Block(AbstractBlock.Properties.of(Material.METAL)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.NETHERITE_BLOCK)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> RAW_UNOBTAINIUM_BLOCK = BLOCKS
            .register("raw_unobtainium_block",
                    () -> new Block(AbstractBlock.Properties.of(Material.METAL)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.ANCIENT_DEBRIS)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> UNOBTAINIUM_ANTIKYTHERA = BLOCKS
            .register("unobtainium_antikythera",
                    () -> new UnobtainiumAntikytheraBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.LODESTONE)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> UNOBTAINIUM_STARFORGE = BLOCKS
            .register("unobtainium_starforge",
                    () -> new UnobtainiumStarforgeBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.LODESTONE)
                            .requiresCorrectToolForDrops()
                            .lightLevel(getLightValueLit(15))));

    public static final RegistryObject<Block> CRACKED_BEDROCK = BLOCKS
            .register("cracked_bedrock",
                    () -> new Block(AbstractBlock.Properties.of(Material.STONE)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> INERT_COMMAND_BLOCK = BLOCKS
            .register("inert_command_block",
                    () -> new InertCommandBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.METAL)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> INERT_CHAIN_COMMAND_BLOCK = BLOCKS
            .register("inert_chain_command_block",
                    () -> new InertCommandBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.METAL)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> INERT_REPEATING_COMMAND_BLOCK = BLOCKS
            .register("inert_repeating_command_block",
                    () -> new InertCommandBlock(AbstractBlock.Properties.of(Material.METAL)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.METAL)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> INERT_STRUCTURE_BLOCK = BLOCKS
            .register("inert_structure_block",
                    () -> new InertStructureBlock(AbstractBlock.Properties.of(Material.STONE)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> INERT_JIGSAW_BLOCK = BLOCKS
            .register("inert_jigsaw_block",
                    () -> new InertJigsawBlock(AbstractBlock.Properties.of(Material.STONE)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> PAINTED_BARRIER = BLOCKS
            .register("painted_barrier",
                    () -> new PaintedBarrierBlock(AbstractBlock.Properties.of(Material.BARRIER)
                            .strength(50f, 1200f)
                            .harvestTool(ToolType.PICKAXE).harvestLevel(4)
                            .sound(SoundType.LODESTONE)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()));
}
