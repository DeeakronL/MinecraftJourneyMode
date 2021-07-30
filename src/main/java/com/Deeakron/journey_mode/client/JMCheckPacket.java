package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class JMCheckPacket {
    private final String data;
    private boolean result = false;
    public JMCheckPacket(PacketBuffer buf) {
        this.data = buf.readString();
    }

    public JMCheckPacket(String data) {
        this.data = data;
    }

    public void encode(PacketBuffer buf){
        buf.writeString(data);
    }

    public static JMCheckPacket decode(PacketBuffer buf) {
        return new JMCheckPacket(buf.readString());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(data);
        ServerWorld serverWorld = context.get().getSender().getServerWorld();
        PlayerEntity player = (PlayerEntity) serverWorld.getEntityByUuid(info);
        context.get().setPacketHandled(true);
        this.result = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getJourneyMode();
        journey_mode.LOGGER.info("packet sent!");
    }

    public boolean getResult() {
        return this.result;
    }
}
