package com.Deeakron.journey_mode.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class AntikytheraShapelessRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AntikytheraShapelessRecipe> {

    private static final ResourceLocation NAME = new ResourceLocation("journey_mode", "crafting_antikythera_shapeless");
    public AntikytheraShapelessRecipe read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (nonnulllist.size() > AntikytheraRecipe.MAX_WIDTH * AntikytheraRecipe.MAX_HEIGHT) {
            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (AntikytheraRecipe.MAX_WIDTH * AntikytheraRecipe.MAX_HEIGHT));
        } else {
            ItemStack itemstack = AntikytheraRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new AntikytheraShapelessRecipe(recipeId, s, itemstack, nonnulllist);
        }
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int i = 0; i < ingredientArray.size(); ++i) {
            Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
            if (!ingredient.hasNoMatchingItems()) {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }

    public AntikytheraShapelessRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        String s = buffer.readString(32767);
        int i = buffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

        for(int j = 0; j < nonnulllist.size(); ++j) {
            nonnulllist.set(j, Ingredient.read(buffer));
        }

        ItemStack itemstack = buffer.readItemStack();
        return new AntikytheraShapelessRecipe(recipeId, s, itemstack, nonnulllist);
    }

    public void write(PacketBuffer buffer, AntikytheraShapelessRecipe recipe) {
        buffer.writeString(recipe.group);
        buffer.writeVarInt(recipe.recipeItems.size());

        for(Ingredient ingredient : recipe.recipeItems) {
            ingredient.write(buffer);
        }

        buffer.writeItemStack(recipe.recipeOutput);
    }

    public void init(){

    }
}
