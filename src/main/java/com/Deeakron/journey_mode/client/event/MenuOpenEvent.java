package com.Deeakron.journey_mode.client.event;

import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

public class MenuOpenEvent extends Event {
    public boolean freeze;
    public int tickSpeed;
    public boolean mobSpawn;
    public boolean mobGrief;
    public boolean godMode;
    public boolean keepInv;
    public UUID player;

    public MenuOpenEvent(boolean freeze, int tickSpeed, boolean mobSpawn, boolean mobGrief, boolean godMode, boolean keepInv, UUID player) {
        this.freeze = freeze;
        this.tickSpeed = tickSpeed;
        this.mobSpawn = mobSpawn;
        this.mobGrief = mobGrief;
        this.godMode = godMode;
        this.keepInv = keepInv;
        this.player = player;
    }
}
