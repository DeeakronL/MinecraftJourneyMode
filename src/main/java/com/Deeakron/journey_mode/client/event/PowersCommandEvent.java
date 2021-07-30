package com.Deeakron.journey_mode.client.event;

import net.minecraftforge.eventbus.api.Event;

public class PowersCommandEvent extends Event {
    public String command;

    public PowersCommandEvent(String command) {
        this.command = command;
    }
}
