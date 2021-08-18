package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class JMCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final EntityJourneyMode jm = new EntityJourneyMode();
    //public static final ResourceLocation ID = new ResourceLocation(journey_mode.MODID, "jm");
    private final LazyOptional<EntityJourneyMode> implContainer;
    private final LazyOptional<IEntityJourneyMode> jMOptional = LazyOptional.of(() -> jm);
    @CapabilityInject(IEntityJourneyMode.class)
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
            journey_mode.LOGGER.info("look at me the provider and serializeNBT for null");
            return new CompoundNBT();

        } else {
            journey_mode.LOGGER.info("look at me the provider and serializeNBT");
            return (CompoundNBT) INSTANCE.writeNBT(jm, null);
        }
        //journey_mode.LOGGER.info("look at me the provider and serializeNBT");
        //return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(INSTANCE != null) {
            INSTANCE.readNBT(jm, null, nbt);
        }
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
            tag.putBoolean("mode", instance.getJourneyMode());
            instance.getResearchList().getList().forEach((k,v) -> tag.putInt(k, v[0]));
            tag.putBoolean("godMode", instance.getGodMode());
            if(instance.getPlayer() != null){
                tag.putUniqueId("player", instance.getPlayer());
            }

            return tag;
        }

        @Override
        public void readNBT(Capability<IEntityJourneyMode> capability, IEntityJourneyMode instance, Direction side, INBT nbt) {
            boolean mode = ((CompoundNBT) nbt).getBoolean("mode");
            instance.setJourneyMode(mode);
            String[] items = journey_mode.list.getItems();
            String[][] replacements;
            int[] counts = new int[items.length];
            for (int i = 0; i < items.length; i++) {
                if (journey_mode.doReplace) {
                    boolean wasReplaced = false;
                    for (int j = 0; j < journey_mode.replacementList.getReplacements().length; j++) {
                        if (items[i].equals(journey_mode.replacementList.getReplacements()[j])) {
                            wasReplaced = true;
                            int newCount = ((CompoundNBT) nbt).getInt(journey_mode.replacementList.getOriginals()[j]);
                            if (newCount == 0) {
                                newCount = ((CompoundNBT) nbt).getInt(items[i]);
                            }
                            counts[i] = newCount;
                            break;
                        }
                    }
                    if (!wasReplaced) {
                        counts[i] = ((CompoundNBT) nbt).getInt(items[i]);
                    }
                } else {
                    counts[i] = ((CompoundNBT) nbt).getInt(items[i]);
                }
            }
            instance.updateResearch(items, counts);
            boolean godMode = ((CompoundNBT) nbt).getBoolean("godMode");
            try {
                UUID player = ((CompoundNBT) nbt).getUniqueId("player");
                if(player != null){
                    //journey_mode.LOGGER.info("testing here i guess");
                    instance.setPlayer(player);
                    instance.setGodMode(godMode);
                }
            } catch (NullPointerException e) {

            }


            journey_mode.LOGGER.info("readNBT done");
        }
    }
}
