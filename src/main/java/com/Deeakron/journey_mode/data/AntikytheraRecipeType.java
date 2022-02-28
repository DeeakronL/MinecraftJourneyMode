package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.item.crafting.IRecipeType;

public class AntikytheraRecipeType implements IRecipeType {
    public static final IRecipeType<AntikytheraRecipe> CRAFTING_ANTIKYTHERA = IRecipeType.register(
            journey_mode.MODID + ":crafting_antikythera"
    );
    /*public static final IRecipeType<AntikytheraShapelessRecipe> CRAFTING_ANTIKYTHERA_SHAPELESS = IRecipeType.register(
            journey_mode.MODID + ":crafting_antikythera_shapeless"
    );*/


    public AntikytheraRecipeType() {
        //journey_mode.LOGGER.info(CRAFTING_ANTIKYTHERA);
        //journey_mode.LOGGER.info(CRAFTING_ANTIKYTHERA_SHAPELESS);
    }

    public void init() {

    }
}
