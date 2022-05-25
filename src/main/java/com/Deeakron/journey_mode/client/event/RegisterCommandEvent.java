package com.Deeakron.journey_mode.client.event;

import com.Deeakron.journey_mode.util.JourneyGameModeCommand;
import com.Deeakron.journey_mode.util.JMResearchCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCommandEvent {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        JourneyGameModeCommand.register(commandDispatcher);
        JMResearchCommand.register(commandDispatcher);
    }
}
