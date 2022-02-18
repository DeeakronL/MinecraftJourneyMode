package com.Deeakron.journey_mode.data.recipebook;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class JMRecipeBookContainer<T> extends RecipeBookContainer {

    public JMRecipeBookContainer(ContainerType type, int id) {
        super(type, id);
    }

    public java.util.List<JMRecipeBookCategories> getRecipeBookCategories() {
        return JMRecipeBookCategories.func_243236_a(this.func_241850_m_2());
    }

    @OnlyIn(Dist.CLIENT)
    public abstract JMRecipeBookCategory func_241850_m_2();
}
