package com.Deeakron.journey_mode;

import net.minecraft.util.ResourceLocation;

import java.io.InputStream;
import java.util.HashMap;

public class ResearchList {
    //hash is stored with the key being the item name (i.e. "minecraft:stone") followed by an array with the player's current progress towards researching that item and the "cap" of how much is needed
    private HashMap<String, int[]> research;

    public ResearchList() {
        ResourceLocation data = new ResourceLocation(journey_mode.MODID, "duplication_menu/base_minecraft.json");
        this.research = new HashMap<String, int[]>();
        journey_mode.LOGGER.info("currently creating!");
        this.research.put("minecraft:stone", new int[]{0,128});
        journey_mode.LOGGER.info(this.research.get("minecraft:stone")[0]);
        this.research.put("minecraft:dirt", new int[]{0,128});
        this.research.put("minecraft:polished_blackstone_brick_slab", new int[]{0,256});
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
        int[] data = new int[]{this.research.get(key)[0],this.research.get(key)[1]};
        return data;
    }
}
