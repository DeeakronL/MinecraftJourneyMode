package com.Deeakron.journey_mode.data;

import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class StarforgeRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<StarforgeRecipe> {

    @Override
    public StarforgeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        return new StarforgeRecipe(recipeId, input, output);
    }

    @Override
    public StarforgeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        ItemStack output = buffer.readItem();
        Ingredient input = Ingredient.fromNetwork(buffer);

        return new StarforgeRecipe(recipeId, input, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, StarforgeRecipe recipe) {
        Ingredient input = recipe.getIngredients().get(0);
        input.toNetwork(buffer);

        buffer.writeItemStack(recipe.getResultItem(), false);
    }
}
