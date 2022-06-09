package com.Deeakron.journey_mode.client.event;

import net.minecraftforge.eventbus.api.Event;

public class CloseDuplicationMenuEvent extends Event {
    public String mode;
    public String player;


    public CloseDuplicationMenuEvent(String mode, String player) {
        this.mode = mode;
        this.player = player;
    }
}
