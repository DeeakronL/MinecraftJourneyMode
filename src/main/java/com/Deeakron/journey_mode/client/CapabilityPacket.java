package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.init.ResearchList;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.*;
import java.util.function.Supplier;

public class CapabilityPacket {
    private final boolean mode;
    private final ResearchList research;
    private final boolean godMode;
    private String player = null;

    public CapabilityPacket(EntityJourneyMode cap) {
        this.mode = cap.getJourneyMode();
        this.research = cap.getResearchList();
        this.godMode = cap.getGodMode();
        if (cap.getPlayer() != null) {
            this.player = cap.getPlayer().toString();
        }

    }

    public CapabilityPacket(Boolean journeyMode, ResearchList researchList, Boolean godMode, UUID player) {
        this.mode = journeyMode;
        this.research = researchList;
        this.godMode = godMode;
        this.player = player.toString();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(mode);
        buf.writeBoolean(godMode);
        if (player != null) {
            buf.writeBoolean(true);
            buf.writeUtf(player);
        } else {
            buf.writeBoolean(false);
        }
        String[] strings = this.research.getKeys();
        int[] counts = this.research.getCounts(strings);
        buf.writeInt(strings.length);
        for (int i = 0; i < strings.length; i++) {
            buf.writeUtf(strings[i]);
            buf.writeInt(counts[i]);
        }



    }

    public static CapabilityPacket decode(FriendlyByteBuf buf) {
        Boolean tempMode = buf.readBoolean();
        Boolean tempGodMode = buf.readBoolean();
        Boolean playerIsNotNull = buf.readBoolean();
        UUID tempPlayer = null;
        if (playerIsNotNull) {
            tempPlayer = UUID.fromString(buf.readUtf());

        }
        int size = buf.readInt();
        String[] strings = new String[size];
        int[] counts = new int[size];
        for (int i = 0; i < size; i++) {
            strings[i] = buf.readUtf();
            counts[i] = buf.readInt();
        }
        ResearchList tempResearch = new ResearchList(journey_mode.list.items, journey_mode.list.caps);
        tempResearch.updateCount(strings, counts, true, null, null);
        if (playerIsNotNull) {
            return new CapabilityPacket(tempMode, tempResearch, tempGodMode, tempPlayer);
        } else {
            return new CapabilityPacket(tempMode, tempResearch, tempGodMode, null);
        }

    }

    public static void handle(CapabilityPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {

            Entity entity = Minecraft.getInstance().level.getPlayerByUUID(UUID.fromString(message.player));
            EntityJourneyMode cap = entity.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            cap.setJourneyMode(message.mode);
            cap.setResearchList(message.research);
            cap.setGodMode(message.godMode);
            cap.setPlayer(UUID.fromString(message.player));
                }
                );
        context.get().setPacketHandled(true);
    }

}
