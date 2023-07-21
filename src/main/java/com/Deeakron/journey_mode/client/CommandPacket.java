package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CommandPacket {
    private final String data;
    public CommandPacket(FriendlyByteBuf buf) {
        this.data = buf.readUtf();
    }

    public CommandPacket(String data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeUtf(data);
    }

    public static CommandPacket decode(FriendlyByteBuf buf) {
        return new CommandPacket(buf.readUtf());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        String command = data;
        ServerLevel ServerLevel = context.get().getSender().getLevel();
        MinecraftServer server = context.get().getSender().getServer();
        CommandSourceStack source = new CommandSourceStack(CommandSource.NULL, Vec3.atCenterOf(context.get().getSender().blockPosition()), Vec2.ZERO, (ServerLevel) ServerLevel, 2, context.get().getSender().getName().getString(), context.get().getSender().getName(), server, (Entity) null);
        if (command.equals("dawn")) {
            server.getCommands().performPrefixedCommand(source, "time set day");
        } else if (command.equals("noon")) {
            server.getCommands().performPrefixedCommand(source, "time set noon");
        } else if (command.equals("dusk")) {
            server.getCommands().performPrefixedCommand(source, "time set night");
        } else if (command.equals("midnight")) {
            server.getCommands().performPrefixedCommand(source, "time set midnight");
        } else if (command.equals("freeze")) {
            server.getCommands().performPrefixedCommand(source, "gamerule doDaylightCycle false");
        } else if (command.equals("unfreeze")) {
            server.getCommands().performPrefixedCommand(source, "gamerule doDaylightCycle true");
        } else if (command.equals("clear")) {
            server.getCommands().performPrefixedCommand(source, "weather clear 1800");
        } else if (command.equals("rain")) {
            server.getCommands().performPrefixedCommand(source, "weather rain 1800");
        } else if (command.equals("storm")) {
            server.getCommands().performPrefixedCommand(source, "weather thunder 1800");
        } else if (command.equals("normal_speed")) {
            server.getCommands().performPrefixedCommand(source, "gamerule randomTickSpeed 3");
        } else if (command.equals("double_speed")) {
            server.getCommands().performPrefixedCommand(source, "gamerule randomTickSpeed 60");
        } else if (command.equals("quadruple_speed")) {
            server.getCommands().performPrefixedCommand(source, "gamerule randomTickSpeed 120");
        } else if (command.equals("octuple_speed")) {
            server.getCommands().performPrefixedCommand(source, "gamerule randomTickSpeed 240");
        } else if (command.equals("enable_spawn")) {
            server.getCommands().performPrefixedCommand(source, "gamerule doMobSpawning true");
        } else if (command.equals("disable_spawn")) {
            server.getCommands().performPrefixedCommand(source, "gamerule doMobSpawning false");
        } else if (command.equals("enable_grief")) {
            server.getCommands().performPrefixedCommand(source, "gamerule mobGriefing true");
        } else if (command.equals("disable_grief")) {
            server.getCommands().performPrefixedCommand(source, "gamerule mobGriefing false");
        } else if (command.equals("enable_god_mode")) {
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setPlayer(context.get().getSender().getUUID());
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(true);
        } else if (command.equals("disable_god_mode")) {
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setPlayer(context.get().getSender().getUUID());
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(false);
        } else if (command.equals("lose_inv")) {
            server.getCommands().performPrefixedCommand(source, "gamerule keepInventory false");
        } else if (command.equals("keep_inv")) {
            server.getCommands().performPrefixedCommand(source, "gamerule keepInventory true");
        }
        context.get().setPacketHandled(true);
    }
}
