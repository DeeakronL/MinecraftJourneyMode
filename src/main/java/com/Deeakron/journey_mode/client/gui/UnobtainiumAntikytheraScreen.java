package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.container.UnobtainiumAntikytheraContainer;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UnobtainiumAntikytheraScreen extends AbstractContainerScreen<UnobtainiumAntikytheraContainer> implements RecipeUpdateListener {
    private static final ResourceLocation ANTIKYTHERA_GUI_TEXTURES = new ResourceLocation(journey_mode.MODID,"textures/gui/jm_antikythera.png");
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
    private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();
    private boolean widthTooNarrow;

    public UnobtainiumAntikytheraScreen(UnobtainiumAntikytheraContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
        this.children.add(this.recipeBookGui);
        this.setInitialFocus(this.recipeBookGui);
        if(this.recipeBookGui.isVisible()) {
            this.recipeBookGui.toggleVisibility();
        }
        this.titleLabelX = 29;
    }

    public void tick() {
        super.tick();
        this.recipeBookGui.tick();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.renderBg(matrixStack, partialTicks, mouseX, mouseY);
            this.recipeBookGui.render(matrixStack, mouseX, mouseY, partialTicks);
        } else {
            this.recipeBookGui.render(matrixStack, mouseX, mouseY, partialTicks);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
            this.recipeBookGui.renderGhostRecipe(matrixStack, this.leftPos, this.topPos, true, partialTicks);
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
        this.recipeBookGui.renderTooltip(matrixStack, this.leftPos, this.topPos, mouseX, mouseY);
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(ANTIKYTHERA_GUI_TEXTURES);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookGui.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBookGui);
            return true;
        } else {
            return  super.mouseClicked(mouseX, mouseY, button);
        }
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.imageWidth) || mouseY >= (double)(guiTopIn + this.imageHeight);
        return this.recipeBookGui.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && flag;
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    public void recipesUpdated() {
        this.recipeBookGui.recipesUpdated();
    }

    public void removed() {
        this.recipeBookGui.removed();
        super.removed();
    }

    public RecipeBookGui getRecipeBookComponent() {
        return this.recipeBookGui;
    }
}
