package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public class AntikytheraRecipe implements CraftingRecipe {

    public final int recipeWidth;
    public final int recipeHeight;
    public final NonNullList<Ingredient> recipeItems;
    public final ItemStack recipeOutput;
    public final ResourceLocation id;
    public final String group;

    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;
    public static void setCraftingSize(int width, int height) {
        if (MAX_WIDTH < width) MAX_WIDTH = width;
        if (MAX_HEIGHT < height) MAX_HEIGHT = height;
    }

    public AntikytheraRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        this.id = idIn;
        this.group = groupIn;
        this.recipeWidth = recipeWidthIn;
        this.recipeHeight = recipeHeightIn;
        this.recipeItems = recipeItemsIn;
        this.recipeOutput = recipeOutputIn;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    /**
     * Recipes with equal group are combined into one button in the recipe book
     */
    public String getGroup() {
        return this.group;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getResultItem() {
        return this.recipeOutput;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.recipeWidth && height >= this.recipeHeight;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CraftingContainer inv, Level worldIn) {
        for(int i = 0; i <= inv.getWidth() - this.recipeWidth; ++i) {
            for(int j = 0; j <= inv.getHeight() - this.recipeHeight; ++j) {
                if (this.checkMatch(inv, i, j, true)) {
                    return true;
                }

                if (this.checkMatch(inv, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkMatch(CraftingContainer CraftingContainer, int width, int height, boolean p_77573_4_) {
        for(int i = 0; i < CraftingContainer.getWidth(); ++i) {
            for(int j = 0; j < CraftingContainer.getHeight(); ++j) {
                int k = i - width;
                int l = j - height;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                    if (p_77573_4_) {
                        ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                    }
                }

                if (!ingredient.test(CraftingContainer.getItem(i + j * CraftingContainer.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(CraftingContainer inv) {
        return this.getResultItem().copy();
    }

    @Nonnull
    @Override
    public IRecipeType<?> getType() {
        return JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA;
    }


    public int getWidth() {
        return this.recipeWidth;
    }

    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.recipeHeight;
    }

    public int getRecipeHeight() {
        return getHeight();
    }

    public static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(keys.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keys.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + patternWidth * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return JMRecipeSerializerInit.ANTIKYTHERA_SERIALIZER.get();
    }

    public static Map<String, Ingredient> deserializeKey(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack deserializeItem(JsonObject object) {
        String s = JSONUtils.getAsString(object, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JSONUtils.getAsInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }

    @VisibleForTesting
    static String[] shrink(String... toShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < toShrink.length; ++i1) {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (toShrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[toShrink.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = toShrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String str) {
        int i;
        for(i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String str) {
        int i;
        for(i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
        }

        return i;
    }

    public static String[] patternFromJson(JsonArray jsonArr) {
        String[] astring = new String[jsonArr.size()];
        if (astring.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = JSONUtils.convertToString(jsonArr.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }
}
