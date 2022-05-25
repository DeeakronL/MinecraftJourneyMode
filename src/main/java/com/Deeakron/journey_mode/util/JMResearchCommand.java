package com.Deeakron.journey_mode.util;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;

public class JMResearchCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> jMResearchCommand
                = Commands.literal("research")
                .requires((commandSource) -> commandSource.hasPermission(2))
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
                        .then(Commands.literal("change")
                                .then(Commands.argument("item", ItemArgument.item())
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(JMResearchCommand::changeResearch)
                                        )
                                )
                        )

                );
        dispatcher.register(jMResearchCommand);
    }

    static int grantResearch(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        Item item = ItemArgument.getItem(commandContext, "item").getItem();
        if(commandContext.getSource().getEntity() instanceof ServerPlayerEntity){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            int[] values = cap.getResearch("\"" + item.getRegistryName().toString() + "\"");
            int remaining = values[1];
            String[] name = new String[1];
            name[0] = "\"" + item.getRegistryName().toString() + "\"";
            int[] num = new int[1];
            num[0] = remaining;
            cap.updateResearch(name, num, false, cap.getPlayer(), ItemArgument.getItem(commandContext, "item").createItemStack(1,false));
        }
        return 1;
    }

    static int revokeResearch(CommandContext<CommandSource> commandContext){
        Item item = ItemArgument.getItem(commandContext, "item").getItem();
        if(commandContext.getSource().getEntity() instanceof ServerPlayerEntity){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            int[] values = cap.getResearch("\"" + item.getRegistryName().toString() + "\"");
            int progress = values[0];
            String[] name = new String[1];
            name[0] = "\"" + item.getRegistryName().toString() + "\"";
            int[] num = new int[1];
            num[0] = 0 - progress;
            cap.updateResearch(name, num, false, cap.getPlayer(), null);
        }
        return 1;
    }

    static int changeResearch(CommandContext<CommandSource> commandContext){
        Item item = ItemArgument.getItem(commandContext, "item").getItem();
        int count = IntegerArgumentType.getInteger(commandContext, "count");
        if(commandContext.getSource().getEntity() instanceof ServerPlayerEntity){
            EntityJourneyMode cap = commandContext.getSource().getEntity().getCapability(JMCapabilityProvider.INSTANCE, null).orElse(new EntityJourneyMode());
            int[] values = cap.getResearch("\"" + item.getRegistryName().toString() + "\"");
            if (count < 0){
                count = 0;
            } else if (count > values[1]) {
                count = values[1];
            }
            int change = count - values[0];
            String[] name = new String[1];
            name[0] = "\"" + item.getRegistryName().toString() + "\"";
            int[] num = new int[1];
            num[0] = change;
            cap.updateResearch(name, num, false, cap.getPlayer(), null);
        }
        return 1;
    }
}
