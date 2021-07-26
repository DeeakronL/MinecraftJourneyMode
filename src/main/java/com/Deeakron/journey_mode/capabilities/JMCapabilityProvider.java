package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JMCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final EntityJourneyMode jm = new EntityJourneyMode();
    //public static final ResourceLocation ID = new ResourceLocation(journey_mode.MODID, "jm");
    private final LazyOptional<EntityJourneyMode> implContainer;
    private final LazyOptional<IEntityJourneyMode> jMOptional = LazyOptional.of(() -> jm);
    public static final Capability<EntityJourneyMode> INSTANCE = null;

    public void invalidate() {
        jMOptional.invalidate();
    }

    public JMCapabilityProvider(ServerPlayerEntity object) {
        this.implContainer = LazyOptional.of(() -> new EntityJourneyMode());
        journey_mode.LOGGER.info("Attached?");
    }

    public JMCapabilityProvider() {
        journey_mode.LOGGER.info("Attached?");
        this.implContainer = LazyOptional.of(() -> new EntityJourneyMode());
    }

    @Override
    public CompoundNBT serializeNBT() {
        if(INSTANCE == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) INSTANCE.writeNBT(jm, null);
        }
        //journey_mode.LOGGER.info("look at me the provider and serializeNBT");
        //return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        journey_mode.LOGGER.info("huh? what?");
        //return INSTANCE == cap ? jMOptional.cast() : LazyOptional.empty();
        return jMOptional.cast();
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IEntityJourneyMode.class, new Storage(), EntityJourneyMode::new);
    }

    public static class Storage implements  Capability.IStorage<IEntityJourneyMode> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IEntityJourneyMode> capability, IEntityJourneyMode instance, Direction side) {
            journey_mode.LOGGER.info("look at me the provider and writeNBT");
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("mode", 0);
            return tag;
        }

        @Override
        public void readNBT(Capability<IEntityJourneyMode> capability, IEntityJourneyMode instance, Direction side, INBT nbt) {

        }
    }
}
