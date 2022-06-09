package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.client.gui.JourneyModeDuplicationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class DuplicationMenuPacket {
    private final boolean wasCreative;
    private final boolean wasGodMode;
    private final String player;

    public DuplicationMenuPacket(FriendlyByteBuf buf) {
        this.wasCreative = buf.readBoolean();
        this.wasGodMode = buf.readBoolean();
        this.player = buf.readUtf();
    }

    public DuplicationMenuPacket(boolean wasCreative, boolean wasGodMode, String player) {
        this.wasCreative = wasCreative;
        this.wasGodMode = wasGodMode;
        this.player = player;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(wasCreative);
        buf.writeBoolean(wasGodMode);
        buf.writeUtf(player);
    }

    public static DuplicationMenuPacket decode(FriendlyByteBuf buf) {
        return new DuplicationMenuPacket(buf.readBoolean(), buf.readBoolean(), buf.readUtf());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(player);
        Player playerEntity = Minecraft.getInstance().player;
        context.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleOnClient(this, wasCreative, wasGodMode, playerEntity)));
    }

    public void handleOnClient(final DuplicationMenuPacket msg, boolean wasCreative, boolean wasGodMode, Player player) {

        Minecraft.getInstance().setScreen(new JourneyModeDuplicationScreen(Minecraft.getInstance().player, wasCreative, wasGodMode));
    }
}
