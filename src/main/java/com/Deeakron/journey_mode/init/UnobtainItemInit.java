package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.item.*;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.Deeakron.journey_mode.init.DuplicationInit.unobtainableGroup;

public class UnobtainItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            journey_mode.MODID);

    public static final RegistryObject<Item> UNOBTAINIUM_INGOT = ITEMS.register("unobtainium_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> RAW_UNOBTAINIUM = ITEMS.register("raw_unobtainium", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> PRIMORDIAL_VOID_DUST = ITEMS.register("primordial_void_dust",
            () -> new PrimordialVoidDustItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> AETHERIAL_VOID_DUST = ITEMS.register("aetherial_void_dust",
            () -> new AetherialVoidDustItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> PAINT_BUCKET = ITEMS.register("paint_bucket",
            () -> new PaintBucketItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> UNOBTAINIUM_HAMMER = ITEMS.register("unobtainium_hammer",
            () -> new UnobtainiumHammerItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<Item> SCANNER = ITEMS.register("scanner",
            () -> new ScannerItem(JMArmorMaterial.ARMOR_SCANNER, EquipmentSlot.HEAD, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));

    //Block Items
    public static final RegistryObject<BlockItem> UNOBTAINIUM_BLOCK = ITEMS.register("unobtainium_block",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<BlockItem> RAW_UNOBTAINIUM_BLOCK = ITEMS.register("raw_unobtainium_block",
            () -> new BlockItem(UnobtainBlockInit.RAW_UNOBTAINIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<BlockItem> UNOBTAINIUM_ANTIKYTHERA = ITEMS.register("unobtainium_antikythera",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_ANTIKYTHERA.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static final RegistryObject<BlockItem> UNOBTAINIUM_STARFORGE = ITEMS.register("unobtainium_starforge",
            () -> new BlockItem(UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static final RegistryObject<BlockItem> CRACKED_BEDROCK = ITEMS.register("cracked_bedrock",
            () -> new BlockItem(UnobtainBlockInit.CRACKED_BEDROCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<BlockItem> INERT_COMMAND_BLOCK = ITEMS.register("inert_command_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_COMMAND_BLOCK.get(), new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_CHAIN_COMMAND_BLOCK = ITEMS.register("inert_chain_command_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get(), new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_REPEATING_COMMAND_BLOCK = ITEMS.register("inert_repeating_command_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get(), new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_STRUCTURE_BLOCK = ITEMS.register("inert_structure_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_STRUCTURE_BLOCK.get(), new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<BlockItem> INERT_JIGSAW_BLOCK = ITEMS.register("inert_jigsaw_block",
            () -> new BlockItem(UnobtainBlockInit.INERT_JIGSAW_BLOCK.get(), new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<BlockItem> PAINTED_BARRIER = ITEMS.register("painted_barrier",
            () -> new BlockItem(UnobtainBlockInit.PAINTED_BARRIER.get(), new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<BlockItem> BROKEN_LIGHT = ITEMS.register("broken_light",
            () -> new BlockItem(UnobtainBlockInit.BROKEN_LIGHT.get(), new Item.Properties().tab(unobtainableGroup)));


}
