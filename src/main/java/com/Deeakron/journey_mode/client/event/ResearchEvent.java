package com.Deeakron.journey_mode.client.event;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class ResearchEvent extends Event {
    public ItemEntity item;
    public ServerPlayer player;

    public ResearchEvent(ItemEntity item, ServerPlayer player) {this.item = item; this.player = player;}

    public ItemEntity getItem() {return this.item;};
    public ServerPlayer getEntity() {return this.player;};
}
