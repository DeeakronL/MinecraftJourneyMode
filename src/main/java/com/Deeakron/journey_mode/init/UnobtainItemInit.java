package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.item.*;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.Deeakron.journey_mode.init.DuplicationInit.unobtainableGroup;

public class UnobtainItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            journey_mode.MODID);

    public static final DeferredRegister<Item> MC_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static final RegistryObject<Item> COMMAND_BLOCK = MC_ITEMS.register("command_block",
            () -> new BlockItem(Blocks.COMMAND_BLOCK, new Item.Properties().tab(unobtainableGroup)));
            //new BlockItem((Blocks.COMMAND_BLOCK), new BlockItem.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> DRAGON_EGG = MC_ITEMS.register("dragon_egg",
            () -> new BlockItem(Blocks.DRAGON_EGG, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> BARRIER = MC_ITEMS.register("barrier",
            () -> new BlockItem(Blocks.BARRIER, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> CHAIN_COMMAND_BLOCK = MC_ITEMS.register("chain_command_block",
            () -> new BlockItem(Blocks.CHAIN_COMMAND_BLOCK, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> REPEATING_COMMAND_BLOCK = MC_ITEMS.register("repeating_command_block",
            () -> new BlockItem(Blocks.REPEATING_COMMAND_BLOCK, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> SPAWNER = MC_ITEMS.register("spawner",
            () -> new BlockItem(Blocks.SPAWNER, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> STRUCTURE_BLOCK = MC_ITEMS.register("structure_block",
            () -> new BlockItem(Blocks.STRUCTURE_BLOCK, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> STRUCTURE_VOID = MC_ITEMS.register("structure_void",
            () -> new BlockItem(Blocks.STRUCTURE_VOID, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> JIGSAW = MC_ITEMS.register("jigsaw",
            () -> new BlockItem(Blocks.JIGSAW, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> LIGHT = MC_ITEMS.register("light",
            () -> new BlockItem(Blocks.LIGHT, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> SCULK_SENSOR = MC_ITEMS.register("sculk_sensor",
            () -> new BlockItem(Blocks.SCULK_SENSOR, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> COMMAND_BLOCK_MINECART = MC_ITEMS.register("command_block_minecart",
            () -> new MinecartItem(null, new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> DEBUG_STICK = MC_ITEMS.register("debug_stick",
            () -> new DebugStickItem(new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> KNOWLEDGE_BOOK = MC_ITEMS.register("knowledge_book",
            () -> new KnowledgeBookItem(new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> SUSPICIOUS_STEW = MC_ITEMS.register("suspicious_stew",
            () -> new SuspiciousStewItem(new Item.Properties().tab(unobtainableGroup)));

    public static final RegistryObject<Item> BUNDLE = MC_ITEMS.register("bundle",
            () -> new BundleItem(new Item.Properties().tab(unobtainableGroup)));

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
