package com.Deeakron.journey_mode;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

public class ItemList {
    public static String[] items;
    public static int[] caps;
    public static String[] categories;

    public ItemList(String file) throws IOException {
        //ResourceLocation location = new ResourceLocation("journey_mode", "duplication_menu/base_minecraft.json");
        BufferedReader readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(file), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = readIn.readLine()) != null) {
            sb.append(line);
        }
        JsonObject json = new JsonObject();//.addProperty("main", sb.toString());
        //json.addProperty("main", sb.toString());
        json = new JsonParser().parse(sb.toString()).getAsJsonObject();
        JsonElement[] items = new JsonElement[json.getAsJsonArray("items").size()];
        for (int i =0; i<json.getAsJsonArray("items").size(); i++){
            items[i] = json.getAsJsonArray("items").get(i);
        }
        journey_mode.LOGGER.info(items[0].getAsJsonObject().get("item"));
        journey_mode.LOGGER.info("bookmark");
        this.items = new String[items.length];
        this.caps = new int[items.length];
        this.categories = new String[0];
        for (int i = 0; i < items.length; i++) {
            this.items[i] = items[i].getAsJsonObject().get("item").toString();
            this.caps[i] = items[i].getAsJsonObject().get("count").getAsInt();
            boolean newCat = true;
            for (int j = 0; j < this.categories.length; j++) {
                if (this.categories[j].equals(items[i].getAsJsonObject().get("category").toString())) {
                    newCat = false;
                }
            }
            if (newCat == true) {
                this.categories = Arrays.copyOf(this.categories, this.categories.length + 1);
                this.categories[this.categories.length - 1] = items[i].getAsJsonObject().get("category").toString();
            }
        }
    }

    public String[] getItems() {
        return this.items;
    }

    public int[] getCaps() {
        return this.caps;
    }

    public String[] getCategories() {
        return this.categories;
    }

    public void updateList(ItemList list) {
        String[] newItems = list.getItems();
        int[] newCaps = list.getCaps();
        String[] newCategories = list.getCategories();
        for (int i = 0; i < newItems.length; i++) {
            boolean alreadyUsed = false;
            for (int j = 0; j < this.items.length; j++) {
                if (newItems[i].equals(this.items[j])) {
                    this.caps[j] = newCaps[i];
                    alreadyUsed = true;
                    break;
                }
            }
            if (!alreadyUsed) {
                this.items = Arrays.copyOf(this.items, this.items.length + 1);
                this.caps = Arrays.copyOf(this.caps, this.caps.length + 1);
                this.items[this.items.length - 1] = newItems[i];
                this.caps[this.caps.length - 1] = newCaps[i];
            }
        }
        for (int i = 0; i < newCategories.length; i++) {
            boolean newCat = true;
            for (int j = 0; j < this.categories.length; j++) {
                if (newCategories[i].equals(this.categories[j])) {
                    newCat = false;
                }
            }
            if (newCat) {
                this.categories = Arrays.copyOf(this.categories, this.categories.length + 1);
                this.categories[this.categories.length - 1] = newCategories[i];
            }
        }
    }
}
