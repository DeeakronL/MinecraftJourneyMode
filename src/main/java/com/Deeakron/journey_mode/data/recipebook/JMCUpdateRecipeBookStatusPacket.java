package com.Deeakron.journey_mode.data.recipebook;

import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class JMCUpdateRecipeBookStatusPacket  implements IPacket<IServerPlayNetHandler> {
    private JMRecipeBookCategory category;
    private boolean visible;
    private boolean filterCraftable;

    public JMCUpdateRecipeBookStatusPacket() {
    }

    @OnlyIn(Dist.CLIENT)
    public JMCUpdateRecipeBookStatusPacket(JMRecipeBookCategory category, boolean visible, boolean filterCraftable) {
        this.category = category;
        this.visible = visible;
        this.filterCraftable = filterCraftable;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.category = buf.readEnumValue(JMRecipeBookCategory.class);
        this.visible = buf.readBoolean();
        this.filterCraftable = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.category);
        buf.writeBoolean(this.visible);
        buf.writeBoolean(this.filterCraftable);
    }

    @Override
    public void processPacket(IServerPlayNetHandler handler) {

    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    /*public void processPacket(IServerPlayNetHandler handler) {
        handler.func_241831_a(this);
    }*/

    public JMRecipeBookCategory getCategory() {
        return this.category;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean shouldFilterCraftable() {
        return this.filterCraftable;
    }
}
