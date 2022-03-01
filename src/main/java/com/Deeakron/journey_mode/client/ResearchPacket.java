package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ResearchPacket {
    private final String item;
    private final int count;
    private final String player;
    private final ItemStack itemStack;

    public ResearchPacket(PacketBuffer buf) {
        this.item= buf.readString();
        this.count = buf.readInt();
        this.player = buf.readString();
        this.itemStack = buf.readItemStack();
    }

    public ResearchPacket(String item, int count, String player, ItemStack itemObject) {
        this.item = item;
        this.count = count;
        this.player = player;
        this.itemStack = itemObject;
    }

    public void encode(PacketBuffer buf) {
        buf.writeString(item);
        buf.writeInt(count);
        buf.writeString(player);
        buf.writeItemStack(itemStack);
    }

    public static ResearchPacket decode(PacketBuffer buf) {
        return new ResearchPacket(buf.readString(), buf.readInt(), buf.readString(), buf.readItemStack());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(player);
        ServerWorld serverWorld = context.get().getSender().getServerWorld();
        PlayerEntity playerEntity = (PlayerEntity) serverWorld.getEntityByUuid(info);
        EntityJourneyMode cap = playerEntity.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
        cap.updateResearch(new String[]{item}, new int[]{count}, false, info, itemStack);
    }
}
