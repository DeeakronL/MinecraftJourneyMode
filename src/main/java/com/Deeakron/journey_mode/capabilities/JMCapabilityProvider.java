package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class JMCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final EntityJourneyMode jm = new EntityJourneyMode();
    private final LazyOptional<EntityJourneyMode> implContainer;
    private final LazyOptional<IEntityJourneyMode> jMOptional = LazyOptional.of(() -> jm);

    public static final Capability<EntityJourneyMode> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    public void invalidate() {
        jMOptional.invalidate();
    }

    public JMCapabilityProvider(ServerPlayer object) {
        this.implContainer = LazyOptional.of(() -> new EntityJourneyMode());
    }

    public JMCapabilityProvider() {
        this.implContainer = LazyOptional.of(() -> new EntityJourneyMode());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("mode", jm.getJourneyMode());
        jm.getResearchList().getList().forEach((k,v) -> tag.putInt(k, v[0]));
        tag.putBoolean("godMode", jm.getGodMode());
        if(jm.getPlayer() != null){
            tag.putUUID("player", jm.getPlayer());
        }

        return tag;
    }


    // using jm instead of instance may cause issues, if there are problems check there
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        boolean mode = nbt.getBoolean("mode");
        jm.setJourneyMode(mode);
        String[] items = journey_mode.list.getItems();
        String[][] replacements;
        int[] counts = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            if (journey_mode.doReplace) {
                journey_mode.LOGGER.info("hey!");
                boolean wasReplaced = false;
                for (int j = 0; j < journey_mode.replacementList.getReplacements().length; j++) {
                    if (items[i].equals(journey_mode.replacementList.getReplacements()[j])) {
                        wasReplaced = true;
                        int newCount = nbt.getInt(journey_mode.replacementList.getOriginals()[j]);
                        if (newCount == 0) {
                            newCount = nbt.getInt(items[i]);
                        }
                        counts[i] = newCount;
                        break;
                    }
                }
                if (!wasReplaced) {
                    counts[i] = nbt.getInt(items[i]);
                }
            } else {
                counts[i] = nbt.getInt(items[i]);
            }
        }
        jm.updateResearch(items, counts, true, null);
        boolean godMode = nbt.getBoolean("godMode");
        try {
            UUID player = nbt.getUUID("player");
            if(player != null){
                jm.setPlayer(player);
                jm.setGodMode(godMode);
            }
        } catch (NullPointerException e) {

        }

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return jMOptional.cast();
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IEntityJourneyMode.class);
    }

    /* No longer necessary?
    public static class Storage implements  Capability.IStorage<IEntityJourneyMode> {

        @Nullable
        @Override
        public Tag writeNBT(Capability<IEntityJourneyMode> capability, IEntityJourneyMode instance, Direction side) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("mode", instance.getJourneyMode());
            instance.getResearchList().getList().forEach((k,v) -> tag.putInt(k, v[0]));
            tag.putBoolean("godMode", instance.getGodMode());
            if(instance.getPlayer() != null){
                tag.putUUID("player", instance.getPlayer());
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
            instance.updateResearch(items, counts, true, null);
            boolean godMode = ((CompoundNBT) nbt).getBoolean("godMode");
            try {
                UUID player = ((CompoundNBT) nbt).getUUID("player");
                if(player != null){
                    instance.setPlayer(player);
                    instance.setGodMode(godMode);
                }
            } catch (NullPointerException e) {

            }

        }
    }*/
}
