package com.Deeakron.journey_mode.data.recipebook;

import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.google.common.collect.*;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JMRecipeBook extends ClientRecipeBook {
    private static final Logger LOGGER = LogManager.getLogger();
    private Map<JMRecipeBookCategories, List<RecipeList>> recipesByCategory = ImmutableMap.of();
    private List<RecipeList> allRecipes = ImmutableList.of();
    private final JMRecipeBookStatus field_242138_c = new JMRecipeBookStatus();

    public void func_243196_a(Iterable<IRecipe<?>> p_243196_1_) {
        Map<JMRecipeBookCategories, List<List<IRecipe<?>>>> map = func_243201_b(p_243196_1_);
        Map<JMRecipeBookCategories, List<RecipeList>> map1 = Maps.newHashMap();
        ImmutableList.Builder<RecipeList> builder = ImmutableList.builder();
        map.forEach((p_243197_2_, p_243197_3_) -> {
            List list = map1.put(p_243197_2_, p_243197_3_.stream().map(RecipeList::new).peek(builder::add).collect(ImmutableList.toImmutableList()));
        });
        JMRecipeBookCategories.field_243235_w.forEach((p_243199_1_, p_243199_2_) -> {
            List list = map1.put(p_243199_1_, p_243199_2_.stream().flatMap((p_243198_1_) -> {
                return map1.getOrDefault(p_243198_1_, ImmutableList.of()).stream();
            }).collect(ImmutableList.toImmutableList()));
        });
        this.recipesByCategory = ImmutableMap.copyOf(map1);
        this.allRecipes = builder.build();
    }

    private static Map<JMRecipeBookCategories, List<List<IRecipe<?>>>> func_243201_b(Iterable<IRecipe<?>> p_243201_0_) {
        Map<JMRecipeBookCategories, List<List<IRecipe<?>>>> map = Maps.newHashMap();
        Table<JMRecipeBookCategories, String, List<IRecipe<?>>> table = HashBasedTable.create();

        for(IRecipe<?> irecipe : p_243201_0_) {
            if (!irecipe.isDynamic()) {
                JMRecipeBookCategories recipebookcategories = getCategory(irecipe);
                String s = irecipe.getGroup();
                if (s.isEmpty()) {
                    map.computeIfAbsent(recipebookcategories, (p_243202_0_) -> {
                        return Lists.newArrayList();
                    }).add(ImmutableList.of(irecipe));
                } else {
                    List<IRecipe<?>> list = table.get(recipebookcategories, s);
                    if (list == null) {
                        list = Lists.newArrayList();
                        table.put(recipebookcategories, s, list);
                        map.computeIfAbsent(recipebookcategories, (p_202890_0_) -> {
                            return Lists.newArrayList();
                        }).add(list);
                    }

                    list.add(irecipe);
                }
            }
        }

        return map;
    }

    private static JMRecipeBookCategories getCategory(IRecipe<?> recipe) {
        IRecipeType<?> irecipetype = recipe.getType();
        if (irecipetype == JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA || irecipetype == JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA_SHAPELESS) {
            ItemStack itemstack = recipe.getRecipeOutput();
            ItemGroup itemgroup = itemstack.getItem().getGroup();
            if (itemgroup == ItemGroup.BUILDING_BLOCKS) {
                return JMRecipeBookCategories.ANTIKYTHERA_BUILDING_BLOCKS;
            } else if (itemgroup != ItemGroup.TOOLS && itemgroup != ItemGroup.COMBAT) {
                return itemgroup == ItemGroup.REDSTONE ? JMRecipeBookCategories.ANTIKYTHERA_REDSTONE : JMRecipeBookCategories.ANTIKYTHERA_MISC;
            } else {
                return JMRecipeBookCategories.ANTIKYTHERA_EQUIPMENT;
            }
        } else if (irecipetype == JMRecipeSerializerInit.RECIPE_TYPE) {
            if (recipe.getRecipeOutput().getItem().isFood()) {
                return JMRecipeBookCategories.STARFORGE_FOOD;
            } else {
                return recipe.getRecipeOutput().getItem() instanceof BlockItem ? JMRecipeBookCategories.STARFORGE_BLOCKS : JMRecipeBookCategories.STARFORGE_MISC;
            }
        } else {
            LOGGER.warn("Unknown recipe category: {}/{}", () -> {
                return Registry.RECIPE_TYPE.getKey(recipe.getType());
            }, recipe::getId);
            return JMRecipeBookCategories.UNKNOWN;
        }
    }

    public List<RecipeList> getRecipes() {
        return this.allRecipes;
    }

    public List<RecipeList> getRecipes(JMRecipeBookCategories categories) {
        return this.recipesByCategory.getOrDefault(categories, Collections.emptyList());
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_242145_b(JMRecipeBookCategory p_242145_1_) {
        return this.field_242138_c.func_242158_b_2(p_242145_1_);
    }
    @OnlyIn(Dist.CLIENT)
    public void func_242146_b(JMRecipeBookCategory p_242146_1_, boolean p_242146_2_) {
        this.field_242138_c.func_242159_b(p_242146_1_, p_242146_2_);
    }



    public JMRecipeBookStatus func_242139_a_2() {
        return this.field_242138_c.func_242149_a();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_242142_a(JMRecipeBookCategory p_242142_1_) {
        return this.field_242138_c.func_242151_a_2(p_242142_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public void func_242143_a(JMRecipeBookCategory p_242143_1_, boolean p_242143_2_) {
        this.field_242138_c.func_242152_a_2(p_242143_1_, p_242143_2_);
    }

}
