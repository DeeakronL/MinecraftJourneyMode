package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class AntikytheraRecipeBuilder extends ShapedRecipeBuilder {

    public AntikytheraRecipeBuilder(ItemLike p_126114_, int p_126115_) {
        super(p_126114_, p_126115_);
    }

    public RecipeSerializer<?> getType() {
        return JMRecipeSerializerInit.ANTIKYTHERA_RECIPE_SERIALIZER;
    }

    public static AntikytheraRecipeBuilder shaped(ItemLike p_126117_) {
        return shaped(p_126117_, 1);
    }

    public static AntikytheraRecipeBuilder shaped(ItemLike p_126119_, int p_126120_) {
        return new AntikytheraRecipeBuilder(p_126119_, p_126120_);
    }
}
