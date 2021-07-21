package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.capabilities.IEntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapability;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

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

    static int confirmMessage(CommandContext<CommandSource> commandContext){
        journey_mode.LOGGER.info("successful command");
        if(commandContext.getSource().getEntity() instanceof ServerPlayerEntity){
            journey_mode.LOGGER.info("is player");
            journey_mode.LOGGER.info(commandContext.getSource().getEntity().getCapability(JMCapability.JOURNEY_MODE_CAPABILITY, null));
            //cap.get;
        }

        return 1;
    }
}
