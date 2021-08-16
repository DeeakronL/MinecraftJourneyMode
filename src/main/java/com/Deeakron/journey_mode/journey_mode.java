package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.event.EventHandler;
import com.Deeakron.journey_mode.config.Config;
import com.Deeakron.journey_mode.config.NewFilesConfig;
import com.Deeakron.journey_mode.config.UnobtainConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("journey_mode")
public class journey_mode
{
    public static final String MODID = "journey_mode";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static ItemList list;
    public static KeyBinding[] keyBindings;
    public static ReplacementList replacementList;
    public static boolean doReplace = false;
    public static Date lastMenuOpened;

    public journey_mode() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.server_config);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.client_config);

        Config.loadConfig(Config.client_config, FMLPaths.CONFIGDIR.get().resolve("journeyMode-client.toml").toString());
        Config.loadConfig(Config.server_config, FMLPaths.CONFIGDIR.get().resolve("journeyMode-server.toml").toString());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        this.lastMenuOpened = new Date();



        try{
            this.list = new ItemList("data/journey_mode/duplication_menu/base_minecraft.json");
            if (UnobtainConfig.use_unobtainable.get()) {
                ItemList unobtainList = new ItemList("data/journey_mode/duplication_menu/unobtainable.json");
                this.list.updateList(unobtainList);
                String[] test = new String[1];
                test[0] = "\"minecraft:bedrock\"";
                //this.list.removeItem(test);
            }
            if (NewFilesConfig.use_other_files.get()) {
                ItemList changeList = new ItemList(FMLPaths.CONFIGDIR.get().resolve("changes.json").toString());
                this.list.updateList(changeList);
            }
            if (NewFilesConfig.use_replacements.get()) {
                this.replacementList = new ReplacementList(FMLPaths.CONFIGDIR.get().resolve("replacements.json").toString());
                this.doReplace = true;
            }
            if (NewFilesConfig.use_removals.get()) {
                ItemList removeList = new ItemList(FMLPaths.CONFIGDIR.get().resolve("removals.json").toString());
                this.list.removeItem(removeList.getItems());
            }
        } catch (IOException e) {

        };

        keyBindings = new KeyBinding[1];

        keyBindings[0] = new KeyBinding("key.journey_mode.menu", GLFW.GLFW_KEY_O, "key.categories.journey_mode");

        ClientRegistry.registerKeyBinding(keyBindings[0]);



        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (UnobtainConfig.use_unobtainable.get()) {
            UnobtainBlockInit.BLOCKS.register(bus);
            UnobtainItemInit.ITEMS.register(bus);
        }
        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        JMContainerTypes.CONTAINER_TYPES.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
        JMCapabilityProvider.register();
        EventHandler.registerPackets();
        JMContainerTypes.registerScreens();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        RenderTypeLookup.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_0.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_1.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_2.get(), RenderType.getCutout());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
