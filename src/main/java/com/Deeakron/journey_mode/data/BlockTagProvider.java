package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.BlockInit;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    protected void addTags() {

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(BlockInit.WOODEN_RESEARCH_GRINDER.get(),BlockInit.WOODEN_RESEARCH_GRINDER_PART_0.get(),BlockInit.WOODEN_RESEARCH_GRINDER_PART_1.get(),BlockInit.WOODEN_RESEARCH_GRINDER_PART_2.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockInit.IRON_RESEARCH_GRINDER.get(),BlockInit.IRON_RESEARCH_GRINDER_PART_0.get(),BlockInit.IRON_RESEARCH_GRINDER_PART_1.get(),BlockInit.IRON_RESEARCH_GRINDER_PART_2.get(),BlockInit.DIAMOND_RESEARCH_GRINDER.get(),
                        BlockInit.DIAMOND_RESEARCH_GRINDER_PART_0.get(),BlockInit.DIAMOND_RESEARCH_GRINDER_PART_1.get(),BlockInit.DIAMOND_RESEARCH_GRINDER_PART_2.get());
        if (journey_mode.useUnobtain) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(UnobtainBlockInit.PAINTED_BARRIER.get(),UnobtainBlockInit.UNOBTAINIUM_BLOCK.get(),
                        UnobtainBlockInit.INERT_COMMAND_BLOCK.get(),UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get(),UnobtainBlockInit.CRACKED_BEDROCK.get(),UnobtainBlockInit.INERT_JIGSAW_BLOCK.get(),UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get(),
                        UnobtainBlockInit.INERT_STRUCTURE_BLOCK.get(),UnobtainBlockInit.RAW_UNOBTAINIUM_BLOCK.get(),UnobtainBlockInit.UNOBTAINIUM_ANTIKYTHERA.get(),UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get(),UnobtainBlockInit.BROKEN_LIGHT.get());

            tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                    .add(UnobtainBlockInit.PAINTED_BARRIER.get(),UnobtainBlockInit.UNOBTAINIUM_BLOCK.get(),UnobtainBlockInit.INERT_COMMAND_BLOCK.get(),UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get(),
                            UnobtainBlockInit.CRACKED_BEDROCK.get(),UnobtainBlockInit.INERT_JIGSAW_BLOCK.get(),UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get(),UnobtainBlockInit.INERT_STRUCTURE_BLOCK.get(),
                            UnobtainBlockInit.RAW_UNOBTAINIUM_BLOCK.get(),UnobtainBlockInit.UNOBTAINIUM_ANTIKYTHERA.get(),UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get());
        }

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockInit.IRON_RESEARCH_GRINDER.get(),BlockInit.IRON_RESEARCH_GRINDER_PART_0.get(),BlockInit.IRON_RESEARCH_GRINDER_PART_1.get(),BlockInit.IRON_RESEARCH_GRINDER_PART_2.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(BlockInit.DIAMOND_RESEARCH_GRINDER.get(),BlockInit.DIAMOND_RESEARCH_GRINDER_PART_0.get(),BlockInit.DIAMOND_RESEARCH_GRINDER_PART_1.get(),BlockInit.DIAMOND_RESEARCH_GRINDER_PART_2.get());


    }
}
