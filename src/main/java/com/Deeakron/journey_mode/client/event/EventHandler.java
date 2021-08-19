package com.Deeakron.journey_mode.client.event;

import com.Deeakron.journey_mode.JMContainerTypes;
import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.CommandPacket;
import com.Deeakron.journey_mode.client.GameStatePacket;
import com.Deeakron.journey_mode.client.JMCheckPacket;
import com.Deeakron.journey_mode.client.gui.JourneyModePowersScreen;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Date;
import java.util.UUID;

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
            journey_mode.LOGGER.info("attach capabilities event");
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
            cap.updateResearch(new String[]{item}, new int[]{count});
        }

    }

    @SubscribeEvent
    public static void  onPlayerClone(final PlayerEvent.Clone event) {
        if (event.getEntity() instanceof  ServerPlayerEntity){
            if (true){
                journey_mode.LOGGER.info("ouch");
                PlayerEntity original = event.getOriginal();
                PlayerEntity newer = event.getPlayer();
                EntityJourneyMode cap = original.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                EntityJourneyMode cap2 = newer.getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
                cap2.setJourneyMode(cap.getJourneyMode());
                cap2.setResearchList(cap.getResearchList());
                cap2.setPlayer(newer.getUniqueID());
                journey_mode.LOGGER.info(cap.getGodMode() + " " + cap2.getPlayer());
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
    }

    @SubscribeEvent
    public static void PowersCommandEvent(final PowersCommandEvent event) {
        INSTANCE.sendToServer(new CommandPacket(event.command));
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
}
