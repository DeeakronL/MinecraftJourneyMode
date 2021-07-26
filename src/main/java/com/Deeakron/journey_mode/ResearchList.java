package com.Deeakron.journey_mode;

import net.minecraft.util.ResourceLocation;

import java.io.InputStream;
import java.util.HashMap;

public class ResearchList {
    //hash is stored with the key being the item name (i.e. "minecraft:stone") followed by an array with the player's current progress towards researching that item and the "cap" of how much is needed
    private HashMap<String, int[]> research;

    public ResearchList(String[] items, int[] caps) {
        ResourceLocation data = new ResourceLocation(journey_mode.MODID, "duplication_menu/base_minecraft.json");
        this.research = new HashMap<String, int[]>();
        journey_mode.LOGGER.info("currently creating!");
        for (int i = 0; i < items.length; i++) {
            journey_mode.LOGGER.info("the item is: " + items[i] + " with cap: " + caps[i]);
            this.research.put(items[i], new int[]{0, caps[i]});
            if (items[i].equals("\"minecraft:stone\"")) {
                journey_mode.LOGGER.info("getting item: " + this.research.get(items[i])[0]);
            }
        }
        //journey_mode.LOGGER.info("testing i guess: " + this.research.get("minecraft:stone"));
    }

    public void updateCount(String[] items, int[] counts) {
        for (int i = 0; i < items.length; i++) {
            if (this.research.get(items[i]) != null) {
                int[] data = this.research.get(items[i]);
                int[] newData = data.clone();
                if (data[0] + counts[i] >= data[1]) {
                    newData[0] = data[1];
                } else {
                    newData[0] = data[0] + counts[i];
                }
                this.research.remove(items[i]);
                this.research.put(items[i], newData);
            }
        }
    }

    public void addNewResearch(String[] items, int[] caps) {
        for (int i = 0; i < items.length; i++) {
            if (this.research.get(items[i]) == null) {
                this.research.put(items[i], new int[]{0, caps[i]});
            }
        }
    }
    public int[] get(String key) {
        int info = this.research.get(key)[0];
        int[] data = new int[]{this.research.get(key)[0],this.research.get(key)[1]};
        return data;
    }
}