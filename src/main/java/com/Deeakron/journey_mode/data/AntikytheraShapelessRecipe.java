package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;


public class AntikytheraShapelessRecipe implements ICraftingRecipe {

    public final ResourceLocation id;
    public final String group;
    public final ItemStack recipeOutput;
    public final NonNullList<Ingredient> recipeItems;
    public final boolean isSimple;

    public AntikytheraShapelessRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        this.id = idIn;
        this.group = groupIn;
        this.recipeOutput = recipeOutputIn;
        this.recipeItems = recipeItemsIn;
        this.isSimple = recipeItemsIn.stream().allMatch(Ingredient::isSimple);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public IRecipeType<?> getType() {
        return JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA_SHAPELESS;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return JMRecipeSerializerInit.ANTIKYTHERA_SHAPELESS_SERIALIZER.get();
    }

    /**
     * Recipes with equal group are combined into one button in the recipe book
     */
    public String getGroup() {
        return this.group;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CraftingInventory inv, World worldIn) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    recipeitemhelper.func_221264_a(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        return i == this.recipeItems.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.recipeItems) != null);
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return this.recipeOutput.copy();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height) {
        return width * height >= this.recipeItems.size();
    }

}
