package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.container.JourneyModePowersContainerProvider;
import com.Deeakron.journey_mode.container.JourneyModeRecipesContainerProvider;
import com.Deeakron.journey_mode.container.JourneyModeResearchContainerProvider;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.gui.JourneyModeDuplicationScreen;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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
        Advancement advancement = player.getServer().getAdvancementManager().getAdvancement(new ResourceLocation("journey_mode:journey_mode/get_antikythera"));
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        boolean unlockRecipes = false;
        if (serverPlayer.getAdvancements().getProgress(advancement).isDone()) {
            unlockRecipes = true;
        }
        if (menuType.equals("powers")) {
            journey_mode.hasRecipes = unlockRecipes;
            NetworkHooks.openGui((ServerPlayerEntity) player, new JourneyModePowersContainerProvider());
        } else if (menuType.equals("research")) {
            journey_mode.hasRecipes = unlockRecipes;
            journey_mode.tempList = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getResearchList();
            NetworkHooks.openGui((ServerPlayerEntity) player, new JourneyModeResearchContainerProvider());
        } else if (menuType.equals("duplication")) {
            journey_mode.hasRecipes = unlockRecipes;
            journey_mode.tempList = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getResearchList();
            boolean wasCreative;
            if (player.abilities.isCreativeMode) {
                wasCreative = true;
            } else {
                wasCreative = false;
            }
            player.setGameType(GameType.CREATIVE);
            if(!wasCreative) {
                player.abilities.isCreativeMode = false;
            }
            boolean wasGodMode = player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getGodMode();
            if (wasGodMode == false) {
                player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(false);
            }
            context.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleOnClient(this, wasCreative, wasGodMode, player)));
            //NetworkHooks.openGui((ServerPlayerEntity) player, new JourneyModeDuplicationContainerProvider());
        } else if (menuType.equals("recipes")) {
            ResourceLocation[] locations = {new ResourceLocation("journey_mode:recipes/antikythera_crafting_barrier"), new ResourceLocation("journey_mode:recipes/antikythera_crafting_chain_command_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_command_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_command_block_minecart"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_debug_stick"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_dirt_path"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_end_portal_frame"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_farmland"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_cobblestone"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_chiseled_stone_bricks"), new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_mossy_stone_bricks"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_stone"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_stone_bricks"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_infested_cracked_stone_bricks"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_jigsaw"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_knowledge_book"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_paint_bucket"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_petrified_oak_slab"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_player_head"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_repeating_command_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_scanner"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_spawner"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_structure_block"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_structure_void"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_wandering_trader_spawn_egg"),new ResourceLocation("journey_mode:recipes/antikythera_crafting_zombie_horse_spawn_egg")};
            boolean[] achieved = new boolean[locations.length];
            int recipeCount = 0;
            for (int i = 0; i < locations.length; i++) {
                Advancement recipeAdvancement = player.getServer().getAdvancementManager().getAdvancement(locations[i]);
                if (serverPlayer.getAdvancements().getProgress(recipeAdvancement).isDone()) {
                    achieved[i] = true;
                    recipeCount += 1;
                } else {
                    achieved[i] = false;
                }
            }
            journey_mode.tempAdvance = achieved;
            journey_mode.tempCount = recipeCount;
            NetworkHooks.openGui((ServerPlayerEntity) player, new JourneyModeRecipesContainerProvider());
        }
    }

    public void handleOnClient(final MenuSwitchPacket msg, boolean wasCreative, boolean wasGodMode, PlayerEntity player) {

        Minecraft.getInstance().displayGuiScreen(new JourneyModeDuplicationScreen(Minecraft.getInstance().player, wasCreative, wasGodMode, (ServerPlayerEntity) player));
    }
}
