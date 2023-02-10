package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CloseDuplicationMenuPacket {
    private String mode;
    private String player;

    public CloseDuplicationMenuPacket(FriendlyByteBuf buf) {
        this.mode = buf.readUtf();
        this.player = buf.readUtf();
    }

    public CloseDuplicationMenuPacket(String mode, String player) {
        this.mode = mode;
        this.player = player;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(mode);
        buf.writeUtf(player);
    }

    public static CloseDuplicationMenuPacket decode(FriendlyByteBuf buf) {
        return new CloseDuplicationMenuPacket(buf.readUtf(), buf.readUtf());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(player);
        ServerLevel serverLevel = context.get().getSender().getLevel();
        Player player = (Player) serverLevel.getEntity(info);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        if (mode.equals("creative")) {
            serverPlayer.setGameMode(GameType.SURVIVAL);
        } else if (mode.equals("godmode true")) {
            serverPlayer.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(true);
        } else if (mode.equals("godmode false")) {
            serverPlayer.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(false);
        }
    }
}
