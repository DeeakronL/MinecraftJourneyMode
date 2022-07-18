package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.init.ResearchList;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IEntityJourneyMode {
    void setJourneyMode(boolean mode);

    boolean getJourneyMode();

    ResearchList getResearchList();

    void updateResearch(String[] items, int[] counts, boolean isGenerating, UUID player, ItemStack... itemObject);

    void addNewResearch(String[] items, int[] caps);

    void removeResearchProgress(String[] items);

    int[] getResearch(String key);

    void setGodMode(boolean mode);

    boolean getGodMode();

    void setPlayer(UUID player);

    UUID getPlayer();

    void copyForRespawn(EntityJourneyMode old);
}
