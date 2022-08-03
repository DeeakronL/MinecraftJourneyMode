package com.Deeakron.journey_mode.client.event;

import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.*;
import com.Deeakron.journey_mode.init.UnobtainItemInit;
import com.Deeakron.journey_mode.journey_mode;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.advancements.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = journey_mode.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    private static final String PROTOCOL_VERSION = "1";
    public Level world;
    private static List<UUID> awaitingRespawn = new ArrayList<UUID>();
    private static List<EntityJourneyMode> awaitingRespawnCap = new ArrayList<EntityJourneyMode>();
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("journey_mode", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof  Player){
            Player player = (Player) event.getObject();
            journey_mode.LOGGER.info(player.level.isClientSide + " apparently");
            journey_mode.LOGGER.info(player.getCapability(JMCapabilityProvider.INSTANCE, null).isPresent() + " is Present");
            JMCapabilityProvider provider = new JMCapabilityProvider();
            event.addCapability(new ResourceLocation(journey_mode.MODID), provider);
            //event.addListener(provider::invalidate);
            EntityJourneyMode cap = event.getObject().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
            cap.setPlayer(event.getObject().getUUID());
            //INSTANCE.sendToServer(new CapabilityPacket(cap));


        }
    }

    @SubscribeEvent static void onPlayerLogInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        EntityJourneyMode cap = event.getEntity().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
        cap.setGodMode(cap.getGodMode());
    }

    /*@SubscribeEvent
    //This event is just for easy testing purposes with research, uncomment to reuse

    public static void pickupItem(EntityItemPickupEvent event) {
        String item = "\"" + event.getItem().getItem().getItem().getRegistryName() + "\"";
        int count = event.getItem().getItem().getCount();
        EntityJourneyMode cap = event.getEntity().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
        cap.updateResearch(new String[]{item}, new int[]{count});
    }*/

    @SubscribeEvent
    public static void onResearchEvent(ResearchEvent event) {
        String item = "\"" + event.getItem().getItem().getItem().getRegistryName() + "\"";
        int count = event.getItem().getItem().getCount();
        if(event.getEntity() != null){
            EntityJourneyMode cap = event.getEntity().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
            cap.updateResearch(new String[]{item}, new int[]{count}, false, event.getEntity().getUUID(), event.getItem().getItem());
        }

    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        journey_mode.LOGGER.info("oof ouch my bones?");
        Entity source = event.getSource().getDirectEntity();
        Player player = null;
        if (event.getEntity() instanceof Player) {
            EntityJourneyMode cap = event.getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            if (cap.getPlayer() == null) {
                cap.setPlayer(event.getEntity().getUUID());
            }
            updateAwaitingRespawn(event.getEntity().getUUID(), cap);
            journey_mode.LOGGER.info("Death JM: " + cap.getJourneyMode());
        }
        if (source instanceof  Player) {
            player = (Player) source;
        }

        if (source instanceof Projectile) {
            try{
                if (((Projectile) source).getOwner() instanceof Player) {
                    player = (Player) ((Projectile) source).getOwner();
                }
            } catch (NullPointerException e) {
                //for instances where the shooter is dead before the projectile kills
            }

        }

        if (source instanceof Wolf) {
            if (((Wolf) source).getOwner() instanceof Player) {
                player = (Player) ((Wolf) source).getOwner();
            }
        }

        if (player instanceof Player){
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (SpawnEggItem.byId(event.getEntity().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.byId(event.getEntity().getType()).getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap.updateResearch(new String[]{item},
                        new int[]{1},
                        false,
                        player.getUUID(),
                        SpawnEggItem.byId(event.getEntity().getType()).getDefaultInstance());
            }
        }

    }

    @SubscribeEvent
    public static void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event){
        if (event.getCausedByPlayer() != null) {
            Player player = event.getCausedByPlayer();
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (SpawnEggItem.byId(event.getChild().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.byId(event.getChild().getType()).getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap.updateResearch(new String[]{item},
                        new int[]{1},
                        false,
                        player.getUUID(),
                        SpawnEggItem.byId(event.getChild().getType()).getDefaultInstance());
            }
        }
    }

    @SubscribeEvent
    public static void onParrotTameEvent(AnimalTameEvent event) {
        if (event.getAnimal().getType() == EntityType.PARROT) {
            Player player = event.getTamer();
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (SpawnEggItem.byId(event.getAnimal().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.byId(event.getAnimal().getType()).getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap.updateResearch(new String[]{item},
                        new int[]{4},
                        false,
                        player.getUUID(),
                        SpawnEggItem.byId(event.getAnimal().getType()).getDefaultInstance());
            }
        }
    }

    @SubscribeEvent
    public static void  onPlayerClone(final PlayerEvent.Clone event) {
        //journey_mode.LOGGER.info("oof ouch my bones!");
        if (event.getEntity() instanceof  ServerPlayer){
            //journey_mode.LOGGER.info("oof ouch my bones!2");
            if (event.isWasDeath()){
                clearAwaitingRespawn(event.getPlayer().getUUID(), (ServerPlayer) event.getEntity());
                EntityJourneyMode cap = event.getPlayer().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                //journey_mode.LOGGER.info("Old Respawn JM: " + event.getOriginal().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).getJourneyMode());
                //journey_mode.LOGGER.info("Respawn JM: " + cap.getJourneyMode());
                event.getOriginal().invalidateCaps();

            } else {
                //journey_mode.LOGGER.info("oof ouch my bones!3");
                Player player = (Player) event.getOriginal();
                player.revive();
                player.reviveCaps();
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                if (cap.getPlayer() == null) {
                    cap.setPlayer(player.getUUID());
                }
                INSTANCE.sendToServer(new CapabilityPacket(cap));
                ServerPlayer player2 = (ServerPlayer) event.getPlayer();
                EntityJourneyMode cap2 = player2.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap2.setJourneyMode(cap.getJourneyMode());
                cap2.setGodMode(cap.getGodMode());
                cap2.setResearchList(cap.getResearchList());
                cap2.setPlayer(cap.getPlayer());
                player.invalidateCaps();
            }
        }
    }

    @SubscribeEvent
    static void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        EntityJourneyMode cap = event.getEntity().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
        cap.setGodMode(cap.getGodMode());
    }

    public static void registerPackets() {
        INSTANCE.registerMessage(0, CommandPacket.class, CommandPacket::encode, CommandPacket::decode, CommandPacket::handle);
        INSTANCE.registerMessage(1, JMCheckPacket.class, JMCheckPacket::encode, JMCheckPacket::decode, JMCheckPacket::handle);
        INSTANCE.registerMessage(2, GameStatePacket.class, GameStatePacket::encode, GameStatePacket::decode,GameStatePacket::handle);
        INSTANCE.registerMessage(3, MenuSwitchPacket.class, MenuSwitchPacket::encode, MenuSwitchPacket::decode, MenuSwitchPacket::handle);
        INSTANCE.registerMessage(4, ResearchPacket.class, ResearchPacket::encode, ResearchPacket::decode, ResearchPacket::handle);
        INSTANCE.registerMessage(5, DuplicationMenuPacket.class, DuplicationMenuPacket::encode, DuplicationMenuPacket::decode, DuplicationMenuPacket::handle);
        INSTANCE.registerMessage(6, CloseDuplicationMenuPacket.class, CloseDuplicationMenuPacket::encode, CloseDuplicationMenuPacket::decode, CloseDuplicationMenuPacket::handle);
        INSTANCE.registerMessage(7, CapabilityPacket.class, CapabilityPacket::encode, CapabilityPacket::decode, CapabilityPacket::handle);
    }

    @SubscribeEvent
    public static void PowersCommandEvent(final PowersCommandEvent event) {
        INSTANCE.sendToServer(new CommandPacket(event.command));
    }

    @SubscribeEvent
    public static void MenuSwitchEvent(final MenuSwitchEvent event) {
        INSTANCE.sendToServer(new MenuSwitchPacket(event.player, event.menuType));
    }

    @SubscribeEvent
    public static void CloseDuplicationMenuEvent(final CloseDuplicationMenuEvent event) {
        INSTANCE.sendToServer(new CloseDuplicationMenuPacket(event.mode, event.player));
    }

    @SubscribeEvent
    public static void MenuResearchEvent(final MenuResearchEvent event) {
        INSTANCE.sendToServer(new ResearchPacket(event.getItem(), event.getCount(), event.getPlayer().toString(), event.getItemStack()));
    }

    @SubscribeEvent
    public static void openMenu(final InputEvent.KeyInputEvent event) {

        if (journey_mode.keyBindings[0].consumeClick()) {
            journey_mode.LOGGER.info("Click");
            Player player = Minecraft.getInstance().player;
            JMCheckPacket packet = new JMCheckPacket(player.getUUID().toString(), false);
            INSTANCE.sendToServer(packet);
        }
    }

    @SubscribeEvent
    public static void openMenuActual(final MenuOpenEvent event) {
        if (new Date().getTime() - journey_mode.lastMenuOpened.getTime() >= 400) {
            Component title = new TextComponent("Journey Mode Menu");
            journey_mode.lastMenuOpened = new Date();
        }
    }

    @SubscribeEvent
    public static void itemDroppedEvent(final ItemTossEvent event) {
        event.getEntityItem().setThrower(event.getPlayer().getUUID());
    }

    @SubscribeEvent
    public static void onLivingDamageEvent(LivingDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            if (event.getSource() == JMDamageSources.RESEARCH_GRINDER) {
                BlockPos pos = event.getEntity().blockPosition();
                event.getEntity().getCommandSenderWorld().playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundSource.BLOCKS, 0.10f, 1.0f);
            }
        }
    }

    @SubscribeEvent
    public static void onAdvancementEvent(AdvancementEvent event) {
        ServerPlayer player = event.getPlayer().getServer().getPlayerList().getPlayer(event.getPlayer().getUUID());
        MinecraftServer server = event.getPlayer().getServer();
        Advancement advancement = server.getAdvancements().getAdvancement(new ResourceLocation("journey_mode:journey_mode/villager_traded"));
        Advancement advancement2 = server.getAdvancements().getAdvancement(new ResourceLocation("journey_mode:journey_mode/dolphin_fed"));
        if (advancement != null) {
            if (event.getAdvancement().getId() == advancement.getId()) {
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                AdvancementProgress advancementprogress = player.getAdvancements().getOrStartProgress(advancement);
                if (!advancementprogress.hasProgress()) {

                } else {
                    for(String s : advancementprogress.getCompletedCriteria()) {
                        boolean isCapped = false;
                        if (helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                            String item = "\"" + SpawnEggItem.byId(EntityType.VILLAGER).getRegistryName() + "\"";
                            EntityJourneyMode cap = event.getPlayer().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                            cap.updateResearch(new String[]{item},
                                    new int[]{1},
                                    false,
                                    event.getPlayer().getUUID(),
                                    SpawnEggItem.byId(EntityType.VILLAGER).getDefaultInstance());
                            if(cap.getResearch(item)[0] == 64) {
                                isCapped = true;
                            }
                        }
                        if (!isCapped) {
                            player.getAdvancements().revoke(advancement, s);
                        }

                    }

                }
            }
        }
        if (advancement2 != null) {
            if (event.getAdvancement().getId() == advancement2.getId()) {
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                AdvancementProgress advancementprogress = player.getAdvancements().getOrStartProgress(advancement2);
                if (!advancementprogress.hasProgress()) {

                } else {
                    for(String s : advancementprogress.getCompletedCriteria()) {
                        boolean isCapped = false;
                        if (helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                            String item = "\"" + SpawnEggItem.byId(EntityType.DOLPHIN).getRegistryName() + "\"";
                            EntityJourneyMode cap = event.getPlayer().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                            cap.updateResearch(new String[]{item},
                                    new int[]{1},
                                    false,
                                    event.getPlayer().getUUID(),
                                    SpawnEggItem.byId(EntityType.DOLPHIN).getDefaultInstance());
                            if(cap.getResearch(item)[0] == 64) {
                                isCapped = true;
                            }
                        }
                        if (!isCapped) {
                            player.getAdvancements().revoke(advancement2, s);
                        }

                    }

                }
            }
        }

    }

    // Didn't feel as good in testing to do
    /*@SubscribeEvent
    public static void onGameModeChangeEvent(PlayerEvent.PlayerChangeGameModeEvent event) {
        EntityJourneyMode cap = event.getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
        if (cap.getJourneyMode()){
            cap.setJourneyMode(false);
        }
        journey_mode.LOGGER.info(event.getNewGameMode().getName());
        if (event.getNewGameMode().isCreative() || event.getNewGameMode().getName() == "spectator") {
            cap.setGodMode(true);
        } else {
            cap.setGodMode(false);
        }
    }*/


    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        try{
            if (event.player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == UnobtainItemInit.PAINT_BUCKET.get()) {
                Level world = event.player.level;
                BlockPos pos = event.player.blockPosition();
                for (int i = -5; i < 6; i++) {
                    for (int j = -5; j < 6; j++) {
                        for (int k = -5; k < 6; k++) {
                            if (world.getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).is(Blocks.BARRIER)) {
                                world.addParticle(ParticleTypes.BARRIER, pos.getX() + (double) i + 0.5D, pos.getY() + (double) j + 0.5D, pos.getZ() + (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {

        }
    }

    public static void duplicationMenuOpenEvent(Object msg, ServerPlayer player) {
        INSTANCE.sendTo(msg, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    private static void updateAwaitingRespawn(UUID uuid, EntityJourneyMode cap) {
        awaitingRespawn.add(uuid);
        awaitingRespawnCap.add(cap);
    }

    private static void clearAwaitingRespawn(UUID uuid, ServerPlayer player) {
        if (awaitingRespawn.size() == 0 || awaitingRespawnCap.size() == 0) {
            return;
        }
        EntityJourneyMode cap = awaitingRespawnCap.get(awaitingRespawn.indexOf(uuid));
        INSTANCE.sendToServer(new CapabilityPacket(cap));
        EntityJourneyMode cap2 = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
        cap2.setJourneyMode(cap.getJourneyMode());
        cap2.setGodMode(cap.getGodMode());
        cap2.setResearchList(cap.getResearchList());
        cap2.setPlayer(cap.getPlayer());
        awaitingRespawn.remove(uuid);
        awaitingRespawnCap.remove(cap);
    }
}
