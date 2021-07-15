package com.Deeakron.journey_mode;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class JourneyGameModeCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> journeyGameModeCommand
                = Commands.literal("gamemode")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.literal("journey")
                    .executes(JourneyGameModeCommand::confirmMessage)
                );
        dispatcher.register(journeyGameModeCommand);
    }

    static int confirmMessage(CommandContext<CommandSource> commandContext) {
        journey_mode.LOGGER.info("successful command");
        return 1;
    }
}
