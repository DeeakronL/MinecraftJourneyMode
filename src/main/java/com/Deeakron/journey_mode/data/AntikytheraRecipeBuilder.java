package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

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

    public ResourceLocation getId() {
        return IAntikytheraRecipe.RECIPE_TYPE_ID;
    }

    //slightly adjusted vanilla version
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation p_176754_, Item p_176755_, int p_176756_, String p_176757_, List<String> p_176758_, Map<Character, Ingredient> p_176759_, Advancement.Builder p_176760_, ResourceLocation p_176761_) {
            this.id = p_176754_;
            this.result = p_176755_;
            this.count = p_176756_;
            this.group = p_176757_;
            this.pattern = p_176758_;
            this.key = p_176759_;
            this.advancement = p_176760_;
            this.advancementId = p_176761_;
        }

        public void serializeRecipeData(JsonObject p_126167_) {
            if (!this.group.isEmpty()) {
                p_126167_.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(String s : this.pattern) {
                jsonarray.add(s);
            }

            p_126167_.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();

            for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }

            p_126167_.add("key", jsonobject);
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject1.addProperty("count", this.count);
            }

            p_126167_.add("result", jsonobject1);
        }

        public RecipeSerializer<?> getType() {
            return JMRecipeSerializerInit.ANTIKYTHERA_RECIPE_SERIALIZER;
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
