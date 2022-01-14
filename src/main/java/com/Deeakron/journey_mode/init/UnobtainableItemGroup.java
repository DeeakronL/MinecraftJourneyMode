package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class UnobtainableItemGroup extends ItemGroup {
    //public static final ItemGroup UNOBTAINABLES = new UnobtainableItemGroup("unobtainable");
    private boolean isSpecial = true;

    public UnobtainableItemGroup(String label) {
        super(journey_mode.MODID + "." + label);
    }


    @Override
    public ItemStack createIcon() {
        return new ItemStack(UnobtainBlockInit.UNOBTAINIUM_BLOCK.get());
    }

}
