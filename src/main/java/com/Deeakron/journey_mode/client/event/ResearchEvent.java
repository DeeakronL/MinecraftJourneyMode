package com.Deeakron.journey_mode.client.event;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class ResearchEvent extends Event {
    public ItemEntity item;
    public ServerPlayerEntity player;

    public ResearchEvent(ItemEntity item, ServerPlayerEntity player) {this.item = item; this.player = player;}

    public ItemEntity getItem() {return this.item;};
    public ServerPlayerEntity getEntity() {return this.player;};
}
