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
import java.util.List;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

public class ItemList {
    public static String[] items;
    public static int[] caps;

    public ItemList() throws IOException {
        ResourceLocation location = new ResourceLocation("journey_mode", "duplication_menu/base_minecraft.json");
        BufferedReader readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("data/journey_mode/duplication_menu/base_minecraft.json"), StandardCharsets.UTF_8));
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
        for (int i = 0; i < items.length; i++) {
            this.items[i] = items[i].getAsJsonObject().get("item").toString();
            this.caps[i] = items[i].getAsJsonObject().get("count").getAsInt();
        }
    }

    public String[] getItems() {
        return this.items;
    }

    public int[] getCaps() {
        return this.caps;
    }
}
