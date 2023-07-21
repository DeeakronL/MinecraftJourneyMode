package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import com.Deeakron.journey_mode.init.UnobtainItemInit;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class AdvancementsProvider extends AdvancementProvider {
    public AdvancementsProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {

        //last 3 booleans are showToast, announceChat, and hidden

        Advancement root = Advancement.Builder.advancement().display(
                UnobtainItemInit.UNOBTAINIUM_BLOCK.get(),
                Component.translatable("advancement.journey_mode.root.title"),
                Component.translatable("advancement.journey_mode.root.description"),
                new ResourceLocation(journey_mode.MODID, "textures/gui/advancements/background/raw_unobtainium_block.png"),
                FrameType.TASK,
                true,true,false)
                .addCriterion("unobtainium", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.RAW_UNOBTAINIUM.get()))
                .save(consumer, "journey_mode:root");


        Advancement starforge  = Advancement.Builder.advancement().parent(root).display(
                UnobtainItemInit.UNOBTAINIUM_STARFORGE.get(),
                Component.translatable("advancement.journey_mode.get_starforge.title"),
                Component.translatable("advancement.journey_mode.get_starforge.description"),
                null,
                FrameType.TASK,
                true,true,false)
                .addCriterion("starforge", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.UNOBTAINIUM_STARFORGE.get()))
                .save(consumer, "journey_mode:get_starforge");

        Advancement primordial_dust  = Advancement.Builder.advancement().parent(starforge).display(
                        UnobtainItemInit.PRIMORDIAL_VOID_DUST.get(),
                        Component.translatable("advancement.journey_mode.get_primordial_dust.title"),
                        Component.translatable("advancement.journey_mode.get_primordial_dust.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("primordial_dust", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.PRIMORDIAL_VOID_DUST.get()))
                .save(consumer, "journey_mode:get_primordial_dust");

        Advancement turtle_spawn_egg  = Advancement.Builder.advancement().parent(starforge).display(
                Items.TURTLE_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_turtle_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_turtle_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TURTLE_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_turtle_spawn_egg");

        Advancement chorus_plant  = Advancement.Builder.advancement().parent(starforge).display(
                        Items.CHORUS_PLANT.asItem(),
                        Component.translatable("advancement.journey_mode.get_chorus_plant.title"),
                        Component.translatable("advancement.journey_mode.get_chorus_plant.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("chorus_plant", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHORUS_PLANT.asItem()))
                .save(consumer, "journey_mode:get_chorus_plant");

        Advancement crack_bedrock  = Advancement.Builder.advancement().parent(primordial_dust).display(
                        UnobtainItemInit.CRACKED_BEDROCK.get(),
                        Component.translatable("advancement.journey_mode.crack_bedrock.title"),
                        Component.translatable("advancement.journey_mode.crack_bedrock.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("cracked_bedrock", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(UnobtainBlockInit.CRACKED_BEDROCK.get()).build()), ItemPredicate.Builder.item().of(UnobtainItemInit.PRIMORDIAL_VOID_DUST.get())))
                .save(consumer, "journey_mode:crack_bedrock");

        Advancement unobtainium  = Advancement.Builder.advancement().parent(crack_bedrock).display(
                        UnobtainItemInit.UNOBTAINIUM_INGOT.get(),
                        Component.translatable("advancement.journey_mode.get_unobtainium.title"),
                        Component.translatable("advancement.journey_mode.get_unobtainium.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("unobtainium", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.UNOBTAINIUM_INGOT.get()))
                .save(consumer, "journey_mode:get_unobtainium");

        Advancement antikythera  = Advancement.Builder.advancement().parent(unobtainium).display(
                        UnobtainItemInit.UNOBTAINIUM_ANTIKYTHERA.get(),
                        Component.translatable("advancement.journey_mode.get_antikythera.title"),
                        Component.translatable("advancement.journey_mode.get_antikythera.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("antikythera", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.UNOBTAINIUM_ANTIKYTHERA.get()))
                .save(consumer, "journey_mode:get_antikythera");

        Advancement aetherial_dust  = Advancement.Builder.advancement().parent(antikythera).display(
                        UnobtainItemInit.AETHERIAL_VOID_DUST.get(),
                        Component.translatable("advancement.journey_mode.get_aetherial_dust.title"),
                        Component.translatable("advancement.journey_mode.get_aetherial_dust.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("aetherial_dust", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.AETHERIAL_VOID_DUST.get()))
                .save(consumer, "journey_mode:get_aetherial_dust");

        Advancement paint  = Advancement.Builder.advancement().parent(antikythera).display(
                        UnobtainItemInit.PAINT_BUCKET.get(),
                        Component.translatable("advancement.journey_mode.get_paint.title"),
                        Component.translatable("advancement.journey_mode.get_paint.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("paint", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.PAINT_BUCKET.get()))
                .save(consumer, "journey_mode:get_paint");

        Advancement scanner  = Advancement.Builder.advancement().parent(antikythera).display(
                        UnobtainItemInit.SCANNER.get(),
                        Component.translatable("advancement.journey_mode.get_scanner.title"),
                        Component.translatable("advancement.journey_mode.get_scanner.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("scanner", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.SCANNER.get()))
                .save(consumer, "journey_mode:get_scanner");

        Advancement inert_structure  = Advancement.Builder.advancement().parent(aetherial_dust).display(
                        UnobtainItemInit.INERT_STRUCTURE_BLOCK.get(),
                        Component.translatable("advancement.journey_mode.get_inert_structure.title"),
                        Component.translatable("advancement.journey_mode.get_inert_structure.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("inert_structure", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(UnobtainBlockInit.INERT_STRUCTURE_BLOCK.get()).build()), ItemPredicate.Builder.item().of(UnobtainItemInit.AETHERIAL_VOID_DUST.get())))
                .save(consumer, "journey_mode:get_inert_structure");

        Advancement inert_command  = Advancement.Builder.advancement().parent(aetherial_dust).display(
                        UnobtainItemInit.INERT_COMMAND_BLOCK.get(),
                        Component.translatable("advancement.journey_mode.get_inert_command.title"),
                        Component.translatable("advancement.journey_mode.get_inert_command.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .requirements(RequirementsStrategy.OR)
                .addCriterion("inert_command", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(UnobtainBlockInit.INERT_COMMAND_BLOCK.get()).build()), ItemPredicate.Builder.item().of(UnobtainItemInit.AETHERIAL_VOID_DUST.get())))
                .addCriterion("inert_chain", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(UnobtainBlockInit.INERT_CHAIN_COMMAND_BLOCK.get()).build()), ItemPredicate.Builder.item().of(UnobtainItemInit.AETHERIAL_VOID_DUST.get())))
                .addCriterion("inert_repeating", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(UnobtainBlockInit.INERT_REPEATING_COMMAND_BLOCK.get()).build()), ItemPredicate.Builder.item().of(UnobtainItemInit.AETHERIAL_VOID_DUST.get())))
                .save(consumer, "journey_mode:get_inert_command");

        Advancement inert_jigsaw  = Advancement.Builder.advancement().parent(aetherial_dust).display(
                        UnobtainItemInit.INERT_JIGSAW_BLOCK.get(),
                        Component.translatable("advancement.journey_mode.get_inert_jigsaw.title"),
                        Component.translatable("advancement.journey_mode.get_inert_jigsaw.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("inert_jigsaw", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(UnobtainBlockInit.INERT_JIGSAW_BLOCK.get()).build()), ItemPredicate.Builder.item().of(UnobtainItemInit.AETHERIAL_VOID_DUST.get())))
                .save(consumer, "journey_mode:get_inert_jigsaw");

        Advancement painted_barrier  = Advancement.Builder.advancement().parent(paint).display(
                        UnobtainItemInit.PAINTED_BARRIER.get(),
                        Component.translatable("advancement.journey_mode.get_painted_barrier.title"),
                        Component.translatable("advancement.journey_mode.get_painted_barrier.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("painted_barrier", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(UnobtainBlockInit.PAINTED_BARRIER.get()).build()), ItemPredicate.Builder.item().of(UnobtainItemInit.PAINT_BUCKET.get())))
                .save(consumer, "journey_mode:get_painted_barrier");

        Advancement villager_spawn_egg  = Advancement.Builder.advancement().parent(scanner).display(
                        Items.VILLAGER_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_villager_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_villager_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.VILLAGER_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_villager_spawn_egg");

        Advancement spawn_egg  = Advancement.Builder.advancement().parent(scanner).display(
                        Items.ZOMBIE_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ZOMBIE_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_spawn_egg");

        Advancement parrot_spawn_egg  = Advancement.Builder.advancement().parent(scanner).display(
                        Items.PARROT_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_parrot_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_parrot_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PARROT_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_parrot_spawn_egg");

        Advancement dolphin_spawn_egg  = Advancement.Builder.advancement().parent(scanner).display(
                        Items.DOLPHIN_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_dolphin_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_dolphin_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DOLPHIN_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_dolphin_spawn_egg");

        Advancement breedable_spawn_egg  = Advancement.Builder.advancement().parent(scanner).display(
                        Items.COW_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_breedable_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_breedable_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .requirements(RequirementsStrategy.OR)
                .addCriterion("axolotl_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AXOLOTL_SPAWN_EGG.asItem()))
                .addCriterion("cat_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CAT_SPAWN_EGG.asItem()))
                .addCriterion("chicken_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHICKEN_SPAWN_EGG.asItem()))
                .addCriterion("cow_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COW_SPAWN_EGG.asItem()))
                .addCriterion("donkey_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DONKEY_SPAWN_EGG.asItem()))
                .addCriterion("fox_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FOX_SPAWN_EGG.asItem()))
                .addCriterion("horse_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HORSE_SPAWN_EGG.asItem()))
                .addCriterion("mooshroom_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MOOSHROOM_SPAWN_EGG.asItem()))
                .addCriterion("mule_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MULE_SPAWN_EGG.asItem()))
                .addCriterion("ocelot_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.OCELOT_SPAWN_EGG.asItem()))
                .addCriterion("pig_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PIG_SPAWN_EGG.asItem()))
                .addCriterion("rabbit_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RABBIT_SPAWN_EGG.asItem()))
                .addCriterion("sheep_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SHEEP_SPAWN_EGG.asItem()))
                .addCriterion("strider_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STRIDER_SPAWN_EGG.asItem()))
                .addCriterion("bee_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BEE_SPAWN_EGG.asItem()))
                .addCriterion("goat_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOAT_SPAWN_EGG.asItem()))
                .addCriterion("llama_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LLAMA_SPAWN_EGG.asItem()))
                .addCriterion("panda_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PANDA_SPAWN_EGG.asItem()))
                .addCriterion("trader_llama_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TRADER_LLAMA_SPAWN_EGG.asItem()))
                .addCriterion("wolf_spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WOLF_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_breedable_spawn_egg");

        Advancement wandering_trader_spawn_egg  = Advancement.Builder.advancement().parent(villager_spawn_egg).display(
                        Items.WANDERING_TRADER_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_wandering_trader_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_wandering_trader_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WANDERING_TRADER_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_wandering_trader_spawn_egg");

        Advancement zombie_horse_spawn_egg  = Advancement.Builder.advancement().parent(breedable_spawn_egg).display(
                        Items.ZOMBIE_HORSE_SPAWN_EGG.asItem(),
                        Component.translatable("advancement.journey_mode.get_zombie_horse_spawn_egg.title"),
                        Component.translatable("advancement.journey_mode.get_zombie_horse_spawn_egg.description"),
                        null,
                        FrameType.TASK,
                        true,true,false)
                .addCriterion("spawn_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ZOMBIE_HORSE_SPAWN_EGG.asItem()))
                .save(consumer, "journey_mode:get_zombie_horse_spawn_egg");

        Advancement light  = Advancement.Builder.advancement().parent(aetherial_dust).display(
                Items.LIGHT.asItem(),
                Component.translatable("advancement.journey_mode.get_light.title"),
                Component.translatable("advancement.journey_mode.get_light.description"),
                null,
                FrameType.TASK,
                true,true,false)
                .addCriterion("light", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LIGHT.asItem()))
                .save(consumer, "journey_mode:get_light");

        Advancement broken_light  = Advancement.Builder.advancement().parent(light).display(
                UnobtainItemInit.BROKEN_LIGHT.get(),
                Component.translatable("advancement.journey_mode.get_broken_light.title"),
                Component.translatable("advancement.journey_mode.get_broken_light.description"),
                null,
                FrameType.TASK,
                true,true,false)
                .addCriterion("broken_light", InventoryChangeTrigger.TriggerInstance.hasItems(UnobtainItemInit.BROKEN_LIGHT.get()))
                .save(consumer, "journey_mode:get_broken_light");
    }
}
