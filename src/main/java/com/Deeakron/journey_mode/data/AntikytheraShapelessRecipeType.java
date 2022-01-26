package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.item.crafting.IRecipeType;

public class AntikytheraShapelessRecipeType implements IRecipeType {
    /*public static final IRecipeType<AntikytheraRecipe> CRAFTING_ANTIKYTHERA = IRecipeType.register(
            journey_mode.MODID + ":crafting_antikythera"
    );*/
    public static final IRecipeType<AntikytheraShapelessRecipe> CRAFTING_ANTIKYTHERA_SHAPELESS = IRecipeType.register(
            journey_mode.MODID + ":crafting_antikythera_shapeless"
    );


    public AntikytheraShapelessRecipeType() {
        journey_mode.LOGGER.info(CRAFTING_ANTIKYTHERA_SHAPELESS);
    }

    public void init() {

    }
}
