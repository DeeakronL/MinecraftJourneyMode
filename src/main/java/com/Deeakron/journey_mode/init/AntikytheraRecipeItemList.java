package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class AntikytheraRecipeItemList {
    private static ResourceLocation[] locations = {new ResourceLocation("journey_mode:recipes/antikythera_crafting_barrier"), new ResourceLocation("journey_mode:recipes/antikythera_crafting_chain_command_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_command_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_command_block_minecart"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_debug_stick"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_dirt_path"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_end_portal_frame"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_farmland"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_cobblestone"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_chiseled_stone_bricks"), new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_mossy_stone_bricks"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_stone"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_stone_bricks"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_cracked_stone_bricks"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_jigsaw"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_knowledge_book"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_paint_bucket"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_petrified_oak_slab"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_player_head"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_repeating_command_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_scanner"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_spawner"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_structure_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_structure_void"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_wandering_trader_spawn_egg"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_zombie_horse_spawn_egg")};
    private static ResourceLocation[] recipeLocations = {new ResourceLocation("journey_mode:antikythera_crafting_barrier"), new ResourceLocation("journey_mode:antikythera_crafting_chain_command_block"),new ResourceLocation("journey_mode:antikythera_crafting_command_block"),new ResourceLocation("journey_mode:antikythera_crafting_command_block_minecart"),new ResourceLocation("journey_mode:antikythera_crafting_debug_stick"),new ResourceLocation("journey_mode:antikythera_crafting_dirt_path"),new ResourceLocation("journey_mode:antikythera_crafting_end_portal_frame"),new ResourceLocation("journey_mode:antikythera_crafting_farmland"),new ResourceLocation("journey_mode:antikythera_crafting_infested_cobblestone"),new ResourceLocation("journey_mode:antikythera_crafting_infested_chiseled_stone_bricks"), new ResourceLocation("journey_mode:antikythera_crafting_infested_mossy_stone_bricks"),new ResourceLocation("journey_mode:antikythera_crafting_infested_stone"),new ResourceLocation("journey_mode:antikythera_crafting_infested_stone_bricks"),new ResourceLocation("journey_mode:antikythera_crafting_infested_cracked_stone_bricks"),new ResourceLocation("journey_mode:antikythera_crafting_jigsaw"),new ResourceLocation("journey_mode:antikythera_crafting_knowledge_book"),new ResourceLocation("journey_mode:antikythera_crafting_paint_bucket"),new ResourceLocation("journey_mode:antikythera_crafting_petrified_oak_slab"),new ResourceLocation("journey_mode:antikythera_crafting_player_head"),new ResourceLocation("journey_mode:antikythera_crafting_repeating_command_block"),new ResourceLocation("journey_mode:antikythera_crafting_scanner"),new ResourceLocation("journey_mode:antikythera_crafting_spawner"),new ResourceLocation("journey_mode:antikythera_crafting_structure_block"),new ResourceLocation("journey_mode:antikythera_crafting_structure_void"),new ResourceLocation("journey_mode:antikythera_crafting_wandering_trader_spawn_egg"),new ResourceLocation("journey_mode:antikythera_crafting_zombie_horse_spawn_egg")};
    private static ResourceLocation[][] items;
    private static boolean[] isShaped = {true, true, true, false, false, false, true, false, false, false, false, false, false, false, true, true, false, true, false, true, true, true, true, true, false, true};

    public AntikytheraRecipeItemList(MinecraftServer server) {
        RecipeManager manager = server.getRecipeManager();
        //journey_mode.LOGGER.info("hey things seem to be working, like " + manager.getRecipe(recipeLocations[0]).toString());
        ResourceLocation tempItems [][] = new ResourceLocation[recipeLocations.length][];
        for (int i = 0; i < recipeLocations.length; i++) {
            ResourceLocation tempItems2 [] = new ResourceLocation[10];
            for (int j =0; j < 9; j++) {
                //code to get items to put into items[i], with items[i][0-8] being the input and items[i][9] being the output
                journey_mode.LOGGER.info(recipeLocations[i]);
                //journey_mode.LOGGER.info(manager.getRecipe(recipeLocations[i]).get().getIngredients().get(j));
                try {
                    if (manager.getRecipe(recipeLocations[i]).get().getIngredients().get(j) != null) {
                        tempItems2[j] = manager.getRecipe(recipeLocations[i]).get().getIngredients().get(j).getMatchingStacks()[0].getItem().getRegistryName();
                    } else {
                        tempItems2[j] = new ResourceLocation("");
                    }
                } catch (IndexOutOfBoundsException e) {
                    tempItems2[j] = new ResourceLocation("");
                }

                //
            }
            tempItems2[9] = manager.getRecipe(recipeLocations[i]).get().getRecipeOutput().getItem().getRegistryName();
            tempItems[i] = tempItems2;

        }
        items = tempItems;
    }

    public ResourceLocation[] getLocations() {
        return this.locations;
    }

    public ResourceLocation[] getSpecificRecipe(int index) {
        return this.items[index];
    }

    public boolean getIsShaped(int index) {
        return this.isShaped[index];
    }
}
