package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.journey_mode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ItemList {
    public String[] items;
    public int[] caps;
    public String[] categories;
    public String[] catTypes;

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
        this.categories = new String[items.length];
        this.catTypes = new String[0];
        for (int i = 0; i < items.length; i++) {
            this.items[i] = items[i].getAsJsonObject().get("item").toString();
            this.caps[i] = items[i].getAsJsonObject().get("count").getAsInt();
            this.categories[i] = items[i].getAsJsonObject().get("category").getAsString();
            boolean newCat = true;
            for (int j = 0; j < this.catTypes.length; j++) {
                if (this.catTypes[j].equals(items[i].getAsJsonObject().get("category").toString())) {
                    newCat = false;
                }
            }
            if (newCat == true) {
                this.catTypes = Arrays.copyOf(this.catTypes, this.catTypes.length + 1);
                this.catTypes[this.catTypes.length - 1] = items[i].getAsJsonObject().get("category").toString();
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

    public String[] getCatTypes() {return this.catTypes;}

    public void updateList(ItemList list) {
        String[] newItems = list.getItems();
        int[] newCaps = list.getCaps();
        String[] newCategories = list.getCategories();
        String[] newCatTypes = list.getCatTypes();
        for (int i = 0; i < newItems.length; i++) {
            journey_mode.LOGGER.info("adding new item: " + newItems[i]);
            boolean alreadyUsed = false;
            for (int j = 0; j < this.items.length; j++) {
                if (newItems[i].equals(this.items[j])) {
                    this.caps[j] = newCaps[i];
                    this.categories[j] = newCategories[i];
                    alreadyUsed = true;
                    journey_mode.LOGGER.info(newItems[i] + " was already used! Updated.");
                    break;
                }
            }
            if (!alreadyUsed) {
                this.items = Arrays.copyOf(this.items, this.items.length + 1);
                this.caps = Arrays.copyOf(this.caps, this.caps.length + 1);
                this.categories = Arrays.copyOf(this.categories, this.categories.length + 1);
                this.items[this.items.length - 1] = newItems[i];
                this.caps[this.caps.length - 1] = newCaps[i];
                this.categories[this.categories.length - 1] = newCategories[i];
            }
        }
        for (int i = 0; i < newCatTypes.length; i++) {
            boolean newCat = true;
            for (int j = 0; j < this.catTypes.length; j++) {
                if (newCatTypes[i].equals(this.catTypes[j])) {
                    newCat = false;
                }
            }
            if (newCat) {
                this.catTypes = Arrays.copyOf(this.catTypes, this.catTypes.length + 1);
                this.catTypes[this.catTypes.length - 1] = newCatTypes[i];
            }
        }
    }

    public void removeItem(String[] items) {
        String[] newItemArray = new String[0];
        int[] newCapsArray = new int[0];
        String[] newCategoriesArray = new String[0];
        for (int i = 0; i < this.items.length; i++) {
            boolean wasRemoved = false;
            for (int j = 0; j < items.length; j++) {
                if (this.items[i].equals(items[j])) {
                    wasRemoved = true;
                    break;
                }
            }
            if (!wasRemoved) {
                newItemArray = Arrays.copyOf(newItemArray, newItemArray.length + 1);
                newCapsArray = Arrays.copyOf(newCapsArray, newCapsArray.length + 1);
                newCategoriesArray = Arrays.copyOf(newCategoriesArray, newCategoriesArray.length + 1);
                newItemArray[newItemArray.length - 1] = this.items[i];
                newCapsArray[newCapsArray.length - 1] = this.caps[i];
                newCategoriesArray[newCategoriesArray.length - 1] = this.categories[i];
            }
        }
        this.items = newItemArray;
        this.caps = newCapsArray;
        this.categories = newCategoriesArray;

        String[] newCatTypes = new String[0];

        for (int i = 0; i < this.categories.length; i++) {
            boolean newCat = true;
            for (int j = 0; j < newCatTypes.length; j++) {
                if (this.categories[i].equals(newCatTypes[j])) {
                    newCat = false;
                }
            }
            if (newCat) {
                newCatTypes = Arrays.copyOf(newCatTypes, newCatTypes.length + 1);
                newCatTypes[newCatTypes.length - 1] = this.categories[i];
            }
        }
        this.catTypes = newCatTypes;
    }
}
