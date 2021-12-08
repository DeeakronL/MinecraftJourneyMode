package com.Deeakron.journey_mode;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class StarforgeRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<StarforgeRecipe> {

    @Override
    public StarforgeRecipe read(ResourceLocation recipeId, JsonObject json) {
        ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
        Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
        Ingredient fuel = Ingredient.deserialize(JSONUtils.getJsonObject(json, "fuel"));

        return new StarforgeRecipe(recipeId, fuel, input, output);
    }

    @Override
    public StarforgeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        ItemStack output = buffer.readItemStack();
        Ingredient input = Ingredient.read(buffer);
        Ingredient fuel = Ingredient.read(buffer);

        return new StarforgeRecipe(recipeId, fuel, input, output);
    }

    @Override
    public void write(PacketBuffer buffer, StarforgeRecipe recipe) {
        Ingredient input = recipe.getIngredients().get(0);
        input.write(buffer);
        Ingredient fuel = recipe.getIngredients().get(1);
        fuel.write(buffer);

        buffer.writeItemStack(recipe.getRecipeOutput(), false);
    }
}
