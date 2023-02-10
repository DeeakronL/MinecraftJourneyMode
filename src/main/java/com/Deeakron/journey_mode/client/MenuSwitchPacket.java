package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.client.event.EventHandler;
import com.Deeakron.journey_mode.container.JourneyModePowersContainerProvider;
import com.Deeakron.journey_mode.container.JourneyModeRecipesContainerProvider;
import com.Deeakron.journey_mode.container.JourneyModeResearchContainerProvider;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class MenuSwitchPacket {
    private final String data;
    private final String menuType;

    public MenuSwitchPacket(FriendlyByteBuf buf) {
        this.data = buf.readUtf();
        this.menuType = buf.readUtf();
    }

    public MenuSwitchPacket(String data, String menuType) {
        this.data = data;
        this.menuType = menuType;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(data);
        buf.writeUtf(menuType);
    }

    public static MenuSwitchPacket decode(FriendlyByteBuf buf) {
        return new MenuSwitchPacket(buf.readUtf(), buf.readUtf());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        UUID info = UUID.fromString(data);
        ServerLevel serverLevel = context.get().getSender().getLevel();
        Player player = (Player) serverLevel.getEntity(info);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        boolean unlockRecipes = false;
        if (journey_mode.useUnobtain) {
            Advancement advancement = player.getServer().getAdvancements().getAdvancement(new ResourceLocation("journey_mode:get_antikythera"));


            if (serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                unlockRecipes = true;
            }
        }
        if (menuType.equals("powers")) {
            journey_mode.hasRecipes = unlockRecipes;
            NetworkHooks.openGui((ServerPlayer) player, new JourneyModePowersContainerProvider());
        } else if (menuType.equals("research")) {
            journey_mode.hasRecipes = unlockRecipes;
            journey_mode.tempList = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getResearchList();
            NetworkHooks.openGui((ServerPlayer) player, new JourneyModeResearchContainerProvider());
        } else if (menuType.equals("duplication")) {
            journey_mode.hasRecipes = unlockRecipes;
            journey_mode.tempList = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getResearchList();
            boolean wasCreative;
            if (player.getAbilities().instabuild) {
                wasCreative = true;
            } else {
                wasCreative = false;
            }
            ((ServerPlayer) player).setGameMode(GameType.CREATIVE);
            if(!wasCreative) {
                player.getAbilities().instabuild = false;
            }
            boolean wasGodMode = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getGodMode();
            if (wasGodMode == false) {
                player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(false);
            }
            context.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleOnClient(this, wasCreative, wasGodMode, player)));
        } else if (menuType.equals("recipes")) {
            ResourceLocation[] locations = journey_mode.itemListHandler.getLocations();
            boolean[] achieved = new boolean[locations.length];
            int recipeCount = 0;
            for (int i = 0; i < locations.length; i++) {
                Advancement recipeAdvancement = player.getServer().getAdvancements().getAdvancement(locations[i]);
                journey_mode.LOGGER.info("checking advancement number " + i);
                if (serverPlayer.getAdvancements().getOrStartProgress(recipeAdvancement).isDone()) {
                    achieved[i] = true;
                    recipeCount += 1;
                } else {
                    achieved[i] = false;
                }
            }
            journey_mode.tempAdvance = achieved;
            journey_mode.tempCount = recipeCount;
            NetworkHooks.openGui((ServerPlayer) player, new JourneyModeRecipesContainerProvider());
        }
    }

    public void handleOnClient(final MenuSwitchPacket msg, boolean wasCreative, boolean wasGodMode, Player player) {
        EventHandler.duplicationMenuOpenEvent(new DuplicationMenuPacket(wasCreative, wasGodMode, player.getStringUUID()), (ServerPlayer) player);
    }
}
