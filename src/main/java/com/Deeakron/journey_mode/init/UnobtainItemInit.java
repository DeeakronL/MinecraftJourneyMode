package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.item.PrimordialVoidDustItem;
import com.Deeakron.journey_mode.item.ScannerItem;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
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

    public static final RegistryObject<Item> PRIMORDIAL_VOID_DUST = ITEMS.register("primordial_void_dust",
            () -> new PrimordialVoidDustItem(new Item.Properties().group(ItemGroup.MATERIALS)));

    //Block Items
    public static final RegistryObject<BlockItem> UNOBTAINIUM_BLOCK = ITEMS.register("unobtainium_block",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<BlockItem> UNOBTAINIUM_RAW_BLOCK = ITEMS.register("unobtainium_raw_block",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_RAW_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<BlockItem> UNOBTAINIUM_ANTIKYTHERA = ITEMS.register("unobtainium_antikythera",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_ANTIKYTHERA.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    public static final RegistryObject<BlockItem> UNOBTAINIUM_STARFORGE = ITEMS.register("unobtainium_starforge",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    public static final RegistryObject<BlockItem> CRACKED_BEDROCK = ITEMS.register("cracked_bedrock",
            () -> new BlockItem(UnobtainBlockInit.CRACKED_BEDROCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<Item> SCANNER = ITEMS.register("scanner",
            () -> new ScannerItem(JMArmorMaterial.ARMOR_SCANNER, EquipmentSlotType.HEAD, (new Item.Properties()).group(ItemGroup.COMBAT)));


}