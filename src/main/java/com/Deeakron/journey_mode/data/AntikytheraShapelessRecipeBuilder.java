package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AntikytheraShapelessRecipeBuilder extends ShapelessRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;

    public AntikytheraShapelessRecipeBuilder(ItemLike p_126114_, int p_126115_) {
        super(p_126114_, p_126115_);
        this.result = p_126114_.asItem();
        this.count = p_126115_;
    }

    public RecipeSerializer<?> getType() {
        return JMRecipeSerializerInit.ANTIKYTHERA_SHAPELESS_RECIPE_SERIALIZER;
    }

    public static AntikytheraShapelessRecipeBuilder shapeless(ItemLike p_126117_) {
        return shapeless(p_126117_, 1);
    }

    public static AntikytheraShapelessRecipeBuilder shapeless(ItemLike p_126119_, int p_126120_) {
        return new AntikytheraShapelessRecipeBuilder(p_126119_, p_126120_);
    }

    public AntikytheraShapelessRecipeBuilder requires(Tag<Item> p_126183_) {
        return this.requires(Ingredient.of(p_126183_));
    }

    public AntikytheraShapelessRecipeBuilder requires(ItemLike p_126210_) {
        return this.requires(p_126210_, 1);
    }

    public AntikytheraShapelessRecipeBuilder requires(ItemLike p_126212_, int p_126213_) {
        for(int i = 0; i < p_126213_; ++i) {
            this.requires(Ingredient.of(p_126212_));
        }

        return this;
    }

    public AntikytheraShapelessRecipeBuilder requires(Ingredient p_126185_) {
        return this.requires(p_126185_, 1);
    }

    public AntikytheraShapelessRecipeBuilder requires(Ingredient p_126187_, int p_126188_) {
        for(int i = 0; i < p_126188_; ++i) {
            this.ingredients.add(p_126187_);
        }

        return this;
    }

    public void save(Consumer<FinishedRecipe> p_126205_, ResourceLocation p_126206_) {
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126206_)).rewards(AdvancementRewards.Builder.recipe(p_126206_)).requirements(RequirementsStrategy.OR);
        p_126205_.accept(new AntikytheraShapelessRecipeBuilder.Result(p_126206_, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.advancement, new ResourceLocation(p_126206_.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + p_126206_.getPath())));
    }

    //slightly adjusted vanilla version
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation p_126222_, Item p_126223_, int p_126224_, String p_126225_, List<Ingredient> p_126226_, Advancement.Builder p_126227_, ResourceLocation p_126228_) {
            this.id = p_126222_;
            this.result = p_126223_;
            this.count = p_126224_;
            this.group = p_126225_;
            this.ingredients = p_126226_;
            this.advancement = p_126227_;
            this.advancementId = p_126228_;
        }

        public void serializeRecipeData(JsonObject p_126230_) {
            if (!this.group.isEmpty()) {
                p_126230_.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }

            p_126230_.add("ingredients", jsonarray);
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject.addProperty("count", this.count);
            }

            p_126230_.add("result", jsonobject);
        }

        public RecipeSerializer<?> getType() {
            return JMRecipeSerializerInit.ANTIKYTHERA_SHAPELESS_RECIPE_SERIALIZER;
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
