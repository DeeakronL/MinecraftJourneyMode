package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.advancements.JMTriggers;
import com.Deeakron.journey_mode.client.gui.ResearchToast;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class ResearchList {
    //hash is stored with the key being the item name (i.e. "minecraft:stone") followed by an array with the player's current progress towards researching that item and the "cap" of how much is needed
    private HashMap<String, int[]> research;

    public ResearchList(String[] items, int[] caps) {
        //ResourceLocation data = new ResourceLocation(journey_mode.MODID, "duplication_menu/base_minecraft.json");
        this.research = new HashMap<String, int[]>();
        //journey_mode.LOGGER.info("currently creating!");
        for (int i = 0; i < items.length; i++) {
            //journey_mode.LOGGER.info("the item is: " + items[i] + " with cap: " + caps[i]);
            this.research.put(items[i], new int[]{0, caps[i]});
        }
        //journey_mode.LOGGER.info("testing i guess: " + this.research.get("minecraft:stone"));
    }

    public void updateCount(String[] items, int[] counts, boolean isGenerating, UUID player, ItemEntity... itemObject) {
        for (int i = 0; i < items.length; i++) {
            if (this.research.get(items[i]) != null) {
                int[] data = this.research.get(items[i]);
                int[] newData = data.clone();
                if (data[0] >= data[1]) {
                    //already hit the cap
                    newData[0] = data[1];
                } else if (data[0] + counts[i] >= data[1]) {
                    //hit the cap
                    newData[0] = data[1];
                    if (!isGenerating) {
                        journey_mode.LOGGER.info("Cap hit");
                        if(itemObject != null) {
                            Minecraft.getInstance().getToastGui().add(new ResearchToast(itemObject[0]));
                        }

                    }
                } else {
                    //haven't hit the cap
                    newData[0] = data[0] + counts[i];
                }
                this.research.remove(items[i]);
                this.research.put(items[i], newData);
                journey_mode.LOGGER.info("Updated count for " + items[i] + "! Remaining needed: " + (this.research.get(items[i])[1] - this.research.get(items[i])[0]));
            }
        }
    }

    public void removeResearchProgress(String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (this.research.get(items[i]) != null) {
                int[] data = this.research.get(items[i]);
                int[] newData = data.clone();
                newData[0] = 0;
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
        //int info = this.research.get(key)[0];
        int[] data = new int[]{this.research.get(key)[0],this.research.get(key)[1]};
        return data;
    }

    public HashMap<String, int[]> getList() {
        return this.research;
    }

    public boolean hasItem(String key) {
        if (this.research.get(key) == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean reachCap(String key) {
        if (this.research.get(key)[0] == this.research.get(key)[1]) {
            return true;
        } else {
            return false;
        }
    }

    //debug, remove later
    public void listItems() {
        this.getList().forEach((k,v) -> journey_mode.LOGGER.info("item: " + k + " with count " + v[0] + "/" + v[1]));
    }
}
