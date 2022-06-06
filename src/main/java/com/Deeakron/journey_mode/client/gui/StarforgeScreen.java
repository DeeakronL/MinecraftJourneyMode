package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.container.StarforgeContainer;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class StarforgeScreen  extends AbstractContainerScreen<StarforgeContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_starforge.png");

    public StarforgeScreen(StarforgeContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);

        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(PoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        this.blit(PoseStack, this.leftPos + 79, this.topPos + 35, 176, 14, this.menu.getSmeltProgressionScaled(), 17);

        this.blit(PoseStack, this.leftPos + 57, this.topPos + 36 + (12 - this.menu.getFuelUsageScaled()), 176, (12 - this.menu.getFuelUsageScaled()), 14, this.menu.getFuelUsageScaled());
    }

    @Override
    protected void renderLabels(PoseStack PoseStack, int mouseX, int mouseY) {
        this.font.draw(PoseStack, this.title.getString(), 8.0f, 1.0f, 15921906);
        this.font.draw(PoseStack, this.InventoryTitle.getString(), 89.0f, 69.0f, 15921906);
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }
}
