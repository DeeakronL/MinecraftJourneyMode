package com.Deeakron.journey_mode.util;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class JourneyGameModeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> journeyGameModeCommand
                = Commands.literal("journeymode")
                .then(Commands.literal("on")
                    .executes(JourneyGameModeCommand::addJM)
                    )
                .then(Commands.literal("off")
                    .executes(JourneyGameModeCommand::removeJM)
                    );
        dispatcher.register(journeyGameModeCommand);
    }

    static int addJM(CommandContext<CommandSourceStack> commandContext){
        if(commandContext.getSource().getEntity() instanceof ServerPlayer){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            cap.setJourneyMode(true);
        }

        return 1;
    }

    static int removeJM(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        if(commandContext.getSource().getEntity() instanceof  ServerPlayer){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            if (cap.getJourneyMode()){
                cap.setJourneyMode(false);
            }
            if (commandContext.getSource().getPlayerOrException().isCreative() || commandContext.getSource().getPlayerOrException().isSpectator()) {
                cap.setGodMode(true);
            } else {
                cap.setGodMode(false);
            }


        }
        return 1;
    }
}
