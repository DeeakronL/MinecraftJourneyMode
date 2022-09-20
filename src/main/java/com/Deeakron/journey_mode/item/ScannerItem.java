package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;


public class ScannerItem extends ArmorItem {
    public ScannerItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return journey_mode.MODID + ":textures/armor/scanner_layer_1.png";
    }
}
