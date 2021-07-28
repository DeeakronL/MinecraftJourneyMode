package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.IEntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapability;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.impl.GameModeCommand;
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
                    .executes(JourneyGameModeCommand::addJM)
                    )
                .then(Commands.literal("journeyoff")
                    .executes(JourneyGameModeCommand::removeJM)
                    )
                /*.then(Commands.literal("creative")
                    .executes(JourneyGameModeCommand::removeJM)
                    )
                .then(Commands.literal("spectator")
                    .executes(JourneyGameModeCommand::removeJM)
                    )
                .then(Commands.literal("survival")
                    .executes((JourneyGameModeCommand::removeJM))
                )*/;
        dispatcher.register(journeyGameModeCommand);
    }

    static int addJM(CommandContext<CommandSource> commandContext){
        journey_mode.LOGGER.info("successful command");
        if(commandContext.getSource().getEntity() instanceof ServerPlayerEntity){
            journey_mode.LOGGER.info("is player");
            //String line = commandContext.getSource().getEntity().getCapability(JMCapability.JOURNEY_MODE_CAPABILITY, null);
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            journey_mode.LOGGER.info("journey mode is: " + cap.getJourneyMode());
            journey_mode.LOGGER.info(cap);
            cap.setJourneyMode(true);
            //cap.get;
        }

        return 1;
    }

    static int removeJM(CommandContext<CommandSource> commandContext){
        journey_mode.LOGGER.info("successful command");
        if(commandContext.getSource().getEntity() instanceof  ServerPlayerEntity){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            if (cap.getJourneyMode()){
                cap.setJourneyMode(false);
            }
            journey_mode.LOGGER.info(commandContext.getCommand().toString());


        }
        return 1;
    }
}
