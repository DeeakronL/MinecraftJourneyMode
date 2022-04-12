package com.Deeakron.journey_mode.client.event;

import com.Deeakron.journey_mode.config.UnobtainConfig;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.*;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import com.Deeakron.journey_mode.init.UnobtainItemInit;
import com.Deeakron.journey_mode.item.ScannerItem;
import com.Deeakron.journey_mode.journey_mode;
import com.Deeakron.journey_mode.util.JMDamageSources;
import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.command.impl.GameModeCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.ITickList;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Date;
import java.util.Properties;

@Mod.EventBusSubscriber(modid = journey_mode.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    private static final String PROTOCOL_VERSION = "1";
    public World world;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("journey_mode", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof  ServerPlayerEntity){
            JMCapabilityProvider provider = new JMCapabilityProvider();
            event.addCapability(new ResourceLocation(journey_mode.MODID), provider);
            event.addListener(provider::invalidate);
            EntityJourneyMode cap = event.getObject().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
            cap.setPlayer(event.getObject().getUniqueID());
            //journey_mode.LOGGER.info("attach capabilities event");
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
            cap.updateResearch(new String[]{item}, new int[]{count}, false, event.getEntity().getUniqueID(), event.getItem().getItem());
        }

    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        //journey_mode.LOGGER.info(event.getSource());
        //journey_mode.LOGGER.info(SpawnEggItem.getEgg(event.getEntity().getType()).getItem().toString());
        Entity source = event.getSource().getImmediateSource();
        PlayerEntity player = null;
        if (source instanceof  PlayerEntity) {
            //journey_mode.LOGGER.info("How dare you?");
            player = (PlayerEntity) source;
        }

        if (source instanceof ProjectileEntity) {
            try{
                if (((ProjectileEntity) source).getShooter().getEntity() instanceof PlayerEntity) {
                    player = (PlayerEntity) ((ProjectileEntity) source).getShooter().getEntity();
                }
            } catch (NullPointerException e) {
                journey_mode.LOGGER.info("OOF");
            }

        }

        if (source instanceof WolfEntity) {
            if (((WolfEntity) source).getOwner() instanceof PlayerEntity) {
                player = (PlayerEntity) ((WolfEntity) source).getOwner();
            }
        }

        if (player instanceof PlayerEntity){
            ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
            if (SpawnEggItem.getEgg(event.getEntity().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.getEgg(event.getEntity().getType()).getItem().getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                //journey_mode.LOGGER.info("made it here");
                cap.updateResearch(new String[]{item},
                        new int[]{1},
                        false,
                        player.getUniqueID(),
                        SpawnEggItem.getEgg(event.getEntity().getType()).getItem().getDefaultInstance());
            }
        }

    }

    @SubscribeEvent
    public static void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event){
        if (event.getCausedByPlayer() != null) {
            PlayerEntity player = event.getCausedByPlayer();
            ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
            if (SpawnEggItem.getEgg(event.getChild().getType()) != null && helmet.getItem() == UnobtainItemInit.SCANNER.get()) {
                String item = "\"" + SpawnEggItem.getEgg(event.getChild().getType()).getItem().getRegistryName() + "\"";
                EntityJourneyMode cap = player.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                //journey_mode.LOGGER.info("made it here");
                cap.updateResearch(new String[]{item},
                        new int[]{1},
                        false,
                        player.getUniqueID(),
                        SpawnEggItem.getEgg(event.getChild().getType()).getItem().getDefaultInstance());
            }
        }
    }

    /*@SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
    }*/

    @SubscribeEvent
    public static void  onPlayerClone(final PlayerEvent.Clone event) {
        if (event.getEntity() instanceof  ServerPlayerEntity){
            if (true){
                //journey_mode.LOGGER.info("ouch");
                PlayerEntity original = event.getOriginal();
                PlayerEntity newer = event.getPlayer();
                EntityJourneyMode cap = original.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                EntityJourneyMode cap2 = newer.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap2.setJourneyMode(cap.getJourneyMode());
                cap2.setResearchList(cap.getResearchList());
                cap2.setPlayer(newer.getUniqueID());
                //journey_mode.LOGGER.info(cap.getGodMode() + " " + cap2.getPlayer());
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

        if (journey_mode.keyBindings[0].isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            JMCheckPacket packet = new JMCheckPacket(player.getUniqueID().toString(), false);
            INSTANCE.sendToServer(packet);
            /*ITextComponent title = new StringTextComponent("Journey Mode Menu");
            Minecraft.getInstance().displayGuiScreen(new JourneyModePowersScreen(Minecraft.getInstance().player.inventory, title, 5, false, 1, false, false, false, false));*/
        }
    }

    @SubscribeEvent
    public static void openMenuActual(final MenuOpenEvent event) {
        //journey_mode.lastMenuOpened = new Date();
        //journey_mode.LOGGER.info("time compare: " + journey_mode.lastMenuOpened.getTime());
        //journey_mode.LOGGER.info("new time: " + new Date().getTime());
        //journey_mode.lastMenuOpened = new Date();
        //if (journey_mode.lastMenuOpened.compareTo(new Date()))
        if (new Date().getTime() - journey_mode.lastMenuOpened.getTime() >= 400) {
            ITextComponent title = new StringTextComponent("Journey Mode Menu");
            //Minecraft.getInstance().displayGuiScreen(new JourneyModePowersScreen(Minecraft.getInstance().player.inventory, title, 5, event.freeze, event.tickSpeed, event.mobSpawn, event.mobGrief, event.godMode, event.keepInv));
            journey_mode.lastMenuOpened = new Date();
        }
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        //ScreenManager.registerFactory(JMContainerTypes.JOURNEY_MODE_POWERS.get(), JourneyModePowersScreen::new);
    }

    @SubscribeEvent
    public static void itemDroppedEvent(final ItemTossEvent event) {
        event.getEntityItem().setThrowerId(event.getPlayer().getUniqueID());
    }


    /*@SubscribeEvent
    public static void onSoundEvent(PlaySoundEvent event) {
        if (event.getSound().getSound(). == JMSounds.RESEARCH_GRIND.)
    }*/

    @SubscribeEvent
    public static void onLivingDamageEvent(LivingDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            if (event.getSource() == JMDamageSources.RESEARCH_GRINDER) {
                //journey_mode.LOGGER.info("insert sound here");
                BlockPos pos = event.getEntity().getPosition();
                event.getEntity().getEntityWorld().playSound(null, pos, JMSounds.RESEARCH_GRIND.get(), SoundCategory.BLOCKS, 0.10f, 1.0f);
            }
        }
    }

    @SubscribeEvent
    public static void onAdvancementEvent(AdvancementEvent event) {
        ServerPlayerEntity player = event.getPlayer().getServer().getPlayerList().getPlayerByUUID(event.getPlayer().getUniqueID());
        MinecraftServer server = event.getPlayer().getServer();
        Advancement advancement = server.getAdvancementManager().getAdvancement(new ResourceLocation("journey_mode:journey_mode/villager_traded"));
        if (advancement != null) {
            journey_mode.LOGGER.info("correct advancement?");
            if (event.getAdvancement().getId() == advancement.getId()) {
                journey_mode.LOGGER.info("correct advancement");
                AdvancementProgress advancementprogress = player.getAdvancements().getProgress(advancement);
                if (!advancementprogress.hasProgress()) {

                } else {
                    for(String s : advancementprogress.getCompletedCriteria()) {
                        player.getAdvancements().revokeCriterion(advancement, s);
                        journey_mode.LOGGER.info("advancement revoked");
                        String item = "\"" + SpawnEggItem.getEgg(EntityType.VILLAGER).getItem().getRegistryName() + "\"";
                        EntityJourneyMode cap = event.getPlayer().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                        //journey_mode.LOGGER.info("made it here");
                        cap.updateResearch(new String[]{item},
                                new int[]{1},
                                false,
                                event.getPlayer().getUniqueID(),
                                SpawnEggItem.getEgg(EntityType.VILLAGER).getItem().getDefaultInstance());
                    }

                }
            }
        }


        journey_mode.LOGGER.info("ADVANCEMENT: " + event.getAdvancement());
        //journey_mode.LOGGER.info("ADVANCEMENT: " + event.getAdvancement().getCriteria());
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
    public static void onBlockPlacedEvent(BlockEvent.EntityPlaceEvent event){
        if (event.getPlacedBlock().getBlock().matchesBlock(Blocks.COMMAND_BLOCK)){
            //journey_mode.LOGGER.info(event.getPlacedBlock().getBlockState().get(BlockStateProperties.FACING));
        }
        if (event.getPlacedBlock().getBlock().matchesBlock(UnobtainBlockInit.INERT_COMMAND_BLOCK.get())){
            //journey_mode.LOGGER.info(event.getPlacedBlock().getBlockState().get(BlockStateProperties.FACING));
        }
    }

    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        try{
            if (event.player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == UnobtainItemInit.PAINT_BUCKET.get()) {
                World world = event.player.world;
                BlockPos pos = event.player.getPosition();
                for (int i = -5; i < 6; i++) {
                    for (int j = -5; j < 6; j++) {
                        for (int k = -5; k < 6; k++) {
                            if (world.getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).matchesBlock(Blocks.BARRIER)) {
                                //journey_mode.LOGGER.info("particle spawned?");
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
