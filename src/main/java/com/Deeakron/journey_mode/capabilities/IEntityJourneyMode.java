package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.ResearchList;

import java.util.UUID;

public interface IEntityJourneyMode {
    void setJourneyMode(boolean mode);

    boolean getJourneyMode();

    ResearchList getResearchList();

    void updateResearch(String[] items, int[] counts, boolean isGenerating, UUID player);

    void addNewResearch(String[] items, int[] caps);

    int[] getResearch(String key);

    void setGodMode(boolean mode);

    boolean getGodMode();

    void setPlayer(UUID player);

    UUID getPlayer();
}
