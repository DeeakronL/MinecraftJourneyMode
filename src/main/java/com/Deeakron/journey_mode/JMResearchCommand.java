package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;

public class JMResearchCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> jMResearchCommand
                = Commands.literal("research")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("grant")
                                .then(Commands.argument("item", ItemArgument.item())
                                        .executes(JMResearchCommand::grantResearch)
                                )
                        )
                        .then(Commands.literal("revoke")
                                .then(Commands.argument("item", ItemArgument.item())
                                        .executes(JMResearchCommand::revokeResearch)
                                )
                        )

                );
        dispatcher.register(jMResearchCommand);
    }

    static int grantResearch(CommandContext<CommandSource> commandContext){
        Item item = ItemArgument.getItem(commandContext, "item").getItem();
        if(commandContext.getSource().getEntity() instanceof ServerPlayerEntity){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            //journey_mode.LOGGER.info(item.getRegistryName().toString());
            int[] values = cap.getResearch("\"" + item.getRegistryName().toString() + "\"");
            int remaining = values[1];
            String[] name = new String[1];
            name[0] = "\"" + item.getRegistryName().toString() + "\"";
            int[] num = new int[1];
            num[0] = remaining;
            cap.updateResearch(name, num, false, cap.getPlayer(), null);
        }
        return 1;
    }

    static int revokeResearch(CommandContext<CommandSource> commandContext){
        Item item = ItemArgument.getItem(commandContext, "item").getItem();
        if(commandContext.getSource().getEntity() instanceof ServerPlayerEntity){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            String[] name = new String[1];
            name[0] = "\"" + item.getRegistryName().toString() + "\"";
            cap.removeResearchProgress(name);
        }
        return 1;
    }
}
