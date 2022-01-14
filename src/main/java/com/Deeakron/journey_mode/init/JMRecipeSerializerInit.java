package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.data.*;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMRecipeSerializerInit {

    public static final StarforgeRecipeSerializer STARFORGE_RECIPE_SERIALIZER = new StarforgeRecipeSerializer();
    public static final IRecipeType<IStarforgeRecipe> RECIPE_TYPE = registerType(IStarforgeRecipe.RECIPE_TYPE_ID);
    public static final AntikytheraRecipeSerializer ANTIKYTHERA_RECIPE_SERIALIZER = new AntikytheraRecipeSerializer();
    public static final IRecipeType<AntikytheraRecipe> RECIPE_TYPE_ANTIKYTHERA = registerType(IAntikytheraRecipe.RECIPE_TYPE_ID);

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
            journey_mode.MODID);

    public static final RegistryObject<StarforgeRecipeSerializer> STARFORGE_SERIALIZER = RECIPE_SERIALIZERS.register("starforge", () -> STARFORGE_RECIPE_SERIALIZER);

    public static final RegistryObject<AntikytheraRecipeSerializer> ANTIKYTHERA_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_antikythera", () -> ANTIKYTHERA_RECIPE_SERIALIZER);

    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }

    private static <T extends IRecipeType> T registerType(ResourceLocation recipeTypeId) {
        return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
    }
}
