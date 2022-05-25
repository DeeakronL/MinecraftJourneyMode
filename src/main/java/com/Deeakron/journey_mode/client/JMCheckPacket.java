package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class JMCheckPacket {
    private final String data;
    private boolean result = false;
    public JMCheckPacket(FriendlyByteBuf buf) {
        this.data = buf.readUtf();
        this.result = buf.readBoolean();
    }

    public JMCheckPacket(String data, Boolean result) {
        this.data = data;
        this.result = result;
    }

    public void encode(PacketBuffer buf){
        buf.writeUtf(data);
        buf.writeBoolean(result);
    }

    public static JMCheckPacket decode(PacketBuffer buf) {
        return new JMCheckPacket(buf.readUtf(), buf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(data);
        ServerWorld serverWorld = context.get().getSender().getLevel();
        PlayerEntity player = (PlayerEntity) serverWorld.getEntity(info);
        this.result = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getJourneyMode();
        context.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleOnClient(this)));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleOnClient(final JMCheckPacket msg) {
        Boolean result = msg.getResult();
        if (result) {
            int window = Minecraft.getInstance().player.containerMenu.containerId;
            ITextComponent title = new StringTextComponent("Journey Mode Menu");
            EventHandler.INSTANCE.sendToServer(new GameStatePacket(this.data, false, 1, false, false, false, false));
        }
    }

    public boolean getResult() {
        return this.result;
    }
}
