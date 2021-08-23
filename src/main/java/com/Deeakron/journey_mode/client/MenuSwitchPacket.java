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

public class MenuSwitchPacket {
    private final String data;
    private final String menuType;

    public MenuSwitchPacket(PacketBuffer buf) {
        this.data = buf.readString();
        this.menuType = buf.readString();
    }

    public MenuSwitchPacket(String data, String menuType) {
        this.data = data;
        this.menuType = menuType;
    }

    public void encode(PacketBuffer buf) {
        buf.writeString(data);
        buf.writeString(menuType);
    }

    public static MenuSwitchPacket decode(PacketBuffer buf) {
        return new MenuSwitchPacket(buf.readString(), buf.readString());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(data);
        ServerWorld serverWorld = context.get().getSender().getServerWorld();
        PlayerEntity player = (PlayerEntity) serverWorld.getEntityByUuid(info);
        if (menuType.equals("powers")) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new JourneyModePowersContainerProvider());
        } else if (menuType.equals("research")) {
            journey_mode.tempList = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getResearchList();
            NetworkHooks.openGui((ServerPlayerEntity) player, new JourneyModeResearchContainerProvider());
        }
    }
}
