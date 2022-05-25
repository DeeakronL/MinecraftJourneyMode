package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.container.StarforgeContainer;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.matrix.MatrixStack;
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
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1, 1, 1, 1);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        this.blit(matrixStack, this.leftPos + 79, this.topPos + 35, 176, 14, this.menu.getSmeltProgressionScaled(), 17);

        this.blit(matrixStack, this.leftPos + 57, this.topPos + 36 + (12 - this.menu.getFuelUsageScaled()), 176, (12 - this.menu.getFuelUsageScaled()), 14, this.menu.getFuelUsageScaled());
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title.getString(), 8.0f, 1.0f, 15921906);
        this.font.draw(matrixStack, this.inventory.getDisplayName().getString(), 89.0f, 69.0f, 15921906);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
