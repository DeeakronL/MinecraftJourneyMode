package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

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
    public IRecipeSerializer<?> getSerializer() {
        return JMRecipeSerializerInit.STARFORGE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return JMRecipeSerializerInit.RECIPE_TYPE;
    }

    public Ingredient getInput() {
        return this.input;
    }


    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(null, this.input);
    }
}
