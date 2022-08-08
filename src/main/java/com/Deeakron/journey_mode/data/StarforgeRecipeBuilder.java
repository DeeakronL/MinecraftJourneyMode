package com.Deeakron.journey_mode.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class StarforgeRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;

    private StarforgeRecipeBuilder(ItemLike p_126243_, Ingredient p_126244_, float p_126245_, int p_126246_) {
        this.result = p_126243_.asItem();
        this.ingredient = p_126244_;
        this.experience = p_126245_;
        this.cookingTime = p_126246_;
    }

    @Override
    public RecipeBuilder unlockedBy(String p_176496_, CriterionTriggerInstance p_176497_) {
        this.advancement.addCriterion(p_176496_, p_176497_);
        return null;
    }

    @Override
    public RecipeBuilder group(@Nullable String p_176495_) {
        return null;
    }

    @Override
    public Item getResult() {
        return null;
    }

    @Override
    public void save(Consumer<FinishedRecipe> p_176503_, ResourceLocation p_176504_) {

    }
}
