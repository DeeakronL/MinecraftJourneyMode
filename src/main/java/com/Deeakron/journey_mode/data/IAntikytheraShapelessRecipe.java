package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;

public interface IAntikytheraShapelessRecipe extends IRecipe<RecipeWrapper> {
    ResourceLocation RECIPE_TYPE_ID = new ResourceLocation(journey_mode.MODID, "crafting_antikythera_shapeless");

    @Nonnull
    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOrDefault(RECIPE_TYPE_ID);
    }
}