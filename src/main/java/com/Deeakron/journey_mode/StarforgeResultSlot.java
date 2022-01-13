package com.Deeakron.journey_mode;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class StarforgeResultSlot extends SlotItemHandler {
    private final IItemHandlerModifiable handler;

    public StarforgeResultSlot(IItemHandlerModifiable handler, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
        super(handler, p_i50084_3_, p_i50084_4_, p_i50084_5_);
        this.handler = handler;
    }

    public boolean isItemValid(ItemStack stack) {
        return false;
    }
}
