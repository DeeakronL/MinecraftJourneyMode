package com.Deeakron.journey_mode.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class UnobtainConfig {
    public static ForgeConfigSpec.BooleanValue use_unobtainable;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client) {
        server.comment("Unobtainables Config");

        use_unobtainable = server
                .comment("Whether or not to allow unobtainable items to be obtained.")
                .define("unobtain.use_unobtainable", true);
    }
}
