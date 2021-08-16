package com.Deeakron.journey_mode;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class JourneyModePowersContainerProvider implements INamedContainerProvider {
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Journey Mode Menu");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory inv, PlayerEntity player) {
        return new JourneyModePowersContainer(p_createMenu_1_, inv);
    }
}
