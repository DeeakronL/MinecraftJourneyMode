package com.Deeakron.journey_mode.client.event;

import net.minecraftforge.eventbus.api.Event;

public class MenuSwitchEvent extends Event {
    public String player;
    public String menuType;

    public MenuSwitchEvent(String player, String menuType) {
        this.player = player;
        this.menuType = menuType;
    }
}
