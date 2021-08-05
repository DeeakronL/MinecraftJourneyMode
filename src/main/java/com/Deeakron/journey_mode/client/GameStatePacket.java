package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.gui.JourneyModePowersScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class GameStatePacket {
    private final String data;
    private Boolean freeze;
    private int tickSpeed;
    private Boolean mobSpawn;
    private Boolean mobGrief;
    private Boolean godMode;
    private Boolean keepInv;

    public GameStatePacket(PacketBuffer buf) {
        this.data = buf.readString();
        this.freeze = buf.readBoolean();
        this.tickSpeed = buf.readInt();
        this.mobSpawn = buf.readBoolean();
        this.mobGrief = buf.readBoolean();
        this.godMode = buf.readBoolean();
        this.keepInv = buf.readBoolean();
    }

    public GameStatePacket(String data, Boolean freeze, int tickSpeed, Boolean mobSpawn, Boolean mobGrief, Boolean godMode, Boolean keepInv) {
        this.data = data;
        this.freeze = freeze;
        this.tickSpeed = tickSpeed;
        this.mobSpawn = mobSpawn;
        this.mobGrief = mobGrief;
        this.godMode = godMode;
        this.keepInv = keepInv;
    }

    public void encode(PacketBuffer buf) {
        buf.writeString(this.data);
        buf.writeBoolean(this.freeze);
        buf.writeInt(this.tickSpeed);
        buf.writeBoolean(this.mobSpawn);
        buf.writeBoolean(this.mobGrief);
        buf.writeBoolean(this.godMode);
        buf.writeBoolean(this.keepInv);
    }

    public static GameStatePacket decode(PacketBuffer buf) {
        return new GameStatePacket(buf.readString(), buf.readBoolean(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(data);
        ServerWorld serverWorld = context.get().getSender().getServerWorld();
        PlayerEntity player = (PlayerEntity) serverWorld.getEntityByUuid(info);
        this.freeze = !serverWorld.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);
        this.tickSpeed = serverWorld.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED)/3;
        this.mobSpawn = !serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING);
        this.mobGrief = !serverWorld.getGameRules().getBoolean(GameRules.MOB_GRIEFING);
        this.godMode = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getGodMode();
        this.keepInv = serverWorld.getGameRules().getBoolean(GameRules.KEEP_INVENTORY);
        context.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleOnClient(this)));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    /*public static DistExecutor.SafeRunnable handleOnClient(final GameStatePacket msg) {
        Boolean freeze = msg.getFreeze();
        int tickSpeed = msg.getTickSpeed();
        Boolean mobSpawn = msg.getMobSpawn();
        Boolean mobGrief = msg.getMobGrief();
        Boolean godMode = msg.getGodMode();
        Boolean keepInv = msg.getKeepInv();
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                int window = Minecraft.getInstance().player.openContainer.windowId;
                ITextComponent title = new StringTextComponent("Journey Mode Menu");
                Minecraft.getInstance().displayGuiScreen(new JourneyModePowersScreen(Minecraft.getInstance().player.inventory, title, window, freeze, tickSpeed, mobSpawn, mobGrief, godMode, keepInv));
            }
        };
    }*/
    public void handleOnClient(final GameStatePacket msg) {
        Boolean freeze = msg.getFreeze();
        int tickSpeed = msg.getTickSpeed();
        Boolean mobSpawn = msg.getMobSpawn();
        Boolean mobGrief = msg.getMobGrief();
        Boolean godMode = msg.getGodMode();
        Boolean keepInv = msg.getKeepInv();
        int window = Minecraft.getInstance().player.openContainer.windowId;
        ITextComponent title = new StringTextComponent("Journey Mode Menu");
        if (Minecraft.getInstance().world.isRemote()) {
            Minecraft.getInstance().displayGuiScreen(new JourneyModePowersScreen(Minecraft.getInstance().player.inventory, title, window, freeze, tickSpeed, mobSpawn, mobGrief, godMode, keepInv));
        }
    }

    public Boolean getFreeze() {
        return this.freeze;
    }

    public int getTickSpeed() {
        return this.tickSpeed;
    }

    public Boolean getMobSpawn() {
        return this.mobSpawn;
    }

    public Boolean getMobGrief() {
        return mobGrief;
    }

    public Boolean getGodMode() {
        return godMode;
    }

    public Boolean getKeepInv() {
        return keepInv;
    }
}
