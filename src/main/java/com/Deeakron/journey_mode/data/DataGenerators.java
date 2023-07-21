package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import static com.Deeakron.journey_mode.journey_mode.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(true, new BlockTagProvider(generator, journey_mode.MODID, helper));
        generator.addProvider(true, new AdvancementsProvider(generator, helper));
        journey_mode.register(event);
        generator.addProvider(true, new RecipesProvider(generator));
    }
}
