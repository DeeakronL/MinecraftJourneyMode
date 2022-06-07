package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.client.gui.*;
import com.Deeakron.journey_mode.container.*;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMContainerTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, journey_mode.MODID);

    public static final RegistryObject<MenuType<JourneyModePowersContainer>> JOURNEY_MODE_POWERS = CONTAINER_TYPES
            .register("journey_mode_powers", () -> IForgeContainerType.create(JourneyModePowersContainer::new));

    public static final RegistryObject<MenuType<JourneyModeResearchContainer>> JOURNEY_MODE_RESEARCH = CONTAINER_TYPES
            .register("journey_mode_research", () -> IForgeContainerType.create(JourneyModeResearchContainer::new));
    public static final RegistryObject<MenuType<JourneyModeRecipesContainer>> JOURNEY_MODE_RECIPES = CONTAINER_TYPES
            .register("journey_mode_recipes", () -> IForgeContainerType.create(JourneyModeRecipesContainer::new));

    public static final RegistryObject<MenuType<UnobtainiumAntikytheraContainer>> UNOBTAINIUM_ANTIKYTHERA = CONTAINER_TYPES
            .register("unobtainium_antikythera", () -> IForgeContainerType.create(UnobtainiumAntikytheraContainer::new));

    public static final RegistryObject<MenuType<StarforgeContainer>> UNOBTAINIUM_STARFORGE = CONTAINER_TYPES
            .register("starforge", () -> IForgeContainerType.create(StarforgeContainer::new));

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens() {
        MenuScreens.register(JOURNEY_MODE_POWERS.get(), JourneyModePowersScreen::new);
        MenuScreens.register(JOURNEY_MODE_RESEARCH.get(), JourneyModeResearchScreen::new);
        MenuScreens.register(JOURNEY_MODE_RECIPES.get(), JourneyModeRecipesScreen::new);
        MenuScreens.register(UNOBTAINIUM_ANTIKYTHERA.get(), UnobtainiumAntikytheraScreen::new);
        MenuScreens.register(UNOBTAINIUM_STARFORGE.get(), StarforgeScreen::new);
    }
}
