package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.data.BrokenLightParticle;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.Deeakron.journey_mode.journey_mode.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JMParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, journey_mode.MODID);

    public static final RegistryObject<SimpleParticleType> BROKEN_LIGHT = PARTICLE_TYPES.register("broken_light", () -> new SimpleParticleType(false));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        if (journey_mode.useUnobtain) {
            ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
            particleEngine.register(JMParticleTypes.BROKEN_LIGHT.get(), new BrokenLightParticle.BrokenLightProvider());
        }

    }
}
