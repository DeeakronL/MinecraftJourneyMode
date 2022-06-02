package com.Deeakron.journey_mode.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DuplicationListener implements ContainerListener {
    private final Minecraft mc;

    public DuplicationListener(Minecraft mc) {
        this.mc = mc;
    }

    /**
     * update the crafting window inventory with the items in the list
     */
    public void refreshContainer(AbstractContainerMenu containerToSend, NonNullList<ItemStack> itemsList) {
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot.
     */
    public void slotChanged(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
        this.mc.gameMode.handleCreativeModeItemAdd(stack, slotInd);
    }

    @Override
    public void dataChanged(AbstractContainerMenu p_150524_, int p_150525_, int p_150526_) {

    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    public void setContainerData(Container containerIn, int varToUpdate, int newValue) {
    }
}
