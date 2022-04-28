package com.Deeakron.journey_mode.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class JourneyModeRecipesContainerProvider implements INamedContainerProvider {
    @Override
    public ITextComponent getDisplayName() {
        ITextComponent title = new TranslationTextComponent("journey_mode.gui.tabs.recipes");
        return new StringTextComponent(title.getString());
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory inv, PlayerEntity player) {
        JourneyModeRecipesContainer container = new JourneyModeRecipesContainer(p_createMenu_1_);
        return container;
    }
}
