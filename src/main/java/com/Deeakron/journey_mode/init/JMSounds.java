package com.Deeakron.journey_mode.init;


import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, journey_mode.MODID);

    public static final RegistryObject<SoundEvent> RESEARCH_GRIND = SOUNDS.register("block.journey_mode.research_grinder.grind", () -> new SoundEvent(new ResourceLocation(journey_mode.MODID, "block.journey_mode.research_grinder.grind")));

    public static final RegistryObject<SoundEvent> ANTIKYTHERA_CRAFT = SOUNDS.register("block.journey_mode.unobtainium_antikythera.craft", () -> new SoundEvent(new ResourceLocation(journey_mode.MODID, "block.journey_mode.unobtainium_antikythera.craft")));

    public static final RegistryObject<SoundEvent> AETHERIAL_DEACTIVATE = SOUNDS.register("item.journey_mode.aetherial_void_dust.deactivate", () -> new SoundEvent(new ResourceLocation(journey_mode.MODID, "item.journey_mode.aetherial_void_dust.deactivate")));

    public static final RegistryObject<SoundEvent> BEDROCK_CRACK = SOUNDS.register("item.journey_mode.primordial_void_dust.crack", () -> new SoundEvent(new ResourceLocation(journey_mode.MODID, "item.journey_mode.primordial_void_dust.crack")));
}
