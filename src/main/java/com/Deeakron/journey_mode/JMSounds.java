package com.Deeakron.journey_mode;


import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, journey_mode.MODID);

    public static final RegistryObject<SoundEvent> RESEARCH_GRIND = SOUNDS.register("block.journey_mode.research_grinder.grind", () -> new SoundEvent(new ResourceLocation(journey_mode.MODID, "block.journey_mode.research_grinder.grind")));
}