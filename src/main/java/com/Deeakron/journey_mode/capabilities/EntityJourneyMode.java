package com.Deeakron.journey_mode.capabilities;

import com.Deeakron.journey_mode.journey_mode;

public class EntityJourneyMode implements IEntityJourneyMode{
    private boolean mode;

    public EntityJourneyMode() {

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
}
