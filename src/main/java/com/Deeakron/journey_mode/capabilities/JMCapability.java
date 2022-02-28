package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.NBTTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

//
//
// this whole file might be redundant, look into deleting later
//
//

public class JMCapability {

    @CapabilityInject(IEntityJourneyMode.class)
    public static Capability<? extends Object> JOURNEY_MODE_CAPABILITY = null;
    //public Capability<IEntityJourneyMode> JOURNEY_MODE_CAPABILITY = null;

    public static void register() {
        //journey_mode.LOGGER.info(IEntityJourneyMode.class);
        //journey_mode.LOGGER.info(new Storage());
        //journey_mode.LOGGER.info(EntityJourneyMode::new);
        CapabilityManager.INSTANCE.register(IEntityJourneyMode.class, new Storage(), EntityJourneyMode::new);
    }
    public static class Storage implements  Capability.IStorage<IEntityJourneyMode> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IEntityJourneyMode> capability, IEntityJourneyMode instance, Direction side) {

            return null;
        }

        @Override
        public void readNBT(Capability<IEntityJourneyMode> capability, IEntityJourneyMode instance, Direction side, INBT nbt) {

        }
    }

}
