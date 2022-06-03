package com.Deeakron.journey_mode.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;


public class AntikytheraShapelessRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AntikytheraShapelessRecipe> {

    private static final ResourceLocation NAME = new ResourceLocation("journey_mode", "crafting_antikythera_shapeless");
    public AntikytheraShapelessRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String s = GsonHelper.getAsString(json, "group", "");
        NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (nonnulllist.size() > AntikytheraRecipe.MAX_WIDTH * AntikytheraRecipe.MAX_HEIGHT) {
            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (AntikytheraRecipe.MAX_WIDTH * AntikytheraRecipe.MAX_HEIGHT));
        } else {
            ItemStack itemstack = AntikytheraRecipe.deserializeItem(JSONUtils.getAsJsonObject(json, "result"));
            return new AntikytheraShapelessRecipe(recipeId, s, itemstack, nonnulllist);
        }
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int i = 0; i < ingredientArray.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
            if (!ingredient.isEmpty()) {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }

    public AntikytheraShapelessRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String s = buffer.readUtf(32767);
        int i = buffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

        for(int j = 0; j < nonnulllist.size(); ++j) {
            nonnulllist.set(j, Ingredient.fromNetwork(buffer));
        }

        ItemStack itemstack = buffer.readItem();
        return new AntikytheraShapelessRecipe(recipeId, s, itemstack, nonnulllist);
    }

    public void toNetwork(FriendlyByteBuf buffer, AntikytheraShapelessRecipe recipe) {
        buffer.writeUtf(recipe.group);
        buffer.writeVarInt(recipe.recipeItems.size());

        for(Ingredient ingredient : recipe.recipeItems) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.recipeOutput);
    }

    public void init(){

    }
}
