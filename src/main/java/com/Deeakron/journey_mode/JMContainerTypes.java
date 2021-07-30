package com.Deeakron.journey_mode;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMContainerTypes {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, journey_mode.MODID);

    public static final RegistryObject<ContainerType<JourneyModePowersContainer>> JOURNEY_MODE_POWERS = CONTAINER_TYPES
            .register("journey_mode_powers", () -> IForgeContainerType.create(JourneyModePowersContainer::new));
}
