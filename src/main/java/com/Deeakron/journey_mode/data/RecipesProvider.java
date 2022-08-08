package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.BlockInit;
import com.Deeakron.journey_mode.init.ItemInit;
import com.Deeakron.journey_mode.init.UnobtainItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class RecipesProvider extends RecipeProvider {

    public RecipesProvider(DataGenerator generator) {
        super(generator);
    }

    public void registerRecipes(Consumer<FinishedRecipe> consumer) {
        buildCraftingRecipes(consumer);
    }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ItemInit.DIAMOND_RESEARCH_GRINDER.get())
                .define('#', Items.DIAMOND_SWORD)
                .define('O', Items.DIAMOND_BLOCK)
                .define('_', Items.OBSIDIAN)
                .pattern("# #")
                .pattern("O#O")
                .pattern("___")
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemInit.IRON_RESEARCH_GRINDER.get())
                .define('#', Items.IRON_SWORD)
                .define('O', Items.IRON_BLOCK)
                .define('_', Items.ANVIL)
                .pattern("# #")
                .pattern("O#O")
                .pattern("___")
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemInit.WOODEN_RESEARCH_GRINDER.get())
                .define('#', Items.WOODEN_SWORD)
                .define('O', ItemTags.LOGS)
                .define('_', ItemTags.WOODEN_SLABS)
                .pattern("# #")
                .pattern("O#O")
                .pattern("___")
                .save(consumer);
        ShapedRecipeBuilder.shaped(UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('#', Items.SPONGE)
                .define('E', Items.END_CRYSTAL)
                .define('C', Items.CONDUIT)
                .define('B', Items.BEACON)
                .define('P', Items.POISONOUS_POTATO)
                .define('R', Items.RESPAWN_ANCHOR)
                .define('N', Items.NETHERITE_BLOCK)
                .define('D', Items.DRAGON_HEAD)
                .define('O', Items.EMERALD_ORE)
                .pattern("#EC")
                .pattern("BPR")
                .pattern("NDO")
                .save(consumer);
        ShapedRecipeBuilder.shaped(UnobtainItemInit.UNOBTAINIUM_INGOT.get())
                .define('#', Items.BEDROCK)
                .define('J', UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('N', UnobtainItemInit.PRIMORDIAL_VOID_DUST.get())
                .pattern("#J#")
                .pattern("JNJ")
                .pattern("#J#")
                .save(consumer);
        ShapedRecipeBuilder.shaped(UnobtainItemInit.UNOBTAINIUM_STARFORGE.get())
                .define('#', UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('B', Items.BLACK_STAINED_GLASS)
                .define('N', Items.NETHER_STAR)
                .pattern("#B#")
                .pattern("BNB")
                .pattern("#B#")
                .save(consumer);
        ShapedRecipeBuilder.shaped(UnobtainItemInit.UNOBTAINIUM_ANTIKYTHERA.get())
                .define('#', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('B', Items.BEACON)
                .define('E', Items.END_CRYSTAL)
                .define('N', Items.NETHER_STAR)
                .define('D', Items.DRAGON_EGG)
                .pattern("#B#")
                .pattern("ENE")
                .pattern("#D#")
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(UnobtainItemInit.RAW_UNOBTAINIUM.get(), 9)
                .requires(UnobtainItemInit.RAW_UNOBTAINIUM_BLOCK.get())
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(UnobtainItemInit.RAW_UNOBTAINIUM_BLOCK.get())
                .requires(UnobtainItemInit.RAW_UNOBTAINIUM.get(), 9)
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.BARRIER)
                .define('#', Items.BEDROCK)
                .define('G', Items.GLASS)
                .pattern("###")
                .pattern("#G#")
                .pattern("###")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.CHAIN_COMMAND_BLOCK)
                .define('#', Items.CHAIN)
                .define('C', Items.COMMAND_BLOCK)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.COMMAND_BLOCK)
                .define('#', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('O', Items.OBSERVER)
                .define('N', Items.NETHERITE_BLOCK)
                .define('L', Items.LODESTONE)
                .pattern("#O#")
                .pattern("NLN")
                .pattern("#N#")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.END_PORTAL_FRAME)
                .define('#', Items.END_STONE)
                .define('J', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('E', Items.ENDER_PEARL)
                .pattern("#J#")
                .pattern("JEJ")
                .pattern("#J#")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.JIGSAW)
                .define('#', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('_', Items.PETRIFIED_OAK_SLAB)
                .define('S', Items.SPECTRAL_ARROW)
                .define('C', Items.COMMAND_BLOCK)
                .pattern("#_#")
                .pattern("SCS")
                .pattern("#S#")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.KNOWLEDGE_BOOK)
                .define('#', Items.STONECUTTER)
                .define('C', Items.CRAFTING_TABLE)
                .define('S', Items.SMITHING_TABLE)
                .define('F', Items.FURNACE)
                .define('W', Items.WRITABLE_BOOK)
                .define('B', Items.BLAST_FURNACE)
                .define('O', Items.BREWING_STAND)
                .define('R', Items.SMOKER)
                .define('E', Items.ENCHANTING_TABLE)
                .pattern("#CS")
                .pattern("FWB")
                .pattern("ORE")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.PETRIFIED_OAK_SLAB)
                .define('#', Items.OAK_LOG)
                .define('B', Items.BEDROCK)
                .pattern("###")
                .pattern("#B#")
                .pattern("###")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.REPEATING_COMMAND_BLOCK)
                .define('#', Items.REPEATER)
                .define('C', Items.COMMAND_BLOCK)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(UnobtainItemInit.SCANNER.get())
                .define('#', UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('D', Items.DRAGON_HEAD)
                .define('N', Items.NETHERITE_HELMET)
                .define('Y', Items.YELLOW_STAINED_GLASS_PANE)
                .pattern("#D#")
                .pattern("#N#")
                .pattern("Y Y")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.SPAWNER)
                .define('#', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('J', Items.CHAIN)
                .define('E', Items.PIG_SPAWN_EGG)
                .pattern("#J#")
                .pattern("JEJ")
                .pattern("#J#")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.STRUCTURE_BLOCK)
                .define('#', Items.JIGSAW)
                .define('J', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('C', Items.COMMAND_BLOCK)
                .pattern("#J#")
                .pattern("JCJ")
                .pattern("#J#")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.STRUCTURE_VOID)
                .define('#', Items.STRUCTURE_BLOCK)
                .define('B', Items.BARRIER)
                .pattern("###")
                .pattern("#B#")
                .pattern("###")
                .save(consumer);
        AntikytheraRecipeBuilder.shaped(Items.ZOMBIE_HORSE_SPAWN_EGG)
                .define('#', Items.ROTTEN_FLESH)
                .define('E', Items.HORSE_SPAWN_EGG)
                .pattern("###")
                .pattern("#E#")
                .pattern("###")
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.COMMAND_BLOCK_MINECART)
                .requires(Items.COMMAND_BLOCK)
                .requires(Items.MINECART)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.DEBUG_STICK)
                .requires(Items.COMMAND_BLOCK)
                .requires(Items.CHAIN_COMMAND_BLOCK)
                .requires(Items.REPEATING_COMMAND_BLOCK)
                .requires(Items.STICK)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.DIRT_PATH)
                .requires(Items.DIRT)
                .requires(Items.NETHERITE_SHOVEL)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.FARMLAND)
                .requires(Items.DIRT)
                .requires(Items.NETHERITE_HOE)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_CHISELED_STONE_BRICKS)
                .requires(Items.CHISELED_STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_CRACKED_STONE_BRICKS)
                .requires(Items.CRACKED_STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_DEEPSLATE)
                .requires(Items.DEEPSLATE)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_STONE)
                .requires(Items.STONE)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_MOSSY_STONE_BRICKS)
                .requires(Items.MOSSY_STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_STONE_BRICKS)
                .requires(Items.STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(UnobtainItemInit.PAINT_BUCKET.get())
                .requires(Items.WATER_BUCKET)
                .requires(Items.RED_DYE)
                .requires(Items.RED_MUSHROOM_BLOCK)
                .requires(Items.RED_GLAZED_TERRACOTTA)
                .requires(Items.REDSTONE)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.PLAYER_HEAD)
                .requires(Items.ZOMBIE_HEAD)
                .requires(Items.GOLDEN_APPLE)
                .save(consumer);
        AntikytheraShapelessRecipeBuilder.shapeless(Items.WANDERING_TRADER_SPAWN_EGG)
                .requires(Items.VILLAGER_SPAWN_EGG)
                .requires(Items.TRADER_LLAMA_SPAWN_EGG)
                .save(consumer);
    }
}
