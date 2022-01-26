package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.item.ResearchGrinderItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.Deeakron.journey_mode.journey_mode.MODID;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<BlockItem> WOODEN_RESEARCH_GRINDER = ITEMS.register("wooden_research_grinder",
            () -> new ResearchGrinderItem(BlockInit.WOODEN_RESEARCH_GRINDER.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    public static final RegistryObject<BlockItem> IRON_RESEARCH_GRINDER = ITEMS.register("iron_research_grinder",
            () -> new ResearchGrinderItem(BlockInit.IRON_RESEARCH_GRINDER.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    public static final RegistryObject<BlockItem> DIAMOND_RESEARCH_GRINDER = ITEMS.register("diamond_research_grinder",
            () -> new ResearchGrinderItem(BlockInit.DIAMOND_RESEARCH_GRINDER.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
}