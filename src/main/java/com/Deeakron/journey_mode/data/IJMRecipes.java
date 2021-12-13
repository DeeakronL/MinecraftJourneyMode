package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.item.crafting.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class IJMRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, journey_mode.MODID);
    public static final RegistryObject<AntikytheraRecipeSerializer<AntikytheraRecipe>> ANTIKYTHERA_RECIPES = RECIPE_SERIALIZERS.register("crafting_antikythera", AntikytheraRecipeSerializer::new);
    //public static AntikytheraRecipeSerializer<A> serializer = null;
    public static AntikytheraRecipeType type = null;

    //public static final class Types implements IRecipeType {
        public static final IRecipeType<AntikytheraRecipe> CRAFTING_ANTIKYTHERA = IRecipeType.register(
                journey_mode.MODID + ":crafting_antikythera"
        );


        //private Types() {
    //journey_mode.LOGGER.info(CRAFTING_ANTIKYTHERA);
        //}
    //}

    //public static Types types = null;

    /*public static final class Serializers<T extends IRecipe<?>> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<T> {
        public static final RegistryObject<IRecipeSerializer<?>> CRAFTING_ANTIKYTHERA = RECIPE_SERIALIZERS.register(
                "crafting_antikythera", AntikytheraRecipe.Serializer::new
        );
        //private final Function<ResourceLocation, T> field_222176_t;
       private Serializers(Function<ResourceLocation, T> p_i50024_1_) {
            //this.field_222176_t = p_i50024_1_;
        }

        public T read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new AntikytheraRecipe(recipeId, "",)
        }

        public T read(ResourceLocation recipeId, PacketBuffer buffer) {
            return this.field_222176_t.apply(recipeId);
        }

        public void write(PacketBuffer buffer, T recipe) {
        }
    }*/
    /*public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<AntikytheraRecipe> {
        public static final RegistryObject<IRecipeSerializer<?>> CRAFTING_ANTIKYTHERA = RECIPE_SERIALIZERS.register(
                "crafting_antikythera", AntikytheraRecipeSerializers::new
        );
        private static final ResourceLocation NAME = new ResourceLocation("journey_mode", "crafting_antikythera");
        public AntikytheraRecipe read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            Map<String, Ingredient> map = AntikytheraRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
            String[] astring = AntikytheraRecipe.shrink(AntikytheraRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = AntikytheraRecipe.deserializeIngredients(astring, map, i, j);
            ItemStack itemstack = AntikytheraRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new AntikytheraRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }

        public AntikytheraRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readString(32767);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.read(buffer));
            }

            ItemStack itemstack = buffer.readItemStack();
            return new AntikytheraRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }

        public void write(PacketBuffer buffer, AntikytheraRecipe recipe) {
            buffer.writeVarInt(recipe.recipeWidth);
            buffer.writeVarInt(recipe.recipeHeight);
            buffer.writeString(recipe.group);

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.recipeOutput);
        }
    }*/

    private IJMRecipes() {
    }

    public void register() {

    }
}
