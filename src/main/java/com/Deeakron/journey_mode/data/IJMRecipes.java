package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class IJMRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, journey_mode.MODID);

    public static final class Types implements IRecipeType {
        public static final IRecipeType<AntikytheraRecipe> CRAFTING_ANTIKYTHERA = IRecipeType.register(
                journey_mode.MODID + ":crafting_antikythera"
        );


        private Types() {
    journey_mode.LOGGER.info(CRAFTING_ANTIKYTHERA);
        }
    }
    
    public static Types types = null;

    public static final class Serializers/*<T extends IRecipe<?>> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<T> */{
        public static final RegistryObject<IRecipeSerializer<?>> CRAFTING_ANTIKYTHERA = RECIPE_SERIALIZERS.register(
                "crafting_antikythera", AntikytheraRecipe.Serializer::new
        );
        //private final Function<ResourceLocation, T> field_222176_t;
       private Serializers(/*Function<ResourceLocation, T> p_i50024_1_*/) {
            //this.field_222176_t = p_i50024_1_;
        }
        /*
        public T read(ResourceLocation recipeId, JsonObject json) {
            return this.field_222176_t.apply(recipeId);
        }

        public T read(ResourceLocation recipeId, PacketBuffer buffer) {
            return this.field_222176_t.apply(recipeId);
        }

        public void write(PacketBuffer buffer, T recipe) {
        }*/
    }

    private IJMRecipes() {
    }

    public void register() {
        this.types = new Types();
    }
}
