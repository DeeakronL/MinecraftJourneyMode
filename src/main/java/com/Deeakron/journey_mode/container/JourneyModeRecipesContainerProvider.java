package com.Deeakron.journey_mode.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;

public class JourneyModeRecipesContainerProvider implements MenuProvider {
    @Override
    public Component getDisplayName() {
        Component title = new TranslatableComponent("journey_mode.gui.tabs.recipes");
        return new TextComponent(title.getString());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory inv, Player player) {
        JourneyModeRecipesContainer container = new JourneyModeRecipesContainer(p_createMenu_1_);
        return container;
    }
}
