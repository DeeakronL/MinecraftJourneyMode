package com.Deeakron.journey_mode.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class StarforgeFuelSlot extends SlotItemHandler {
    private final IItemHandlerModifiable handler;

    public StarforgeFuelSlot(IItemHandlerModifiable handler, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
        super(handler, p_i50084_3_, p_i50084_4_, p_i50084_5_);
        this.handler = handler;
    }

    public boolean isItemValid(ItemStack stack) {
        String item = "\"" + stack.getItem().getItem().getRegistryName() + "\"";
        String check = "\"minecraft:nether_star\"";
        return item.equals(check);
    }
}
