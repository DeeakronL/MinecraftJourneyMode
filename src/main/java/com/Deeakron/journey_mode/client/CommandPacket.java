package com.Deeakron.journey_mode.client;

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
        }
        context.get().setPacketHandled(true);
    }
}
