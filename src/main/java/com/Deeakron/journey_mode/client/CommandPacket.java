package com.Deeakron.journey_mode.client;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CommandPacket {
    private final String data;
    public CommandPacket(PacketBuffer buf) {
        this.data = buf.readString();
    }

    public CommandPacket(String data) {
        this.data = data;
    }

    public void encode(PacketBuffer buf){
        buf.writeString(data);
    }

    public static CommandPacket decode(PacketBuffer buf) {
        return new CommandPacket(buf.readString());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        String command = data;
        ServerWorld serverWorld = context.get().getSender().getServerWorld();
        MinecraftServer server = context.get().getSender().getServer();
        CommandSource source = new CommandSource(ICommandSource.DUMMY, Vector3d.copyCentered(context.get().getSender().getPosition()), Vector2f.ZERO, (ServerWorld) serverWorld, 2, context.get().getSender().getName().getString(), context.get().getSender().getName(), server, (Entity) null);
        if (command.equals("dawn")) {
            server.getCommandManager().handleCommand(source, "time set day");
        } else if (command.equals("noon")) {
            server.getCommandManager().handleCommand(source, "time set noon");
        } else if (command.equals("dusk")) {
            server.getCommandManager().handleCommand(source, "time set night");
        } else if (command.equals("midnight")) {
            server.getCommandManager().handleCommand(source, "time set midnight");
        } else if (command.equals("freeze")) {
            server.getCommandManager().handleCommand(source, "gamerule doDaylightCycle false");
        } else if (command.equals("unfreeze")) {
            server.getCommandManager().handleCommand(source, "gamerule doDaylightCycle true");
        } else if (command.equals("clear")) {
            server.getCommandManager().handleCommand(source, "weather clear 1800");
        } else if (command.equals("rain")) {
            server.getCommandManager().handleCommand(source, "weather rain 1800");
        } else if (command.equals("storm")) {
            server.getCommandManager().handleCommand(source, "weather thunder 1800");
        } else if (command.equals("normal_speed")) {
            server.getCommandManager().handleCommand(source, "gamerule randomTickSpeed 3");
        } else if (command.equals("double_speed")) {
            server.getCommandManager().handleCommand(source, "gamerule randomTickSpeed 60");
        } else if (command.equals("quadruple_speed")) {
            server.getCommandManager().handleCommand(source, "gamerule randomTickSpeed 120");
        } else if (command.equals("octuple_speed")) {
            server.getCommandManager().handleCommand(source, "gamerule randomTickSpeed 240");
        } else if (command.equals("enable_spawn")) {
            server.getCommandManager().handleCommand(source, "gamerule doMobSpawning true");
        } else if (command.equals("disable_spawn")) {
            server.getCommandManager().handleCommand(source, "gamerule doMobSpawning false");
        } else if (command.equals("enable_grief")) {
            server.getCommandManager().handleCommand(source, "gamerule mobGriefing true");
        } else if (command.equals("disable_grief")) {
            server.getCommandManager().handleCommand(source, "gamerule mobGriefing false");
        } else if (command.equals("enable_god_mode")) {
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setPlayer(context.get().getSender().getUniqueID());
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(true);
        } else if (command.equals("disable_god_mode")) {
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setPlayer(context.get().getSender().getUniqueID());
            context.get().getSender().getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode()).setGodMode(false);
        } else if (command.equals("lose_inv")) {
            server.getCommandManager().handleCommand(source, "gamerule keepInventory false");
        } else if (command.equals("keep_inv")) {
            server.getCommandManager().handleCommand(source, "gamerule keepInventory true");
        }
        context.get().setPacketHandled(true);
    }
}
