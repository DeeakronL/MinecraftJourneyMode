package com.Deeakron.journey_mode.data.recipebook;

import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.IMutableSearchTree;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

public class JMCUpdateRecipeBookStatusPacket  implements IPacket<IServerPlayNetHandler> {
    private JMRecipeBookCategory category;
    private boolean visible;
    private boolean filterCraftable;
    private final String player;

    public JMCUpdateRecipeBookStatusPacket(String player) {
        this.player = player;
    }

    @OnlyIn(Dist.CLIENT)
    public JMCUpdateRecipeBookStatusPacket(JMRecipeBookCategory category, boolean visible, boolean filterCraftable, String player) {
        this.category = category;
        this.visible = visible;
        this.filterCraftable = filterCraftable;
        this.player = player;
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

    public void handle(Supplier<NetworkEvent.Context> context) {
        /*PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.client);
        this.recipeManager.deserializeRecipes(packetIn.getRecipes());
        IMutableSearchTree<RecipeList> imutablesearchtree = this.client.getSearchTree(SearchTreeManager.RECIPES);
        imutablesearchtree.clear();
        ClientRecipeBook clientrecipebook = this.client.player.getRecipeBook();
        clientrecipebook.func_243196_a(this.recipeManager.getRecipes());
        clientrecipebook.getRecipes().forEach(imutablesearchtree::func_217872_a);
        imutablesearchtree.recalculate();
        net.minecraftforge.client.ForgeHooksClient.onRecipesUpdated(this.recipeManager);*/
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
