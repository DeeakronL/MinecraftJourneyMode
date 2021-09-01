package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.client.gui.JourneyModeDuplicationScreen;
import com.Deeakron.journey_mode.client.gui.JourneyModePowersScreen;
import com.Deeakron.journey_mode.client.gui.JourneyModeResearchScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMContainerTypes {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, journey_mode.MODID);

    public static final RegistryObject<ContainerType<JourneyModePowersContainer>> JOURNEY_MODE_POWERS = CONTAINER_TYPES
            .register("journey_mode_powers", () -> IForgeContainerType.create(JourneyModePowersContainer::new));

    public static final RegistryObject<ContainerType<JourneyModeResearchContainer>> JOURNEY_MODE_RESEARCH = CONTAINER_TYPES
            .register("journey_mode_research", () -> IForgeContainerType.create(JourneyModeResearchContainer::new));

   /*public static final RegistryObject<ContainerType<JourneyModeDuplicationScreen.DuplicationContainer>> JOURNEY_MODE_DUPLICATION = CONTAINER_TYPES
            .register("journey_mode_duplication", () -> IForgeContainerType.create(JourneyModeDuplicationScreen::new));*/

    /*public static <T extends Container> RegistryObject<ContainerType<T>> registerContainer(String id, IContainerFactory<T> factory) {
        return CONTAINER_TYPES.register(id, () -> IForgeContainerType.create(factory));
    }*/

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens() {
        ScreenManager.registerFactory(JOURNEY_MODE_POWERS.get(), JourneyModePowersScreen::new);
        ScreenManager.registerFactory(JOURNEY_MODE_RESEARCH.get(), JourneyModeResearchScreen::new);
        //ScreenManager.registerFactory(JOURNEY_MODE_DUPLICATION.get(), JourneyModeDuplicationScreen::new);
    }
}
