package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.client.gui.JourneyModeDuplicationScreen;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class JourneyModeDuplicationContainerProvider implements MenuProvider {
    @Override
    public Component getDisplayName() {
        Component title = Component.translatable("journey_mode.gui.name");
        return Component.literal(title.getString());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory inv, Player player) {
        return new JourneyModeDuplicationScreen.DuplicationContainer(player, journey_mode.tempList);
    }
}
