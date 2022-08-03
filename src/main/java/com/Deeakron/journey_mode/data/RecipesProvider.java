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
        registerCrafting(consumer);
    }

    private void registerCrafting(Consumer<FinishedRecipe> consumer) {
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
    }
}
