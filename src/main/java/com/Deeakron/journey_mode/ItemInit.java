package com.Deeakron.journey_mode;

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
}
