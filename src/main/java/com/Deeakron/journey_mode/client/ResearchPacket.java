package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.JourneyModePowersContainerProvider;
import com.Deeakron.journey_mode.JourneyModeResearchContainerProvider;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class ResearchPacket {
    private final String item;
    private final int count;
    private final String player;

    public ResearchPacket(PacketBuffer buf) {
        this.item= buf.readString();
        this.count = buf.readInt();
        this.player = buf.readString();
    }

    public ResearchPacket(String item, int count, String player) {
        this.item = item;
        this.count = count;
        this.player = player;
    }

    public void encode(PacketBuffer buf) {
        buf.writeString(item);
        buf.writeInt(count);
        buf.writeString(player);
    }

    public static ResearchPacket decode(PacketBuffer buf) {
        return new ResearchPacket(buf.readString(), buf.readInt(), buf.readString());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(player);
        ServerWorld serverWorld = context.get().getSender().getServerWorld();
        PlayerEntity playerEntity = (PlayerEntity) serverWorld.getEntityByUuid(info);
        EntityJourneyMode cap = playerEntity.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
        cap.updateResearch(new String[]{item}, new int[]{count}, false, info);
    }
}
