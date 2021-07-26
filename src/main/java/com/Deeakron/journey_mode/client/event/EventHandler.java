package com.Deeakron.journey_mode.client.event;

import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = journey_mode.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    private static final String PROTOCOL_VERSION = "1";
    public World world;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("journey_mode", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof  ServerPlayerEntity){
            JMCapabilityProvider provider = new JMCapabilityProvider();
            event.addCapability(new ResourceLocation(journey_mode.MODID, "JM"), provider);
            event.addListener(provider::invalidate);
        }
    }

}