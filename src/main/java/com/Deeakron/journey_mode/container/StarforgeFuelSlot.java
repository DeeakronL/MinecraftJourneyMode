package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class StarforgeFuelSlot extends SlotItemHandler {
    private final IItemHandlerModifiable handler;

    public StarforgeFuelSlot(IItemHandlerModifiable handler, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
        super(handler, p_i50084_3_, p_i50084_4_, p_i50084_5_);
        this.handler = handler;
        journey_mode.LOGGER.info("huh?");
    }

    public boolean mayPlace(ItemStack stack) {
        String item = "\"" + stack.getItem().getRegistryName() + "\"";
        String check = "\"minecraft:nether_star\"";
        return item.equals(check);
    }
}
