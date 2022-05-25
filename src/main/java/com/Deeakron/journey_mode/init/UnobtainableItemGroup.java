package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class UnobtainableItemGroup extends CreativeModeTab {
    private boolean isSpecial = true;

    public UnobtainableItemGroup(String label) {
        super(journey_mode.MODID + "." + label);
    }


    @Override
    public ItemStack makeIcon() {
        return new ItemStack(UnobtainBlockInit.UNOBTAINIUM_BLOCK.get());
    }

}
