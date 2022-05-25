package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.container.JourneyModeRecipesContainer;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class JourneyModeRecipesScreen extends AbstractContainerScreen<JourneyModeRecipesContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_antikythera_recipes.png");
    public static final Component POWERS_TAB = new TranslatableComponent("journey_mode.gui.tabs.powers");
    public static final Component RESEARCH_TAB = new TranslatableComponent("journey_mode.gui.tabs.research");
    public static final Component DUPLICATION_TAB = new TranslatableComponent("journey_mode.gui.tabs.duplication");
    public static final Component RECIPES_TAB = new TranslatableComponent("journey_mode.gui.tabs.recipes");
    public static final Component SHAPELESS_NOTIF = new TranslatableComponent("journey_mode.gui.recipes.shapeless");
    private boolean initialized;
    private int currentRecipe = 0;
    private int numRecipes;
    private boolean showRightButton = false;
    private boolean showLeftButton = false;
    private ResourceLocation[][] recipeItems;
    private boolean[] isShaped;
    private boolean topShaped = true;
    private boolean bottomShaped = true;


    public JourneyModeRecipesScreen(JourneyModeRecipesContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.initialized = false;
        this.inventoryLabelX = -1000;
        this.inventoryLabelY = -1000;
    }

    protected void init() {
        super.init();
        this.addButton(new JourneyModeRecipesScreen.PowersTab(this.leftPos -29, this.topPos + 21));
        this.addButton(new JourneyModeRecipesScreen.ResearchTab(this.leftPos -29, this.topPos + 50));
        this.addButton(new JourneyModeRecipesScreen.DuplicationTab(this.leftPos -29, this.topPos + 79));
        this.addButton(new JourneyModeRecipesScreen.RecipesTab(this.leftPos -29, this.topPos + 108));
        this.addButton(new JourneyModeRecipesScreen.ActualArrowButton(this.leftPos + 6, this.topPos + 73, false, this, 147, 234));
        this.addButton(new JourneyModeRecipesScreen.ActualArrowButton(this.leftPos + 152, this.topPos + 73, true, this, 163, 234));

        //checking all antikythera recipes
        ResourceLocation[] locations = journey_mode.itemListHandler.getLocations();
        boolean[] achieved;

        achieved = journey_mode.tempAdvance;
        int recipeCount = journey_mode.tempCount;
        ResourceLocation[][] items = new ResourceLocation[recipeCount][10];
        boolean[] shaped = new boolean[recipeCount];
        int j = 0;
        for (int i = 0; i < recipeCount; i++) {
            boolean itemsReceived = false;
            while (itemsReceived == false) {
                if (achieved[j]) {
                    items[i] = journey_mode.itemListHandler.getSpecificRecipe(j);
                    shaped[i] = journey_mode.itemListHandler.getIsShaped(j);
                    //code to put items from recipe from locations[j] to items[i], with input being items[i][0-9] and output being items[i][9]
                    j += 1;
                    itemsReceived = true;
                } else {
                    j += 1;
                }
            }
        }
        this.recipeItems = items;
        this.numRecipes = recipeCount;
        this.isShaped = shaped;
        if (recipeCount < 3) {
            this.showRightButton = false;
        } else {
            this.showRightButton = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.initialized) {
            this.initialized = true;
            ResourceLocation[] loc1 = new ResourceLocation[10];
            ResourceLocation[] loc2 = new ResourceLocation[10];
            if (this.numRecipes > 0) {
                loc1 = this.recipeItems[0];
                this.topShaped = this.isShaped[0];
            } else {
                for (int i = 0; i < 10; i++) {
                    loc1[i] = new ResourceLocation("");
                }
                this.topShaped = true;
            }

            if (this.numRecipes > 1) {
                loc2 = this.recipeItems[1];
                this.bottomShaped = this.isShaped[1];
            } else {
                for (int i = 0; i < 10; i++) {
                    loc2[i] = new ResourceLocation("");
                }
                this.bottomShaped = true;
            }
            ItemStack[] items1 = new ItemStack[10];
            ItemStack[] items2 = new ItemStack[10];
            for (int i = 0; i < 10; i++) {
                try {
                    loc1[i].toString().equals("");
                    items1[i] = new ItemStack(ForgeRegistries.ITEMS.getValue(loc1[i]));
                } catch (NullPointerException e) {
                    items1[i] = ItemStack.EMPTY;
                }

                try {
                    loc2[i].toString().equals("");
                    items2[i] = new ItemStack(ForgeRegistries.ITEMS.getValue(loc2[i]));
                } catch (NullPointerException e) {
                    items2[i] = ItemStack.EMPTY;
                }
            }
            this.menu.insertItem(items1, 1);
            this.menu.insertItem(items2, 2);
        }
    }

    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public void updateRecipes(boolean isRight) {
        if (isRight) {
            this.currentRecipe += 2;

        } else if (!isRight) {
            this.currentRecipe -= 2;
        }
        if (this.currentRecipe + 2 >= this.numRecipes) {
            this.showRightButton = false;
        } else {
            this.showRightButton = true;
        }
        if (this.currentRecipe == 0) {
            this.showLeftButton = false;
        } else {
            this.showLeftButton = true;
        }
        ResourceLocation[] loc1 = new ResourceLocation[10];
        ResourceLocation[] loc2 = new ResourceLocation[10];
        loc1 = this.recipeItems[this.currentRecipe];
        this.topShaped = this.isShaped[this.currentRecipe];

        if (this.numRecipes > this.currentRecipe + 1) {
            loc2 = this.recipeItems[this.currentRecipe + 1];
            this.bottomShaped = this.isShaped[this.currentRecipe + 1];
        } else {
            for (int i = 0; i < 10; i++) {
                loc2[i] = new ResourceLocation("");
            }
            this.bottomShaped = true;
        }
        ItemStack[] items1 = new ItemStack[10];
        ItemStack[] items2 = new ItemStack[10];
        for (int i = 0; i < 10; i++) {
            try {
                loc1[i].toString().equals("");
                items1[i] = new ItemStack(ForgeRegistries.ITEMS.getValue(loc1[i]));
            } catch (NullPointerException e) {
                items1[i] = ItemStack.EMPTY;
            }

            try {
                loc2[i].toString().equals("");
                items2[i] = new ItemStack(ForgeRegistries.ITEMS.getValue(loc2[i]));
            } catch (NullPointerException e) {
                items2[i] = ItemStack.EMPTY;
            }
        }
        this.menu.insertItem(items1, 1);
        this.menu.insertItem(items2, 2);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        this.font.draw(matrixStack, this.title.getString(), 8.0f, 6.0f, 4210752);

        for(Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(matrixStack, mouseX - this.leftPos, mouseY - this.topPos);
                break;
            }
        }
        if (!this.topShaped) {
            this.font.draw(matrixStack, this.SHAPELESS_NOTIF.getString(), 90, 60, TextFormatting.DARK_RED.getColor());
        }
        if (!this.bottomShaped) {
            this.font.draw(matrixStack, this.SHAPELESS_NOTIF.getString(), 90, 138, TextFormatting.DARK_RED.getColor());
        }
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void slotClicked(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {

    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        public JourneyModeRecipesScreen screen;
        private boolean selected;
        public boolean pressed = false;

        protected Button(int x, int y) {
            super(x, y, 59, 21, StringTextComponent.EMPTY);
        }

        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bind(JourneyModeRecipesScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 228;
            int j = 77;
            if (this.pressed) {
                j += 59;
            }

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.renderIcon(matrixStack);
        }

        protected abstract void renderIcon(MatrixStack p_230454_1_);

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean selectedIn) {
            this.selected = selectedIn;
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Tab extends AbstractButton {
        public boolean currentTab = false;

        protected Tab(int x, int y) { super(x, y, 32, 28, StringTextComponent.EMPTY);}

        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bind(JourneyModeRecipesScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 221;
            int j = 0;
            if (!this.currentTab) {
                j += 32;
            }

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.renderIcon(matrixStack);
        }

        protected abstract void renderIcon(MatrixStack p_230454_1_);
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class ArrowButton extends AbstractButton {
        public boolean active = false;
        public boolean isRight = false;
        public JourneyModeRecipesScreen screen;

        protected ArrowButton(int x, int y, boolean isRight, JourneyModeRecipesScreen screen) {
            super(x, y, 18, 18, StringTextComponent.EMPTY);
            this.isRight = isRight;
            this.screen = screen;
        }

        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bind(JourneyModeRecipesScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 184;
            int j = 0;
            if (this.isRight && this.screen.showRightButton) {
                this.active = true;
            } else if (!this.isRight && this.screen.showLeftButton) {
                this.active = true;
            } else {
                this.active = false;
            }
            if (!this.active) {
                j += 36;
            }

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.renderIcon(matrixStack);


        }

        protected abstract void renderIcon(MatrixStack p_230454_1_);
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class CycleButton extends JourneyModeRecipesScreen.ArrowButton {
        private final int u;
        private final int v;
        private final boolean isRight;

        protected CycleButton(int x, int y, int u, int v, boolean isRight, JourneyModeRecipesScreen screen) {
            super(x, y, isRight, screen);
            this.u = u;
            this.v = v;
            this.isRight = isRight;
        }

        protected void renderIcon(MatrixStack p_230454_1_) {
            this.blit(p_230454_1_, this.x +1, this.y, this.u, this.v, 18, 18);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class PowersTab extends JourneyModeRecipesScreen.SpriteTab {
        public PowersTab(int x, int y) {
            super(x, y, 162, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(inventory.player.getUUID().toString(), "powers"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeRecipesScreen.this.renderTooltip(matrixStack, POWERS_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class SpriteTab extends JourneyModeRecipesScreen.Tab {
        private final int u;
        private final int v;

        protected SpriteTab(int x, int y, int u, int v) {
            super(x, y);
            this.u = u;
            this.v = v;
        }

        protected void renderIcon(MatrixStack p_230454_1_) {
            this.blit(p_230454_1_, this.x + 7, this.y + 5, this.u, this.v, 18, 18);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ActualArrowButton extends JourneyModeRecipesScreen.CycleButton {
        private final boolean isRight;

        public ActualArrowButton(int x, int y, boolean isRight, JourneyModeRecipesScreen screen, int u, int v) {
            super(x, y, u, v, isRight, screen);
            this.isRight = isRight;
        }

        public void onPress() {
            if(this.isRight && this.screen.showRightButton) {
                this.screen.updateRecipes(true);
            } else if (!this.isRight && this.screen.showLeftButton) {
                this.screen.updateRecipes(false);
            }
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    class ResearchTab extends JourneyModeRecipesScreen.SpriteTab {
        public ResearchTab(int x, int y) {
            super(x, y, 198, 184);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(inventory.player.getUUID().toString(), "research"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeRecipesScreen.this.renderTooltip(matrixStack, RESEARCH_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuplicationTab extends JourneyModeRecipesScreen.SpriteTab {
        public DuplicationTab(int x, int y) {
            super(x, y, 198, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(inventory.player.getUUID().toString(), "duplication"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeRecipesScreen.this.renderTooltip(matrixStack, DUPLICATION_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class RecipesTab extends JourneyModeRecipesScreen.SpriteTab {
        public RecipesTab(int x, int y) {
            super(x, y, 217, 202);
            this.currentTab = true;
        }

        public void onPress() {
            //current tab, so nothing happens
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeRecipesScreen.this.renderTooltip(matrixStack, RECIPES_TAB, mouseX, mouseY);
        }
    }
}
