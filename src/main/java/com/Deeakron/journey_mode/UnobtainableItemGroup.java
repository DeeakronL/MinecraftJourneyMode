package com.Deeakron.journey_mode;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class UnobtainableItemGroup extends ItemGroup {
    private boolean isSpecial = true;

    public UnobtainableItemGroup(String label) {
        super("Unobtainable");
    }


    @Override
    public ItemStack createIcon() {
        return new ItemStack(UnobtainBlockInit.UNOBTAINIUM_BLOCK.get());
    }
}
