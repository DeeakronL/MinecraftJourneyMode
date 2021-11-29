package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.client.gui.JourneyModeDuplicationScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class UnobtainiumAntikytheraContainerProvider implements INamedContainerProvider {
    @Override
    public ITextComponent getDisplayName() {
        ITextComponent title = new TranslationTextComponent("container.journey_mode.unobtainium_antikythera");
        return new StringTextComponent(title.getString());
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory inv, PlayerEntity player) {
        UnobtainiumAntikytheraContainer container = new UnobtainiumAntikytheraContainer(p_createMenu_1_, inv);
        /*if (!player.world.isRemote()) {
            journey_mode.tempContain = container;
        }*/
        return container;
    }
}
