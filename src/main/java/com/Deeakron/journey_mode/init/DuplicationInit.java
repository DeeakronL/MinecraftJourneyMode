package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ItemStack;

public class DuplicationInit {
    public NonNullList<ItemStack> itemList;

    public static CreativeModeTab unobtainableGroup = new UnobtainableItemGroup("unobtainable");

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            "minecraft");

    public static NonNullList<ItemStack> init() {
        ItemList list = journey_mode.list;
        boolean test = false;
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < list.getItems().length; i++) {
            String string = list.getItems()[i];
            String substring = string.substring(1, string.length() - 1);

            ResourceLocation loc = new ResourceLocation(substring);
            ItemStack item = new ItemStack(ForgeRegistries.ITEMS.getValue(loc));
            try {
                ForgeRegistries.ITEMS.getValue(loc).getItemCategory().getDisplayName().getString();
            } catch (NullPointerException e) {
                String check = string.substring(1, 10);
                substring = string.substring(11, string.length() - 1);
                if(check.equals("minecraft" )) {
                    if (item.getItem().getClass() == new BlockItem(null, new BlockItem.Properties()).getClass() || item.getItem().getClass() == new GameMasterBlockItem(null, new Item.Properties()).getClass()) {
                        DuplicationInit.ITEMS.register(substring, () -> new BlockItem(((BlockItem) (item.getItem())).getBlock(), new BlockItem.Properties().tab(unobtainableGroup)));
                    } else {
                        if(item.getItem().getClass() == new DebugStickItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new DebugStickItem(new Item.Properties().tab(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new EnchantedBookItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new EnchantedBookItem(new Item.Properties().tab(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new KnowledgeBookItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new KnowledgeBookItem(new Item.Properties().tab(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new SuspiciousStewItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new SuspiciousStewItem(new Item.Properties().tab(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new MinecartItem(null, new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new MinecartItem(new MinecartCommandBlock(null, 0, 0, 0).getMinecartType(), new Item.Properties().tab(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new BundleItem(new Item.Properties()).getClass()) {
                            DuplicationInit.ITEMS.register(substring, () -> new BundleItem(new Item.Properties().tab(unobtainableGroup)));
                        } else {
                            DuplicationInit.ITEMS.register(substring, () -> new Item(new Item.Properties().tab(unobtainableGroup)));
                        }
                    }

                    items.add(item);
                }
            }
        }
        return items;
    }
}
