package com.Deeakron.journey_mode.data;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class StarforgeRecipeBuilder implements RecipeBuilder {
    private final Item output;
    private final Ingredient input;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;
    private final RecipeSerializer<?> serializer;

    private StarforgeRecipeBuilder(ItemLike p_126243_, Ingredient p_126244_, float p_126245_, int p_126246_, StarforgeRecipeSerializer p_126247_) {
        this.output = p_126243_.asItem();
        this.input = p_126244_;
        this.experience = p_126245_;
        this.cookingTime = p_126246_;
        this.serializer = p_126247_;
    }

    public static StarforgeRecipeBuilder cooking(Ingredient p_126249_, ItemLike p_126250_, float p_126251_, int p_126252_, StarforgeRecipeSerializer p_126253_) {
        return new StarforgeRecipeBuilder(p_126250_, p_126249_, p_126251_, p_126252_, p_126253_);
    }

    public StarforgeRecipeBuilder unlockedBy(String p_126255_, CriterionTriggerInstance p_126256_) {
        this.advancement.addCriterion(p_126255_, p_126256_);
        return this;
    }

    public StarforgeRecipeBuilder group(@Nullable String p_176795_) {
        this.group = p_176795_;
        return this;
    }

    public Item getResult() {
        return this.output;
    }

    public void save(Consumer<FinishedRecipe> p_126263_, ResourceLocation p_126264_) {
        this.ensureValid(p_126264_);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126264_)).rewards(AdvancementRewards.Builder.recipe(p_126264_)).requirements(RequirementsStrategy.OR);
        p_126263_.accept(new StarforgeRecipeBuilder.Result(p_126264_, this.group == null ? "" : this.group, this.input, this.output, this.experience, this.cookingTime, this.advancement, new ResourceLocation(p_126264_.getNamespace(), "recipes/" + this.output.getItemCategory().getRecipeFolderName() + "/" + p_126264_.getPath()), (RecipeSerializer<? extends AbstractCookingRecipe>) this.serializer));
    }

    private void ensureValid(ResourceLocation p_126266_) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + p_126266_);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final Item output;
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer serializer;

        public Result(ResourceLocation p_126287_, String p_126288_, Ingredient p_126289_, Item p_126290_, float p_126291_, int p_126292_, Advancement.Builder p_126293_, ResourceLocation p_126294_, RecipeSerializer<? extends AbstractCookingRecipe> p_126295_) {
            this.id = p_126287_;
            this.group = p_126288_;
            this.input = p_126289_;
            this.output = p_126290_;
            this.experience = p_126291_;
            this.cookingTime = p_126292_;
            this.advancement = p_126293_;
            this.advancementId = p_126294_;
            this.serializer = p_126295_;
        }

        public void serializeRecipeData(JsonObject p_126297_) {
            if (!this.group.isEmpty()) {
                p_126297_.addProperty("group", this.group);
            }

            p_126297_.add("input", this.input.toJson());
            p_126297_.addProperty("output", Registry.ITEM.getKey(this.output).toString());
            //p_126297_.addProperty("experience", this.experience);
            //p_126297_.addProperty("cookingtime", this.cookingTime);
        }

        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
