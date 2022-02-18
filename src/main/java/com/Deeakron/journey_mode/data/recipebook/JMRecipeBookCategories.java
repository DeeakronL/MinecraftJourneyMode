package com.Deeakron.journey_mode.data.recipebook;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public enum JMRecipeBookCategories {
    ANTIKYTHERA_SEARCH(new ItemStack(Items.COMPASS)),
    ANTIKYTHERA_BUILDING_BLOCKS(new ItemStack(Blocks.BRICKS)),
    ANTIKYTHERA_REDSTONE(new ItemStack(Items.REDSTONE)),
    ANTIKYTHERA_EQUIPMENT(new ItemStack(Items.IRON_AXE), new ItemStack(Items.GOLDEN_SWORD)),
    ANTIKYTHERA_MISC(new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.APPLE)),
    STARFORGE_SEARCH(new ItemStack(Items.COMPASS)),
    STARFORGE_FOOD(new ItemStack(Items.PORKCHOP)),
    STARFORGE_BLOCKS(new ItemStack(Blocks.STONE)),
    STARFORGE_MISC(new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.EMERALD)),
    UNKNOWN(new ItemStack(Items.BARRIER));


    public static final List<JMRecipeBookCategories> starforge = ImmutableList.of(STARFORGE_SEARCH, STARFORGE_FOOD, STARFORGE_BLOCKS, STARFORGE_MISC);
    public static final List<JMRecipeBookCategories> antikythera = ImmutableList.of(ANTIKYTHERA_SEARCH, ANTIKYTHERA_EQUIPMENT, ANTIKYTHERA_BUILDING_BLOCKS, ANTIKYTHERA_MISC, ANTIKYTHERA_REDSTONE);
    public static final Map<JMRecipeBookCategories, List<JMRecipeBookCategories>> field_243235_w = ImmutableMap.of(ANTIKYTHERA_SEARCH, ImmutableList.of(ANTIKYTHERA_EQUIPMENT, ANTIKYTHERA_BUILDING_BLOCKS, ANTIKYTHERA_MISC, ANTIKYTHERA_REDSTONE), STARFORGE_SEARCH, ImmutableList.of(STARFORGE_FOOD, STARFORGE_BLOCKS, STARFORGE_MISC));
    private final List<ItemStack> icons;

    private JMRecipeBookCategories(ItemStack... itemStack) {
        this.icons = ImmutableList.copyOf(itemStack);
    }

    public static List<JMRecipeBookCategories> func_243236_a(JMRecipeBookCategory category) {
        switch (category) {
            case ANTIKYTHERA:
                return antikythera;
            case STARFORGE:
                return starforge;
            default:
                return ImmutableList.of();
        }
    }

    public List<ItemStack> getIcons() {
        return this.icons;
    }
}
