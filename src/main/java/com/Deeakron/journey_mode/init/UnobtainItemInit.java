package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.item.AetherialVoidDustItem;
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

import static com.Deeakron.journey_mode.init.DuplicationInit.unobtainableGroup;

public class UnobtainItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            journey_mode.MODID);

    public static final RegistryObject<Item> UNOBTAINIUM_INGOT = ITEMS.register("unobtainium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> UNOBTAINIUM_RAW = ITEMS.register("unobtainium_raw", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));

    public static final RegistryObject<Item> PRIMORDIAL_VOID_DUST = ITEMS.register("primordial_void_dust",
            () -> new PrimordialVoidDustItem(new Item.Properties().group(ItemGroup.MATERIALS)));

    public static final RegistryObject<Item> AETHERIAL_VOID_DUST = ITEMS.register("aetherial_void_dust",
            () -> new AetherialVoidDustItem(new Item.Properties().group(ItemGroup.MATERIALS)));

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

    public static final RegistryObject<BlockItem> INERT_COMMAND_BLOCK = ITEMS.register("inert_command_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_COMMAND_BLOCK.get(), new Item.Properties().group(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_CHAIN_COMMAND_BLOCK = ITEMS.register("inert_chain_command_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get(), new Item.Properties().group(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_REPEATING_COMMAND_BLOCK = ITEMS.register("inert_repeating_command_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get(), new Item.Properties().group(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_STRUCTURE_BLOCK = ITEMS.register("inert_structure_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_STRUCTURE_BLOCK.get(), new Item.Properties().group(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_JIGSAW_BLOCK = ITEMS.register("inert_jigsaw_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_JIGSAW_BLOCK.get(), new Item.Properties().group(unobtainableGroup)));

    public static final RegistryObject<BlockItem> PAINTED_BARRIER = ITEMS.register("painted_barrier",
            () -> new BlockItem(UnobtainBlockInit.PAINTED_BARRIER.get(), new Item.Properties().group(unobtainableGroup)));

    public static final RegistryObject<Item> SCANNER = ITEMS.register("scanner",
            () -> new ScannerItem(JMArmorMaterial.ARMOR_SCANNER, EquipmentSlotType.HEAD, (new Item.Properties()).group(ItemGroup.COMBAT)));


}
