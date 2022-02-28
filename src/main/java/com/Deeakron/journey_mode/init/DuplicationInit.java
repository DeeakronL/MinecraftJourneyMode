package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.entity.item.minecart.CommandBlockMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DuplicationInit {
    public NonNullList<ItemStack> itemList;

    public static ItemGroup unobtainableGroup = new UnobtainableItemGroup("unobtainable");

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
                ForgeRegistries.ITEMS.getValue(loc).getGroup().getGroupName().getString();
            } catch (NullPointerException e) {
                String check = string.substring(1, 13);
                substring = string.substring(11, string.length() - 1);
                //journey_mode.LOGGER.info("substring is: " + substring);
                if(!check.equals("journey_mode")) {
                    //journey_mode.LOGGER.info(item.getItem().getClass());
                    //journey_mode.LOGGER.info(new OperatorOnlyItem(null, new OperatorOnlyItem.Properties()).getClass());
                    if (item.getItem().getClass() == new BlockItem(null, new BlockItem.Properties()).getClass() || item.getItem().getClass() == new OperatorOnlyItem(null, new Item.Properties()).getClass()) {
                        DuplicationInit.ITEMS.register(substring, () -> new BlockItem(((BlockItem) (item.getItem())).getBlock(), new BlockItem.Properties().group(unobtainableGroup)));
                    } else {
                        if(item.getItem().getClass() == new DebugStickItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new DebugStickItem(new Item.Properties().group(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new EnchantedBookItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new EnchantedBookItem(new Item.Properties().group(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new KnowledgeBookItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new KnowledgeBookItem(new Item.Properties().group(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new SuspiciousStewItem(new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new SuspiciousStewItem(new Item.Properties().group(unobtainableGroup)));
                        } else if (item.getItem().getClass() == new MinecartItem(null, new Item.Properties()).getClass()){
                            DuplicationInit.ITEMS.register(substring, () -> new MinecartItem(new CommandBlockMinecartEntity(null, 0, 0, 0).getMinecartType(), new Item.Properties().group(unobtainableGroup)));
                        } else {
                            DuplicationInit.ITEMS.register(substring, () -> new Item(new Item.Properties().group(unobtainableGroup)));
                        }

                        //DuplicationInit.ITEMS.register(substring, () -> new Item(new Item.Properties().group(unobtainableGroup)));
                    }

                    items.add(item);
                }
            }

            //ForgeRegistries.ITEMS.getValue(loc).getGroup();
            /*if (!test) {
                journey_mode.LOGGER.info("running reflection test");
                Field[] fields = ForgeRegistries.ITEMS.getValue(loc).asItem().getClass().getFields();
                for (int j = 0; j < fields.length; j++) {
                    journey_mode.LOGGER.info("[" + j + "] " + fields[j]);
                }
                test = true;
            }*/
            //journey_mode.LOGGER.info(substring + " has no tab? " + ForgeRegistries.ITEMS.getValue(loc).getGroup().getGroupName().getString());
        }
        /*for (int j = 0; j < ItemGroup.GROUPS.length; j++) {
            journey_mode.LOGGER.info("group is: " + )
        }*/

        return items;
        //journey_mode.SPECIAL_ITEM_GROUP.fill(items);
    }
}
