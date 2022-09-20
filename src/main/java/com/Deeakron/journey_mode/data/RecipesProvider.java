package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.ItemInit;
import com.Deeakron.journey_mode.init.UnobtainItemInit;
import com.Deeakron.journey_mode.journey_mode;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class RecipesProvider extends RecipeProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    public RecipesProvider(DataGenerator generator) {
        super(generator);
    }

    public void run(HashCache p_125982_) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        buildCraftingRecipes((p_125991_) -> {
            if (!set.add(p_125991_.getId())) {
                throw new IllegalStateException("Duplicate recipe " + p_125991_.getId());
            } else {
                saveRecipe(p_125982_, p_125991_.serializeRecipe(), path.resolve("data/" + p_125991_.getId().getNamespace() + "/recipes/" + p_125991_.getId().getPath() + ".json"));
                JsonObject jsonobject = p_125991_.serializeAdvancement();
                if (jsonobject != null) {
                    saveAdvancement(p_125982_, jsonobject, path.resolve("data/" + p_125991_.getId().getNamespace() + "/advancements/" + p_125991_.getAdvancementId().getPath() + ".json"));
                }

            }
        });
        if (journey_mode.useUnobtain) {
            buildUnobtainableCraftingRecipes((p_125991_) -> {
                if (!set.add(p_125991_.getId())) {
                    throw new IllegalStateException("Duplicate recipe " + p_125991_.getId());
                } else {
                    saveRecipe(p_125982_, p_125991_.serializeRecipe(), path.resolve("data/" + p_125991_.getId().getNamespace() + "/recipes/" + p_125991_.getId().getPath() + ".json"));
                    JsonObject jsonobject = p_125991_.serializeAdvancement();
                    if (jsonobject != null) {
                        saveAdvancement(p_125982_, jsonobject, path.resolve("data/" + p_125991_.getId().getNamespace() + "/advancements/" + p_125991_.getAdvancementId().getPath() + ".json"));
                    }

                }
            });
        }
    }

    private static void saveRecipe(HashCache p_125984_, JsonObject p_125985_, Path p_125986_) {
        try {
            String s = GSON.toJson((JsonElement)p_125985_);
            String s1 = SHA1.hashUnencodedChars(s).toString();
            if (!Objects.equals(p_125984_.getHash(p_125986_), s1) || !Files.exists(p_125986_)) {
                Files.createDirectories(p_125986_.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(p_125986_);

                try {
                    bufferedwriter.write(s);
                } catch (Throwable throwable1) {
                    if (bufferedwriter != null) {
                        try {
                            bufferedwriter.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }
                    }

                    throw throwable1;
                }

                if (bufferedwriter != null) {
                    bufferedwriter.close();
                }
            }

            p_125984_.putNew(p_125986_, s1);
        } catch (IOException ioexception) {

        }

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
                .unlockedBy("has_diamond",has(Items.DIAMOND_SWORD))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemInit.IRON_RESEARCH_GRINDER.get())
                .define('#', Items.IRON_SWORD)
                .define('O', Items.IRON_BLOCK)
                .define('_', Items.ANVIL)
                .pattern("# #")
                .pattern("O#O")
                .pattern("___")
                .unlockedBy("has_iron",has(Items.IRON_SWORD))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemInit.WOODEN_RESEARCH_GRINDER.get())
                .define('#', Items.WOODEN_SWORD)
                .define('O', ItemTags.LOGS)
                .define('_', ItemTags.WOODEN_SLABS)
                .pattern("# #")
                .pattern("O#O")
                .pattern("___")
                .unlockedBy("has_wood",has(Items.WOODEN_SWORD))
                .save(consumer);
    }

    protected void buildUnobtainableCraftingRecipes(Consumer<FinishedRecipe> consumer) {
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
                .unlockedBy("has_dragon_head",has(Items.WOODEN_SWORD))
                .save(consumer);
        ShapedRecipeBuilder.shaped(UnobtainItemInit.UNOBTAINIUM_INGOT.get())
                .define('#', Items.BEDROCK)
                .define('J', UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('N', UnobtainItemInit.PRIMORDIAL_VOID_DUST.get())
                .pattern("#J#")
                .pattern("JNJ")
                .pattern("#J#")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.RAW_UNOBTAINIUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(UnobtainItemInit.UNOBTAINIUM_STARFORGE.get())
                .define('#', UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('B', Items.BLACK_STAINED_GLASS)
                .define('N', Items.NETHER_STAR)
                .pattern("#B#")
                .pattern("BNB")
                .pattern("#B#")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.RAW_UNOBTAINIUM.get()))
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
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(UnobtainItemInit.RAW_UNOBTAINIUM.get(), 9)
                .requires(UnobtainItemInit.RAW_UNOBTAINIUM_BLOCK.get())
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.RAW_UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:shapeless_raw_unobtainium");
        ShapelessRecipeBuilder.shapeless(UnobtainItemInit.RAW_UNOBTAINIUM_BLOCK.get())
                .requires(UnobtainItemInit.RAW_UNOBTAINIUM.get(), 9)
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.RAW_UNOBTAINIUM.get()))
                .save(consumer, "journey_mode:shapeless_raw_unobtainium_block");
        ShapelessRecipeBuilder.shapeless(UnobtainItemInit.UNOBTAINIUM_INGOT.get(), 9)
                .requires(UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:shapeless_unobtainium_ingot");
        ShapelessRecipeBuilder.shapeless(UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .requires(UnobtainItemInit.UNOBTAINIUM_INGOT.get(), 9)
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_INGOT.get()))
                .save(consumer, "journey_mode:shapeless_unobtainium_block");
        AntikytheraRecipeBuilder.shaped(Items.BARRIER)
                .define('#', Items.BEDROCK)
                .define('G', Items.GLASS)
                .pattern("###")
                .pattern("#G#")
                .pattern("###")
                .unlockedBy("has_bedrock",has(Items.BEDROCK))
                .save(consumer, "journey_mode:antikythera_crafting_barrier");
        AntikytheraRecipeBuilder.shaped(Items.CHAIN_COMMAND_BLOCK)
                .define('#', Items.CHAIN)
                .define('C', Items.COMMAND_BLOCK)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .unlockedBy("has_command_block",has(Items.COMMAND_BLOCK))
                .save(consumer, "journey_mode:antikythera_crafting_chain_command_block");
        AntikytheraRecipeBuilder.shaped(Items.COMMAND_BLOCK)
                .define('#', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('O', Items.OBSERVER)
                .define('N', Items.NETHERITE_BLOCK)
                .define('L', Items.LODESTONE)
                .pattern("#O#")
                .pattern("NLN")
                .pattern("#N#")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:antikythera_crafting_command_block");
        AntikytheraRecipeBuilder.shaped(Items.END_PORTAL_FRAME)
                .define('#', Items.END_STONE)
                .define('J', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('E', Items.ENDER_PEARL)
                .pattern("#J#")
                .pattern("JEJ")
                .pattern("#J#")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:antikythera_crafting_end_portal_frame");
        AntikytheraRecipeBuilder.shaped(Items.JIGSAW)
                .define('#', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('_', Items.PETRIFIED_OAK_SLAB)
                .define('S', Items.SPECTRAL_ARROW)
                .define('C', Items.COMMAND_BLOCK)
                .pattern("#_#")
                .pattern("SCS")
                .pattern("#S#")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:antikythera_crafting_jigsaw");
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
                .unlockedBy("has_enchanting_table",has(Items.ENCHANTING_TABLE))
                .save(consumer, "journey_mode:antikythera_crafting_knowledge_book");
        AntikytheraRecipeBuilder.shaped(Items.PETRIFIED_OAK_SLAB)
                .define('#', Items.OAK_LOG)
                .define('B', Items.BEDROCK)
                .pattern("###")
                .pattern("#B#")
                .pattern("###")
                .unlockedBy("has_bedrock",has(Items.BEDROCK))
                .save(consumer, "journey_mode:antikythera_crafting_petrified_oak_slab");
        AntikytheraRecipeBuilder.shaped(Items.REPEATING_COMMAND_BLOCK)
                .define('#', Items.REPEATER)
                .define('C', Items.COMMAND_BLOCK)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .unlockedBy("has_command_block",has(Items.COMMAND_BLOCK))
                .save(consumer, "journey_mode:antikythera_crafting_repeating_command_block");
        AntikytheraRecipeBuilder.shaped(UnobtainItemInit.SCANNER.get())
                .define('#', UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('D', Items.DRAGON_HEAD)
                .define('N', Items.NETHERITE_HELMET)
                .define('Y', Items.YELLOW_STAINED_GLASS_PANE)
                .pattern("#D#")
                .pattern("#N#")
                .pattern("Y Y")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.RAW_UNOBTAINIUM.get()))
                .save(consumer, "journey_mode:antikythera_crafting_scanner");
        AntikytheraRecipeBuilder.shaped(Items.SPAWNER)
                .define('#', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('J', Items.CHAIN)
                .define('E', Items.PIG_SPAWN_EGG)
                .pattern("#J#")
                .pattern("JEJ")
                .pattern("#J#")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:antikythera_crafting_spawner");
        AntikytheraRecipeBuilder.shaped(Items.STRUCTURE_BLOCK)
                .define('#', Items.JIGSAW)
                .define('J', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('C', Items.COMMAND_BLOCK)
                .pattern("#J#")
                .pattern("JCJ")
                .pattern("#J#")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:antikythera_crafting_structure_block");
        AntikytheraRecipeBuilder.shaped(Items.STRUCTURE_VOID)
                .define('#', Items.STRUCTURE_BLOCK)
                .define('B', Items.BARRIER)
                .pattern("###")
                .pattern("#B#")
                .pattern("###")
                .unlockedBy("has_structure_block",has(Items.STRUCTURE_BLOCK))
                .save(consumer, "journey_mode:antikythera_crafting_structure_void");
        AntikytheraRecipeBuilder.shaped(Items.ZOMBIE_HORSE_SPAWN_EGG)
                .define('#', Items.ROTTEN_FLESH)
                .define('E', Items.HORSE_SPAWN_EGG)
                .pattern("###")
                .pattern("#E#")
                .pattern("###")
                .unlockedBy("has_spawn_egg",has(Items.HORSE_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_zombie_horse_spawn_egg");
        AntikytheraRecipeBuilder.shaped(Items.BUDDING_AMETHYST)
                .define('#', Items.AMETHYST_BLOCK)
                .define('A', Items.AMETHYST_CLUSTER)
                .define('C', Items.COMMAND_BLOCK)
                .pattern("#A#")
                .pattern("ACA")
                .pattern("#A#")
                .unlockedBy("has_command_block",has(Items.COMMAND_BLOCK))
                .save(consumer, "journey_mode:antikythera_crafting_budding_amethyst");
        AntikytheraRecipeBuilder.shaped(Items.SCULK_SENSOR)
                .define('#', Items.TRIPWIRE_HOOK)
                .define('B', Items.BLACK_CONCRETE_POWDER)
                .define('E', Items.EXPERIENCE_BOTTLE)
                .define('R', Items.REDSTONE)
                .define('P', Items.END_PORTAL_FRAME)
                .pattern("# #")
                .pattern("BEB")
                .pattern("RPR")
                .unlockedBy("has_end_portal_frame",has(Items.END_PORTAL_FRAME))
                .save(consumer, "journey_mode:antikythera_crafting_sculk_sensor");
        AntikytheraRecipeBuilder.shaped(Items.LIGHT)
                .define('G', Items.GLOWSTONE)
                .define('B', Items.BARRIER)
                .define('R', Items.REDSTONE_LAMP)
                .define('C', Items.COMMAND_BLOCK)
                .define('S', Items.SEA_LANTERN)
                .define('H', Items.SHROOMLIGHT)
                .pattern("GBR")
                .pattern("BCB")
                .pattern("SBH")
                .unlockedBy("has_command_block",has(Items.COMMAND_BLOCK))
                .save(consumer, "journey_mode:antikythera_crafting_light");
        AntikytheraRecipeBuilder.shaped(UnobtainItemInit.UNOBTAINIUM_HAMMER.get())
                .define('I', UnobtainItemInit.UNOBTAINIUM_INGOT.get())
                .define('B', UnobtainItemInit.UNOBTAINIUM_BLOCK.get())
                .define('R', UnobtainItemInit.RAW_UNOBTAINIUM.get())
                .define('P', UnobtainItemInit.PRIMORDIAL_VOID_DUST.get())
                .define('A', UnobtainItemInit.AETHERIAL_VOID_DUST.get())
                .pattern("IBR")
                .pattern(" P ")
                .pattern(" A ")
                .unlockedBy("has_unobtainium",has(UnobtainItemInit.UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:antikythera_crafting_unobtainium_hammer");
        AntikytheraRecipeBuilder.shaped(Items.BUNDLE)
                .define('S', Items.STRING)
                .define('R', Items.RABBIT_HIDE)
                .pattern("SRS")
                .pattern("R R")
                .pattern("RRR")
                .unlockedBy("has_antikythera",has(UnobtainItemInit.UNOBTAINIUM_ANTIKYTHERA.get()))
                .save(consumer, "journey_mode:antikythera_crafting_bundle");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.COMMAND_BLOCK_MINECART)
                .requires(Items.COMMAND_BLOCK)
                .requires(Items.MINECART)
                .unlockedBy("has_command_block",has(Items.COMMAND_BLOCK))
                .save(consumer, "journey_mode:antikythera_crafting_command_block_minecart");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.DEBUG_STICK)
                .requires(Items.COMMAND_BLOCK)
                .requires(Items.CHAIN_COMMAND_BLOCK)
                .requires(Items.REPEATING_COMMAND_BLOCK)
                .requires(Items.STICK)
                .unlockedBy("has_command_block",has(Items.COMMAND_BLOCK))
                .save(consumer, "journey_mode:antikythera_crafting_debug_stick");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.DIRT_PATH)
                .requires(Items.DIRT)
                .requires(Items.NETHERITE_SHOVEL)
                .unlockedBy("has_shovel",has(Items.NETHERITE_SHOVEL))
                .save(consumer, "journey_mode:antikythera_crafting_dirt_path");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.FARMLAND)
                .requires(Items.DIRT)
                .requires(Items.NETHERITE_HOE)
                .unlockedBy("has_hoe",has(Items.NETHERITE_HOE))
                .save(consumer, "journey_mode:antikythera_crafting_farmland");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_CHISELED_STONE_BRICKS)
                .requires(Items.CHISELED_STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.SILVERFISH_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_infested_chiseled_stone_bricks");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.SILVERFISH_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_infested_cobblestone");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_CRACKED_STONE_BRICKS)
                .requires(Items.CRACKED_STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.SILVERFISH_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_infested_cracked_stone_bricks");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_DEEPSLATE)
                .requires(Items.DEEPSLATE)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.SILVERFISH_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_infested_deepslate");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_STONE)
                .requires(Items.STONE)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.SILVERFISH_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_infested_stone");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_MOSSY_STONE_BRICKS)
                .requires(Items.MOSSY_STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.SILVERFISH_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_infested_mossy_stone_bricks");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.INFESTED_STONE_BRICKS)
                .requires(Items.STONE_BRICKS)
                .requires(Items.SILVERFISH_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.SILVERFISH_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_infested_stone_bricks");
        AntikytheraShapelessRecipeBuilder.shapeless(UnobtainItemInit.PAINT_BUCKET.get())
                .requires(Items.WATER_BUCKET)
                .requires(Items.RED_DYE)
                .requires(Items.RED_MUSHROOM_BLOCK)
                .requires(Items.RED_GLAZED_TERRACOTTA)
                .requires(Items.REDSTONE)
                .unlockedBy("has_red_dye",has(Items.RED_DYE))
                .save(consumer, "journey_mode:antikythera_crafting_paint_bucket");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.PLAYER_HEAD)
                .requires(Items.ZOMBIE_HEAD)
                .requires(Items.GOLDEN_APPLE)
                .unlockedBy("has_zombie_head",has(Items.ZOMBIE_HEAD))
                .save(consumer, "journey_mode:antikythera_crafting_player_head");
        AntikytheraShapelessRecipeBuilder.shapeless(Items.WANDERING_TRADER_SPAWN_EGG)
                .requires(Items.VILLAGER_SPAWN_EGG)
                .requires(Items.TRADER_LLAMA_SPAWN_EGG)
                .unlockedBy("has_spawn_egg",has(Items.VILLAGER_SPAWN_EGG))
                .save(consumer, "journey_mode:antikythera_crafting_wandering_trader_spawn_egg");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.PAINTED_BARRIER.get()), Items.BARRIER, 0.3f, 100)
                .unlockedBy("has_barrier", has(Items.BARRIER))
                .save(consumer, "journey_mode:starforge_smelting_barrier");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.UNOBTAINIUM_ANTIKYTHERA.get()), UnobtainItemInit.AETHERIAL_VOID_DUST.get(), 0.3f, 100)
                .unlockedBy("has_antikythera", has(UnobtainItemInit.UNOBTAINIUM_ANTIKYTHERA.get()))
                .save(consumer, "journey_mode:starforge_smelting_aetherial_void_dust");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.CRACKED_BEDROCK.get()), Items.BEDROCK, 0.3f, 100)
                .unlockedBy("has_cracked_bedrock", has(UnobtainItemInit.CRACKED_BEDROCK.get()))
                .save(consumer, "journey_mode:starforge_smelting_bedrock");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.INERT_CHAIN_COMMAND_BLOCK.get()), Items.CHAIN_COMMAND_BLOCK, 0.3f, 100)
                .unlockedBy("has_inert_command_block", has(UnobtainItemInit.INERT_CHAIN_COMMAND_BLOCK.get()))
                .save(consumer, "journey_mode:starforge_smelting_chain_command_block");
        StarforgeRecipeBuilder.cooking(Ingredient.of(Items.CHORUS_FLOWER), Items.CHORUS_FRUIT, 0.3f, 100)
                .unlockedBy("has_chorus_flower", has(Items.CHORUS_FLOWER))
                .save(consumer, "journey_mode:starforge_smelting_chorus_fruit");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.INERT_COMMAND_BLOCK.get()), Items.COMMAND_BLOCK, 0.3f, 100)
                .unlockedBy("has_inert_command_block", has(UnobtainItemInit.INERT_COMMAND_BLOCK.get()))
                .save(consumer, "journey_mode:starforge_smelting_command_block");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.INERT_JIGSAW_BLOCK.get()), Items.JIGSAW, 0.3f, 100)
                .unlockedBy("has_inert_jigsaw_block", has(UnobtainItemInit.INERT_JIGSAW_BLOCK.get()))
                .save(consumer, "journey_mode:starforge_smelting_jigsaw_block");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.RAW_UNOBTAINIUM_BLOCK.get()), UnobtainItemInit.PRIMORDIAL_VOID_DUST.get(), 0.3f, 100)
                .unlockedBy("has_raw_unobtainium", has(UnobtainItemInit.RAW_UNOBTAINIUM_BLOCK.get()))
                .save(consumer, "journey_mode:starforge_smelting_primordial_void_dust");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.INERT_REPEATING_COMMAND_BLOCK.get()), Items.REPEATING_COMMAND_BLOCK, 0.3f, 100)
                .unlockedBy("has_inert_command_block", has(UnobtainItemInit.INERT_REPEATING_COMMAND_BLOCK.get()))
                .save(consumer, "journey_mode:starforge_smelting_repeating_command_block");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.INERT_STRUCTURE_BLOCK.get()), Items.STRUCTURE_BLOCK, 0.3f, 100)
                .unlockedBy("has_inert_structure_block", has(UnobtainItemInit.INERT_STRUCTURE_BLOCK.get()))
                .save(consumer, "journey_mode:starforge_smelting_structure_block");
        StarforgeRecipeBuilder.cooking(Ingredient.of(Items.TURTLE_EGG), Items.TURTLE_SPAWN_EGG, 0.3f, 100)
                .unlockedBy("has_turtle_egg", has(Items.TURTLE_EGG))
                .save(consumer, "journey_mode:starforge_smelting_turtle_spawn_egg");
        StarforgeRecipeBuilder.cooking(Ingredient.of(UnobtainItemInit.BROKEN_LIGHT.get()), Items.LIGHT, 0.3f, 100)
                .unlockedBy("has_broken_light", has(UnobtainItemInit.BROKEN_LIGHT.get()))
                .save(consumer, "journey_mode:starforge_smelting_light");
    }
}
