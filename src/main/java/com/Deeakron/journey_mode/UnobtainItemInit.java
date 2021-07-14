package com.Deeakron.journey_mode;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class UnobtainItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            journey_mode.MODID);

    public static final RegistryObject<Item> UNOBTAINIUM_INGOT = ITEMS.register("unobtainium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> UNOBTAINIUM_RAW = ITEMS.register("unobtainium_raw", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));

    //Block Items
    public static final RegistryObject<BlockItem> UNOBTAINIUM_BLOCK = ITEMS.register("unobtainium_block",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<BlockItem> UNOBTAINIUM_RAW_BLOCK = ITEMS.register("unobtainium_raw_block",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_RAW_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
}
