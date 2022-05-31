package com.Deeakron.journey_mode.client.event;

import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.*;
import com.Deeakron.journey_mode.init.UnobtainItemInit;
import com.Deeakron.journey_mode.journey_mode;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.advancements.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Date;

@Mod.EventBusSubscriber(modid = journey_mode.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    private static final String PROTOCOL_VERSION = "1";
    public Level world;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("journey_mode", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof  ServerPlayer){
            JMCapabilityProvider provider = new JMCapabilityProvider();
            event.addCapability(new ResourceLocation(journey_mode.MODID), provider);
            event.addListener(provider::invalidate);
            EntityJourneyMode cap = event.getObject().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
            cap.setPlayer(event.getObject().getUUID());
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
        Entity source = event.getSource().getDirectEntity();
        Player player = null;
        if (source instanceof  Player) {
            player = (Player) source;
        }

        if (source instanceof ProjectileEntity) {
            try{
                if (((ProjectileEntity) source).getOwner().getEntity() instanceof Player) {
                    player = (Player) ((ProjectileEntity) source).getOwner().getEntity();
                }
            } catch (NullPointerException e) {
                //for instances where the shooter is dead before the projectile kills
            }

        }

        if (source instanceof WolfEntity) {
            if (((WolfEntity) source).getOwner() instanceof Player) {
                player = (Player) ((WolfEntity) source).getOwner();
            }
        }

        if (player instanceof Player){
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (SpawnEggItem.byId(event.getEntity().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.byId(event.getEntity().getType()).getItem().getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap.updateResearch(new String[]{item},
                        new int[]{1},
                        false,
                        player.getUUID(),
                        SpawnEggItem.byId(event.getEntity().getType()).getItem().getDefaultInstance());
            }
        }

    }

    @SubscribeEvent
    public static void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event){
        if (event.getCausedByPlayer() != null) {
            Player player = event.getCausedByPlayer();
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (SpawnEggItem.byId(event.getChild().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.byId(event.getChild().getType()).getItem().getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap.updateResearch(new String[]{item},
                        new int[]{1},
                        false,
                        player.getUUID(),
                        SpawnEggItem.byId(event.getChild().getType()).getItem().getDefaultInstance());
            }
        }
    }

    @SubscribeEvent
    public static void onParrotTameEvent(AnimalTameEvent event) {
        if (event.getAnimal().getType() == EntityType.PARROT) {
            Player player = event.getTamer();
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (SpawnEggItem.byId(event.getAnimal().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.byId(event.getAnimal().getType()).getItem().getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap.updateResearch(new String[]{item},
                        new int[]{4},
                        false,
                        player.getUUID(),
                        SpawnEggItem.byId(event.getAnimal().getType()).getItem().getDefaultInstance());
            }
        }
    }

    @SubscribeEvent
    public static void  onPlayerClone(final PlayerEvent.Clone event) {
        if (event.getEntity() instanceof  ServerPlayer){
            if (true){
                Player original = event.getOriginal();
                Player newer = event.getPlayer();
                EntityJourneyMode cap = original.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                EntityJourneyMode cap2 = newer.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap2.setJourneyMode(cap.getJourneyMode());
                cap2.setResearchList(cap.getResearchList());
                cap2.setPlayer(newer.getUUID());
                cap2.setGodMode(cap.getGodMode());
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
    public static void MenuResearchEvent(final MenuResearchEvent event) {
        INSTANCE.sendToServer(new ResearchPacket(event.getItem(), event.getCount(), event.getPlayer().toString(), event.getItemStack()));
    }

    @SubscribeEvent
    public static void openMenu(final InputEvent.KeyInputEvent event) {

        if (journey_mode.keyBindings[0].consumeClick()) {
            Player player = Minecraft.getInstance().player;
            JMCheckPacket packet = new JMCheckPacket(player.getUUID().toString(), false);
            INSTANCE.sendToServer(packet);
        }
    }

    @SubscribeEvent
    public static void openMenuActual(final MenuOpenEvent event) {
        if (new Date().getTime() - journey_mode.lastMenuOpened.getTime() >= 400) {
            ITextComponent title = new StringTextComponent("Journey Mode Menu");
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
                            String item = "\"" + SpawnEggItem.byId(EntityType.VILLAGER).getItem().getRegistryName() + "\"";
                            EntityJourneyMode cap = event.getPlayer().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                            cap.updateResearch(new String[]{item},
                                    new int[]{1},
                                    false,
                                    event.getPlayer().getUUID(),
                                    SpawnEggItem.byId(EntityType.VILLAGER).getItem().getDefaultInstance());
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
                            String item = "\"" + SpawnEggItem.byId(EntityType.DOLPHIN).getItem().getRegistryName() + "\"";
                            EntityJourneyMode cap = event.getPlayer().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                            cap.updateResearch(new String[]{item},
                                    new int[]{1},
                                    false,
                                    event.getPlayer().getUUID(),
                                    SpawnEggItem.byId(EntityType.DOLPHIN).getItem().getDefaultInstance());
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

}
