package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.advancements.JMTriggers;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.Deeakron.journey_mode.client.event.EventHandler;
import com.Deeakron.journey_mode.client.event.RegisterCommandEvent;
import com.Deeakron.journey_mode.config.Config;
import com.Deeakron.journey_mode.config.NewFilesConfig;
import com.Deeakron.journey_mode.config.UnobtainConfig;
import com.Deeakron.journey_mode.container.JourneyModeResearchContainer;
import com.Deeakron.journey_mode.init.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

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
    public static KeyMapping[] keyBindings;
    public static ReplacementList replacementList;
    public static boolean doReplace = false;
    public static Date lastMenuOpened;
    public static DuplicationList dupeList;
    //these variables are temporarily used for opening the menu
    public static boolean freeze = false;
    public static int tickSpeed = 1;
    public static boolean mobSpawn = false;
    public static boolean mobGrief = false;
    public static boolean godMode = false;
    public static boolean keepInv = false;
    public static ResearchList tempList;
    public static boolean hasRecipes;
    public static boolean[] tempAdvance;
    public static int tempCount;
    public static JourneyModeResearchContainer tempContain;
    public static AntikytheraRecipeItemList itemListHandler;
    public static boolean useUnobtain;

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
            this.list = new ItemList("data/journey_mode/duplication_menu/base_minecraft.json", true);
            if (UnobtainConfig.use_unobtainable.get()) {
                ItemList unobtainList = new ItemList("data/journey_mode/duplication_menu/unobtainable.json", true);
                this.list.updateList(unobtainList);
                String[] test = new String[1];
                test[0] = "\"minecraft:bedrock\"";
            }
            if (NewFilesConfig.use_other_files.get()) {
                ItemList changeList = new ItemList(FMLPaths.CONFIGDIR.get().resolve("changes.json").toString(), false);
                this.list.updateList(changeList);
            }
            if (NewFilesConfig.use_replacements.get()) {
                this.replacementList = new ReplacementList(FMLPaths.CONFIGDIR.get().resolve("replacements.json").toString(), false);
                this.doReplace = true;
            }
            if (NewFilesConfig.use_removals.get()) {
                ItemList removeList = new ItemList(FMLPaths.CONFIGDIR.get().resolve("removals.json").toString(), false);
                this.list.removeItem(removeList.getItems());
            }
        } catch (IOException e) {

        };


        keyBindings = new KeyMapping[1];

        keyBindings[0] = new KeyMapping("key.journey_mode.menu", GLFW.GLFW_KEY_O, "key.categories.journey_mode");

        ClientRegistry.registerKeyBinding(keyBindings[0]);



        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (UnobtainConfig.use_unobtainable.get()) {
            this.useUnobtain = true;
            UnobtainBlockInit.BLOCKS.register(bus);
            UnobtainItemInit.ITEMS.register(bus);
            JMRecipeSerializerInit.RECIPE_SERIALIZERS.register(bus);
            JMTileEntityTypes.TILE_ENTITY_TYPES.register(bus);
        } else {
            this.useUnobtain = false;
        }

        JMSounds.SOUNDS.register(bus);
        this.dupeList = new DuplicationList(list);
        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        JMContainerTypes.CONTAINER_TYPES.register(bus);
        if (UnobtainConfig.use_unobtainable.get()) {
            DuplicationInit.init();
            DuplicationInit.ITEMS.register(bus);
        }

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        //LOGGER.info("HELLO FROM PREINIT");
        //LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
        JMCapabilityProvider.register();
        EventHandler.registerPackets();
        JMContainerTypes.registerScreens();
        JMTriggers.init();

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        ItemBlockRenderTypes.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_0.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_1.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_2.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER_PART_0.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER_PART_1.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER_PART_2.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER_PART_0.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER_PART_1.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER_PART_2.get(), RenderType.cutout());
        /* this was from before 1.17
        RenderTypeLookup.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_0.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_1.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.WOODEN_RESEARCH_GRINDER_PART_2.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER_PART_0.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER_PART_1.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.IRON_RESEARCH_GRINDER_PART_2.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER_PART_0.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER_PART_1.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.DIAMOND_RESEARCH_GRINDER_PART_2.get(), RenderType.cutout());/*
        if (UnobtainConfig.use_unobtainable.get()) {
            RenderTypeLookup.setRenderLayer(UnobtainBlockInit.PAINTED_BARRIER.get(), RenderType.cutout());
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        /*LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));*/
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
        if (this.useUnobtain) {
            this.itemListHandler = new AntikytheraRecipeItemList(event.getServer());
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            //LOGGER.info("HELLO from Register Block");
        }
    }
}
