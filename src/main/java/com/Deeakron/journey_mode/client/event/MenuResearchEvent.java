package com.Deeakron.journey_mode.client.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

public class MenuResearchEvent extends Event {
    public String item;
    public int count;
    public UUID player;
    public ItemStack itemStack;

    public MenuResearchEvent(String item, int count, UUID player, ItemStack itemStack) {
        this.item = item;
        this.count = count;
        this.player = player;
        this.itemStack = itemStack;
    }

    public String getItem() {
        return this.item;
    }

    public int getCount() {
        return this.count;
    }

    public UUID getPlayer() {
        return player;
    }

    public ItemStack getItemStack() { return itemStack;}
}
