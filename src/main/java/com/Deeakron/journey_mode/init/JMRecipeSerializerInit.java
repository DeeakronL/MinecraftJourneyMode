package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.data.*;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMRecipeSerializerInit {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
            journey_mode.MODID);

    public static final RegistryObject<RecipeSerializer<AntikytheraRecipe>> ANTIKYTHERA_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_antikythera", () -> AntikytheraRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<AntikytheraShapelessRecipe>> ANTIKYTHERA_SHAPELESS_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_antikythera_shapeless", () -> AntikytheraShapelessRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<StarforgeRecipe>> STARFORGE_SERIALIZER = RECIPE_SERIALIZERS.register("starforge", () -> StarforgeRecipe.Serializer.INSTANCE);
    //public static final StarforgeRecipeSerializer STARFORGE_RECIPE_SERIALIZER = new StarforgeRecipeSerializer();
    //public static final AntikytheraRecipeSerializer ANTIKYTHERA_RECIPE_SERIALIZER = new AntikytheraRecipeSerializer();
    //public static final AntikytheraShapelessRecipeSerializer ANTIKYTHERA_SHAPELESS_RECIPE_SERIALIZER = new AntikytheraShapelessRecipeSerializer();

    //public static final RecipeSerializer<AntikytheraRecipe> ANTIKYTHERA_RECIPE = register("crafting_antikythera", new AntikytheraRecipeSerializer());
    //public static final RecipeSerializer<AntikytheraShapelessRecipe> ANTIKYTHERA_SHAPELESS_RECIPE = register("crafting_antikythera_shapeless", new AntikytheraShapelessRecipeSerializer());
    //public static final RecipeSerializer<StarforgeRecipe> STARFORGE_RECIPE = register("starforge", new StarforgeRecipeSerializer());

    //public static final RegistryObject<StarforgeRecipeSerializer> STARFORGE_SERIALIZER = RECIPE_SERIALIZERS.register("starforge", () -> STARFORGE_RECIPE_SERIALIZER);



    //public static final RegistryObject<AntikytheraShapelessRecipeSerializer> ANTIKYTHERA_SHAPELESS_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_antikythera_shapeless", () -> ANTIKYTHERA_SHAPELESS_RECIPE_SERIALIZER);

    //This rename may cause trouble, keep an eye out for it
    private static class JMRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }

    }

    private static <T extends RecipeType> T registerType(ResourceLocation recipeTypeId) {
        return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new JMRecipeType<>());
    }

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String p_44099_, S p_44100_) {
        return Registry.register(Registry.RECIPE_SERIALIZER, p_44099_, p_44100_);
    }

    private void registerRecipeType(ResourceLocation recipeTypeId, RecipeType recipeType) {
        Registry.register(Registry.RECIPE_TYPE, recipeTypeId, recipeType);
    }

    private JMRecipeSerializerInit(){
        //registerRecipeType(IAntikytheraRecipe.RECIPE_TYPE_ID, this.RECIPE_TYPE_ANTIKYTHERA);
    }

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}
