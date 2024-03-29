package com.Deeakron.journey_mode.data;

import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;

public class AntikytheraRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>>  implements RecipeSerializer<AntikytheraRecipe> {
    private static final ResourceLocation NAME = new ResourceLocation("journey_mode", "crafting_antikythera");
    public static final AntikytheraRecipeSerializer INSTANCE = new AntikytheraRecipeSerializer();
    public AntikytheraRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String s = GsonHelper.getAsString(json, "group", "CRAFTING");
        Map<String, Ingredient> map = AntikytheraRecipe.deserializeKey(GsonHelper.getAsJsonObject(json, "key"));
        String[] astring = AntikytheraRecipe.shrink(AntikytheraRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = AntikytheraRecipe.deserializeIngredients(astring, map, i, j);
        ItemStack itemstack = AntikytheraRecipe.deserializeItem(GsonHelper.getAsJsonObject(json, "result"));
        return new AntikytheraRecipe(recipeId, s, i, j, nonnulllist, itemstack);
    }

    public AntikytheraRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int i = buffer.readVarInt();
        int j = buffer.readVarInt();
        String s = buffer.readUtf(32767);
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

        for(int k = 0; k < nonnulllist.size(); ++k) {
            nonnulllist.set(k, Ingredient.fromNetwork(buffer));
        }

        ItemStack itemstack = buffer.readItem();
        return new AntikytheraRecipe(recipeId, s, i, j, nonnulllist, itemstack);
    }

    public void toNetwork(FriendlyByteBuf buffer, AntikytheraRecipe recipe) {
        buffer.writeVarInt(recipe.recipeWidth);
        buffer.writeVarInt(recipe.recipeHeight);
        buffer.writeUtf(recipe.group);

        for(Ingredient ingredient : recipe.recipeItems) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.recipeOutput);
    }

    public void init(){

    }
}
