package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.ResearchList;

public interface IEntityJourneyMode {
    void setJourneyMode(boolean mode);

    boolean getJourneyMode();

    ResearchList getResearchList();

    void updateResearch(String[] items, int[] counts);

    void addNewResearch(String[] items, int[] caps);

    int[] getResearch(String key);
}
