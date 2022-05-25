package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ResearchPacket {
    private final String item;
    private final int count;
    private final String player;
    private final ItemStack itemStack;

    public ResearchPacket(FriendlyByteBuf buf) {
        this.item= buf.readUtf();
        this.count = buf.readInt();
        this.player = buf.readUtf();
        this.itemStack = buf.readItem();
    }

    public ResearchPacket(String item, int count, String player, ItemStack itemObject) {
        this.item = item;
        this.count = count;
        this.player = player;
        this.itemStack = itemObject;
    }

    public void encode(PacketBuffer buf) {
        buf.writeUtf(item);
        buf.writeInt(count);
        buf.writeUtf(player);
        buf.writeItem(itemStack);
    }

    public static ResearchPacket decode(PacketBuffer buf) {
        return new ResearchPacket(buf.readUtf(), buf.readInt(), buf.readUtf(), buf.readItem());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(player);
        ServerWorld serverWorld = context.get().getSender().getLevel();
        PlayerEntity playerEntity = (PlayerEntity) serverWorld.getEntity(info);
        EntityJourneyMode cap = playerEntity.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
        cap.updateResearch(new String[]{item}, new int[]{count}, false, info, itemStack);
    }
}
