package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.client.gui.ResearchToast;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ResearchList {
    //hash is stored with the key being the item name (i.e. "minecraft:stone") followed by an array with the player's current progress towards researching that item and the "cap" of how much is needed
    private HashMap<String, int[]> research;

    public ResearchList(String[] items, int[] caps) {
        this.research = new HashMap<String, int[]>();
        for (int i = 0; i < items.length; i++) {
            this.research.put(items[i], new int[]{0, caps[i]});
        }
    }

    public void updateCount(String[] items, int[] counts, boolean isGenerating, UUID player, ItemStack... itemObject) {
        for (int i = 0; i < items.length; i++) {
            if (this.research.get(items[i]) != null) {
                int[] data = this.research.get(items[i]);
                int[] newData = data.clone();
                if (data[0] >= data[1]) {
                    //already hit the cap
                    if (counts[i] < 0) {
                        newData[0] = data[0] + counts[i];
                    } else {
                        newData[0] = data[1];
                    }
                } else if (data[0] + counts[i] >= data[1]) {
                    //hit the cap
                    newData[0] = data[1];
                    if (!isGenerating) {
                        if(itemObject != null) {
                            Minecraft.getInstance().getToastGui().add(new ResearchToast(itemObject[i].copy()));
                        }

                    }
                } else {
                    //haven't hit the cap
                    newData[0] = data[0] + counts[i];
                }
                this.research.remove(items[i]);
                this.research.put(items[i], newData);
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
}
