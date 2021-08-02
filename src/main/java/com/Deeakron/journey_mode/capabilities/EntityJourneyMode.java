package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.ResearchList;
import com.Deeakron.journey_mode.journey_mode;

public class EntityJourneyMode implements IEntityJourneyMode{
    private boolean mode;
    private ResearchList research;
    private boolean godMode;

    public EntityJourneyMode() {
        this.mode = false;
        this.research = new ResearchList(journey_mode.list.getItems(), journey_mode.list.getCaps());
        this.godMode = false;
    }

    @Override
    public void setJourneyMode(boolean mode) {
        this.mode = mode;
        journey_mode.LOGGER.info("mode set!");
    }

    @Override
    public boolean getJourneyMode() {
        return this.mode;
    }

    @Override
    public ResearchList getResearchList() {
        return this.research;
    }

    @Override
    public int[] getResearch(String key) {
        return this.research.get(key);
    }

    @Override
    public void addNewResearch(String[] items, int[] caps) {
        this.research.addNewResearch(items, caps);
    }

    @Override
    public void updateResearch(String[] items, int[] counts) {
        this.research.updateCount(items, counts);
    }

    public void setResearchList(ResearchList research) {
        this.research = research;
    }

    public void setGodMode(boolean mode) {
        this.godMode = mode;
    }

    public boolean getGodMode() {
        return this.godMode;
    }
}
