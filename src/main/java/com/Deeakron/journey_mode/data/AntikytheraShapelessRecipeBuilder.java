package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class AntikytheraShapelessRecipeBuilder extends ShapelessRecipeBuilder {
    public AntikytheraShapelessRecipeBuilder(ItemLike p_126114_, int p_126115_) {
        super(p_126114_, p_126115_);
    }

    public RecipeSerializer<?> getType() {
        return JMRecipeSerializerInit.ANTIKYTHERA_SHAPELESS_RECIPE_SERIALIZER;
    }
}
