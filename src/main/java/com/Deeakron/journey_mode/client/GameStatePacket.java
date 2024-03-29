package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.container.JourneyModePowersContainerProvider;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.event.MenuOpenEvent;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

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

    public GameStatePacket(FriendlyByteBuf buf) {
        this.data = buf.readUtf();
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

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.data);
        buf.writeBoolean(this.freeze);
        buf.writeInt(this.tickSpeed);
        buf.writeBoolean(this.mobSpawn);
        buf.writeBoolean(this.mobGrief);
        buf.writeBoolean(this.godMode);
        buf.writeBoolean(this.keepInv);
    }

    public static GameStatePacket decode(FriendlyByteBuf buf) {
        return new GameStatePacket(buf.readUtf(), buf.readBoolean(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(data);
        ServerLevel ServerLevel = context.get().getSender().getLevel();
        Player player = (Player) ServerLevel.getEntity(info);
        this.freeze = !ServerLevel.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
        this.tickSpeed = ServerLevel.getGameRules().getInt(GameRules.RULE_RANDOMTICKING)/3;
        this.mobSpawn = !ServerLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
        this.mobGrief = !ServerLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        this.godMode = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getGodMode();
        this.keepInv = ServerLevel.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        journey_mode.freeze = this.freeze;
        journey_mode.tickSpeed = this.tickSpeed;
        journey_mode.mobSpawn = this.mobSpawn;
        journey_mode.mobGrief = this.mobGrief;
        journey_mode.godMode = this.godMode;
        journey_mode.keepInv = this.keepInv;
        boolean unlockRecipes = false;
        if (journey_mode.useUnobtain) {
            Advancement advancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation("journey_mode:get_antikythera"));
            ServerPlayer serverPlayer = (ServerPlayer) player;

            if (serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                unlockRecipes = true;
            }
        }
        journey_mode.hasRecipes = unlockRecipes;
        NetworkHooks.openGui((ServerPlayer) player, new JourneyModePowersContainerProvider());
        context.get().setPacketHandled(true);
    }

    public static DistExecutor.SafeRunnable handleOnClient(final GameStatePacket msg) {
        Boolean freeze = msg.getFreeze();
        int tickSpeed = msg.getTickSpeed();
        Boolean mobSpawn = msg.getMobSpawn();
        Boolean mobGrief = msg.getMobGrief();
        Boolean godMode = msg.getGodMode();
        Boolean keepInv = msg.getKeepInv();
        int window = Minecraft.getInstance().player.containerMenu.containerId;
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                MinecraftForge.EVENT_BUS.post(new MenuOpenEvent(freeze, tickSpeed, mobSpawn, mobGrief, godMode, keepInv, UUID.fromString(msg.data)));
            }
        };
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
