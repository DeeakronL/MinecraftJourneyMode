package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.event.EventHandler;
import com.Deeakron.journey_mode.init.ResearchList;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.*;
import java.util.function.Supplier;

public class CapabilityPacket {
    private final boolean mode;
    private final ResearchList research;
    private final boolean godMode;
    private UUID player = null;

    public CapabilityPacket(EntityJourneyMode cap) {
        this.mode = cap.getJourneyMode();
        this.research = cap.getResearchList();
        this.godMode = cap.getGodMode();
        if (cap.getPlayer() != null) {
            this.player = cap.getPlayer();
        }

    }

    public CapabilityPacket(Boolean journeyMode, ResearchList researchList, Boolean godMode, UUID player) {
        this.mode = journeyMode;
        this.research = researchList;
        this.godMode = godMode;
        this.player = player;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(mode);
        String[] strings = this.research.getKeys();
        int[] counts = this.research.getCounts(strings);
        buf.writeInt(strings.length);
        for (int i = 0; i < strings.length; i++) {
            buf.writeUtf(strings[i]);
            buf.writeInt(counts[i]);
        }
        buf.writeBoolean(godMode);
        if (player != null) {
            buf.writeBoolean(true);
            buf.writeUUID(player);
        } else {
            buf.writeBoolean(false);
        }

    }

    public static CapabilityPacket decode(FriendlyByteBuf buf) {
        Boolean tempMode = buf.readBoolean();
        int size = buf.readInt();
        String[] strings = new String[size];
        int[] counts = new int[size];
        for (int i = 0; i < size; i++) {
            strings[i] = buf.readUtf();
            counts[i] = buf.readInt();
        }
        /*Map research = buf.readMap(FriendlyByteBuf::readUtf,FriendlyByteBuf::readVarIntArray);
        List<String> stringsList = new ArrayList<String>(research.keySet());
        List<Integer> capsList = new ArrayList<Integer>();
        String[] strings = stringsList.toArray(String[]::new);
        Integer[] capsI = capsList.toArray(Integer[]::new);
        //String[] strings = (String[]) research.keySet().toArray();
        //Object[] capsI = research.values().toArray();
        int[] caps = new int[capsI.length];
        for (int i = 0; i < strings.length; i++){
            journey_mode.LOGGER.info("int is " + research.get(strings[i])[0]);
            capsList.add((Integer) research.get(strings[i]));
            //caps[i] = Integer.parseInt(capsI[i].toString());
        }*/
        ResearchList tempResearch = journey_mode.tempList;
        tempResearch.updateCount(strings, counts, true, null, null);
        Boolean tempGodMode = buf.readBoolean();
        Boolean playerIsNotNull = buf.readBoolean();
        if (playerIsNotNull) {
            UUID tempPlayer = buf.readUUID();
            return new CapabilityPacket(tempMode, tempResearch, tempGodMode, tempPlayer);
        } else {
            return new CapabilityPacket(tempMode, tempResearch, tempGodMode, null);
        }
    }

    public static void handle(CapabilityPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getPlayerByUUID(message.player);
            EntityJourneyMode cap = entity.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            cap.setJourneyMode(message.mode);
            cap.setResearchList(message.research);
            cap.setGodMode(message.godMode);
            cap.setPlayer(message.player);
                }
                );
        context.get().setPacketHandled(true);
    }

}
