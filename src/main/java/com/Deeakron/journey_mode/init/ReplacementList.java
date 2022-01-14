package com.Deeakron.journey_mode.init;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReplacementList {
    public String[] originals;
    public String[] replacements;

    public ReplacementList(String file) throws IOException {
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
        for (int i = 0; i < json.getAsJsonArray("items").size(); i++) {
            items[i] = json.getAsJsonArray("items").get(i);
        }
        this.originals = new String[items.length];
        this.replacements = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            this.originals[i] = items[i].getAsJsonObject().get("original").toString();
            this.replacements[i] = items[i].getAsJsonObject().get("new").toString();
        }
    }

    public void UpdateList(ReplacementList list) {
        String[] newOriginals = list.getOriginals();
        String[] newReplacements = list.getReplacements();
        this.originals = Arrays.copyOf(this.originals, this.originals.length + newOriginals.length);
        this.replacements = Arrays.copyOf(this.replacements, this.replacements.length + newReplacements.length);
        for (int i = 0; i < newOriginals.length; i++) {
            this.originals[this.originals.length + i] = newOriginals[i];
            this.replacements[this.replacements.length + i] = newReplacements[i];
        }
    }

    public String[] getOriginals() {
        return this.originals;
    }

    public String[] getReplacements() {
        return this.replacements;
    }
}
