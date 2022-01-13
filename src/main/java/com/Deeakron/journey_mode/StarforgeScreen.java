package com.Deeakron.journey_mode;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StarforgeScreen  extends ContainerScreen<StarforgeContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_starforge.png");

    public StarforgeScreen(StarforgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1, 1, 1, 1);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.blit(matrixStack, this.guiLeft + 79, this.guiTop + 35, 176, 14, this.container.getSmeltProgressionScaled(), 17);

        this.blit(matrixStack, this.guiLeft + 57, this.guiTop + 36 + (12 - this.container.getFuelUsageScaled()), 176, (12 - this.container.getFuelUsageScaled()), 14, this.container.getFuelUsageScaled());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        //super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.drawString(matrixStack, this.title.getString(), 8.0f, 1.0f, 15921906);
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 89.0f, 69.0f, 15921906);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
