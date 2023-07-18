package com.Deeakron.journey_mode.container;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class StarforgeFuelSlot extends SlotItemHandler {
    private final IItemHandlerModifiable handler;

    public StarforgeFuelSlot(IItemHandlerModifiable handler, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
        super(handler, p_i50084_3_, p_i50084_4_, p_i50084_5_);
        this.handler = handler;
    }

    public boolean mayPlace(ItemStack stack) {
        String item = "\"" + ForgeRegistries.ITEMS.getKey(stack.getItem()) + "\"";
        String check = "\"minecraft:nether_star\"";
        return item.equals(check);
    }
}
