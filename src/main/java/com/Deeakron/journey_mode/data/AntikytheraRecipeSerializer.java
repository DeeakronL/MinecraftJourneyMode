package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class AntikytheraRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<AntikytheraRecipe> {
    /*public static final RegistryObject<IRecipeSerializer<?>> CRAFTING_ANTIKYTHERA = IJMRecipes.RECIPE_SERIALIZERS.register(
            "crafting_antikythera", AntikytheraRecipeSerializer::new
    );*/
    private static final ResourceLocation NAME = new ResourceLocation("journey_mode", "crafting_antikythera");
    public AntikytheraRecipe read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "CRAFTING");
        Map<String, Ingredient> map = AntikytheraRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
        String[] astring = AntikytheraRecipe.shrink(AntikytheraRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = AntikytheraRecipe.deserializeIngredients(astring, map, i, j);
        ItemStack itemstack = AntikytheraRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        journey_mode.LOGGER.info("String is: " + s);
        return new AntikytheraRecipe(recipeId, s, i, j, nonnulllist, itemstack);
    }

    public AntikytheraRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        int i = buffer.readVarInt();
        int j = buffer.readVarInt();
        String s = buffer.readString(32767);
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

        for(int k = 0; k < nonnulllist.size(); ++k) {
            nonnulllist.set(k, Ingredient.read(buffer));
        }

        ItemStack itemstack = buffer.readItemStack();
        return new AntikytheraRecipe(recipeId, s, i, j, nonnulllist, itemstack);
    }

    public void write(PacketBuffer buffer, AntikytheraRecipe recipe) {
        buffer.writeVarInt(recipe.recipeWidth);
        buffer.writeVarInt(recipe.recipeHeight);
        buffer.writeString(recipe.group);

        for(Ingredient ingredient : recipe.recipeItems) {
            ingredient.write(buffer);
        }

        buffer.writeItemStack(recipe.recipeOutput);
    }

    public void init(){

    }
}
