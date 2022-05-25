package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;

public interface IStarforgeRecipe extends Recipe<RecipeWrapper> {
    ResourceLocation RECIPE_TYPE_ID = new ResourceLocation(journey_mode.MODID, "starforge");

    @Nonnull
    @Override
    default RecipeType<?> getType() {
        return Registry.RECIPE_TYPE.get(RECIPE_TYPE_ID);
    }
}
