package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.journey_mode;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class StarforgeRecipe implements IStarforgeRecipe {

    private final ResourceLocation id;
    private Ingredient input;
    private final ItemStack output;

    public StarforgeRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if(this.input.test(inv.getItem(0))) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv) {
        return this.output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return JMRecipeSerializerInit.STARFORGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public Ingredient getInput() {
        return this.input;
    }


    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(null, this.input);
    }

    public static class Type implements RecipeType<StarforgeRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "starforge";
    }

    public static class Serializer implements RecipeSerializer<StarforgeRecipe> {
        public static final StarforgeRecipe.Serializer INSTANCE = new StarforgeRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(journey_mode.MODID, "starforge");

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

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return StarforgeRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}
