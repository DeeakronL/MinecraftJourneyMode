package com.Deeakron.journey_mode.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NewFilesConfig {
    public static ForgeConfigSpec.BooleanValue use_other_files;
    public static ForgeConfigSpec.BooleanValue use_replacements;
    public static ForgeConfigSpec.BooleanValue use_removals;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client) {
        server.comment("Files Config");

        use_other_files = server
                .comment("Whether or not to allow user made lists. Needs a file called 'changes.json'.")
                .define("files.other_files", false);

        use_replacements = server
                .comment("Whether or not to check for replacements when IDs are updated. Needs a file called 'replacements.json'.")
                .define("files.replacements", false);

        use_removals = server
                .comment("Whether or not to check for item removals. Needs a file called 'removals.json'.")
                .define("files.removals", false);
    }
}
