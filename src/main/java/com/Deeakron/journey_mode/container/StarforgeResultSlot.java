package com.Deeakron.journey_mode.container;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class StarforgeResultSlot extends SlotItemHandler {
    private final IItemHandlerModifiable handler;

    public StarforgeResultSlot(IItemHandlerModifiable handler, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
        super(handler, p_i50084_3_, p_i50084_4_, p_i50084_5_);
        this.handler = handler;
    }

    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}
