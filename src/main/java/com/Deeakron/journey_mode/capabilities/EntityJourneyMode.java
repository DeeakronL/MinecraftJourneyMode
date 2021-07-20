package com.Deeakron.journey_mode.capabilities;

public class EntityJourneyMode implements IEntityJourneyMode{
    private boolean mode;

    public EntityJourneyMode() {

    }

    @Override
    public void setJourneyMode(boolean mode) {
        this.mode = mode;
    }

    @Override
    public boolean getJourneyMode() {
        return this.mode;
    }
}
