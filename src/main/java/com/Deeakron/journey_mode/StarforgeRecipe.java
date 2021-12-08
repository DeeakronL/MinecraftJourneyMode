package com.Deeakron.journey_mode;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class StarforgeRecipe implements IStarforgeRecipe {

    private final ResourceLocation id;
    private Ingredient fuel;
    private Ingredient input;
    private final ItemStack output;

    public StarforgeRecipe(ResourceLocation id, Ingredient fuel, Ingredient input, ItemStack output) {
        this.id = id;
        this.output = output;
        this.fuel = fuel;
        this.input = input;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        if(this.input.test(inv.getStackInSlot(0)) && this.fuel.test(inv.getStackInSlot(1))) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.output;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
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
        return null;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public Ingredient getFuel() {
        return this.fuel;
    }

    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(null, this.input, this.fuel);
    }
}
