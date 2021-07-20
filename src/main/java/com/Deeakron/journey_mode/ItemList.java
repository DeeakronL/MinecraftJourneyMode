package com.Deeakron.journey_mode;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

public class ItemList {
    public static String[] items;
    public static int[] caps;

    public ItemList() {
        ResourceLocation location = new ResourceLocation("journey_mode", "duplication_menu/base_minecraft.json");
        BufferedReader readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("data/journey_mode/duplication_menu/base_minecraft.json"), StandardCharsets.UTF_8));
    }
}
