package com.Deeakron.journey_mode.data.recipebook;

import com.Deeakron.journey_mode.capabilities.EntityJourneyMode;
import com.Deeakron.journey_mode.capabilities.JMCapabilityProvider;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.*;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class JMRecipeBookPage extends RecipeBookPage {
    private final List<RecipeWidget> buttons = Lists.newArrayListWithCapacity(20);
    private RecipeWidget hoveredButton;
    private final RecipeOverlayGui overlay = new RecipeOverlayGui();
    private Minecraft minecraft;
    private final List<IRecipeUpdateListener> listeners = Lists.newArrayList();
    private List<RecipeList> recipeLists;
    private ToggleWidget forwardButton;
    private ToggleWidget backButton;
    private int totalPages;
    private int currentPage;
    private JMRecipeBook recipeBook;
    private IRecipe<?> lastClickedRecipe;
    private RecipeList lastClickedRecipeList;

    @Override
    public void init(Minecraft p_194194_1_, int p_194194_2_, int p_194194_3_) {
        this.minecraft = p_194194_1_;
        EntityJourneyMode cap = p_194194_1_.player.getCapability(JMCapabilityProvider.INSTANCE,null).orElse(new EntityJourneyMode());
        this.recipeBook = cap.getRecipeBook();

        for(int i = 0; i < this.buttons.size(); ++i) {
            this.buttons.get(i).setPosition(p_194194_2_ + 11 + 25 * (i % 5), p_194194_3_ + 31 + 25 * (i / 5));
        }

        this.forwardButton = new ToggleWidget(p_194194_2_ + 93, p_194194_3_ + 137, 12, 17, false);
        this.forwardButton.initTextureValues(1, 208, 13, 18, JMRecipeBookGui.RECIPE_BOOK);
        this.backButton = new ToggleWidget(p_194194_2_ + 38, p_194194_3_ + 137, 12, 17, true);
        this.backButton.initTextureValues(1, 208, 13, 18, JMRecipeBookGui.RECIPE_BOOK);
    }

    public void updateLists(List<RecipeList> p_194192_1_, boolean p_194192_2_) {
        this.recipeLists = p_194192_1_;
        this.totalPages = (int)Math.ceil((double)p_194192_1_.size() / 20.0D);
        if (this.totalPages <= this.currentPage || p_194192_2_) {
            this.currentPage = 0;
        }

        this.updateButtonsForPage();
    }

    private void updateButtonsForPage() {
        int i = 20 * this.currentPage;

        for(int j = 0; j < this.buttons.size(); ++j) {
            RecipeWidget recipewidget = this.buttons.get(j);
            if (i + j < this.recipeLists.size()) {
                RecipeList recipelist = this.recipeLists.get(i + j);
                recipewidget.func_203400_a(recipelist, this);
                recipewidget.visible = true;
            } else {
                recipewidget.visible = false;
            }
        }

        this.updateArrowButtons();
    }

    private void updateArrowButtons() {
        this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
        this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
    }

    public void func_238927_a_(MatrixStack p_238927_1_, int p_238927_2_, int p_238927_3_, int p_238927_4_, int p_238927_5_, float p_238927_6_) {
        if (this.totalPages > 1) {
            String s = this.currentPage + 1 + "/" + this.totalPages;
            int i = this.minecraft.fontRenderer.getStringWidth(s);
            this.minecraft.fontRenderer.drawString(p_238927_1_, s, (float)(p_238927_2_ - i / 2 + 73), (float)(p_238927_3_ + 141), -1);
        }

        this.hoveredButton = null;

        for(RecipeWidget recipewidget : this.buttons) {
            recipewidget.render(p_238927_1_, p_238927_4_, p_238927_5_, p_238927_6_);
            if (recipewidget.visible && recipewidget.isHovered()) {
                this.hoveredButton = recipewidget;
            }
        }

        this.backButton.render(p_238927_1_, p_238927_4_, p_238927_5_, p_238927_6_);
        this.forwardButton.render(p_238927_1_, p_238927_4_, p_238927_5_, p_238927_6_);
        this.overlay.render(p_238927_1_, p_238927_4_, p_238927_5_, p_238927_6_);
    }

    public void func_238926_a_(MatrixStack p_238926_1_, int p_238926_2_, int p_238926_3_) {
        if (this.minecraft.currentScreen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
            this.minecraft.currentScreen.func_243308_b(p_238926_1_, this.hoveredButton.getToolTipText(this.minecraft.currentScreen), p_238926_2_, p_238926_3_);
        }

    }

    public boolean func_198955_a(double p_198955_1_, double p_198955_3_, int p_198955_5_, int p_198955_6_, int p_198955_7_, int p_198955_8_, int p_198955_9_) {
        this.lastClickedRecipe = null;
        this.lastClickedRecipeList = null;
        if (this.overlay.isVisible()) {
            if (this.overlay.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
                this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
                this.lastClickedRecipeList = this.overlay.getRecipeList();
            } else {
                this.overlay.setVisible(false);
            }

            return true;
        } else if (this.forwardButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
            ++this.currentPage;
            this.updateButtonsForPage();
            return true;
        } else if (this.backButton.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
            --this.currentPage;
            this.updateButtonsForPage();
            return true;
        } else {
            for(RecipeWidget recipewidget : this.buttons) {
                if (recipewidget.mouseClicked(p_198955_1_, p_198955_3_, p_198955_5_)) {
                    if (p_198955_5_ == 0) {
                        this.lastClickedRecipe = recipewidget.getRecipe();
                        this.lastClickedRecipeList = recipewidget.getList();
                    } else if (p_198955_5_ == 1 && !this.overlay.isVisible() && !recipewidget.isOnlyOption()) {
                        this.overlay.func_201703_a(this.minecraft, recipewidget.getList(), recipewidget.x, recipewidget.y, p_198955_6_ + p_198955_8_ / 2, p_198955_7_ + 13 + p_198955_9_ / 2, (float)recipewidget.getWidth());
                    }

                    return true;
                }
            }

            return false;
        }
    }
}
