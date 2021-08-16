package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.client.gui.JourneyModePowersScreen;
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
            .register("journey_mode_powers2", () -> IForgeContainerType.create(JourneyModePowersContainer::new));

    /*public static <T extends Container> RegistryObject<ContainerType<T>> registerContainer(String id, IContainerFactory<T> factory) {
        return CONTAINER_TYPES.register(id, () -> IForgeContainerType.create(factory));
    }*/

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens() {
        ScreenManager.registerFactory(JOURNEY_MODE_POWERS.get(), JourneyModePowersScreen::new);
    }
}
