package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.container.JourneyModeRecipesContainer;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class JourneyModeRecipesScreen extends ContainerScreen<JourneyModeRecipesContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_antikythera_recipes.png");
    public static final ITextComponent POWERS_TAB = new TranslationTextComponent("journey_mode.gui.tabs.powers");
    public static final ITextComponent RESEARCH_TAB = new TranslationTextComponent("journey_mode.gui.tabs.research");
    public static final ITextComponent DUPLICATION_TAB = new TranslationTextComponent("journey_mode.gui.tabs.duplication");
    public static final ITextComponent RECIPES_TAB = new TranslationTextComponent("journey_mode.gui.tabs.recipes");
    public static final ITextComponent SHAPELESS_NOTIF = new TranslationTextComponent("journey_mode.gui.recipes.shapeless");
    private boolean initialized;
    private int currentRecipe = 0;
    private int numRecipes;
    private boolean showRightButton = false;
    private boolean showLeftButton = false;
    private ResourceLocation[][] recipeItems;
    private boolean[] isShaped;
    private boolean topShaped = true;
    private boolean bottomShaped = true;


    public JourneyModeRecipesScreen(JourneyModeRecipesContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
        this.initialized = false;
        this.playerInventoryTitleX = -1000;
        this.playerInventoryTitleY = -1000;
    }

    protected void init() {
        super.init();
        this.addButton(new JourneyModeRecipesScreen.PowersTab(this.guiLeft -29, this.guiTop + 21));
        this.addButton(new JourneyModeRecipesScreen.ResearchTab(this.guiLeft -29, this.guiTop + 50));
        this.addButton(new JourneyModeRecipesScreen.DuplicationTab(this.guiLeft -29, this.guiTop + 79));
        this.addButton(new JourneyModeRecipesScreen.RecipesTab(this.guiLeft -29, this.guiTop + 108));
        this.addButton(new JourneyModeRecipesScreen.ActualArrowButton(this.guiLeft + 6, this.guiTop + 73, false, this, 147, 234));
        this.addButton(new JourneyModeRecipesScreen.ActualArrowButton(this.guiLeft + 152, this.guiTop + 73, true, this, 163, 234));

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
            this.container.insertItem(items1, 1);
            this.container.insertItem(items2, 2);
        }
    }

    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
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
        this.container.insertItem(items1, 1);
        this.container.insertItem(items2, 2);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.drawString(matrixStack, this.title.getString(), 8.0f, 6.0f, 4210752);

        for(Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(matrixStack, mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }
        if (!this.topShaped) {
            this.font.drawString(matrixStack, this.SHAPELESS_NOTIF.getString(), 90, 60, TextFormatting.DARK_RED.getColor());
        }
        if (!this.bottomShaped) {
            this.font.drawString(matrixStack, this.SHAPELESS_NOTIF.getString(), 90, 138, TextFormatting.DARK_RED.getColor());
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {

    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        public JourneyModeRecipesScreen screen;
        private boolean selected;
        public boolean pressed = false;

        protected Button(int x, int y) {
            super(x, y, 59, 21, StringTextComponent.EMPTY);
        }

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModeRecipesScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 228;
            int j = 77;
            if (this.pressed) {
                j += 59;
            }

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.func_230454_a_(matrixStack);
        }

        protected abstract void func_230454_a_(MatrixStack p_230454_1_);

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

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModeRecipesScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 221;
            int j = 0;
            if (!this.currentTab) {
                j += 32;
            }

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.func_230454_a_(matrixStack);
        }

        protected abstract void func_230454_a_(MatrixStack p_230454_1_);
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

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModeRecipesScreen.BACKGROUND_TEXTURE);
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
            this.func_230454_a_(matrixStack);


        }

        protected abstract void func_230454_a_(MatrixStack p_230454_1_);
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

        protected void func_230454_a_(MatrixStack p_230454_1_) {
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
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(playerInventory.player.getUniqueID().toString(), "powers"));
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

        protected void func_230454_a_(MatrixStack p_230454_1_) {
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
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(playerInventory.player.getUniqueID().toString(), "research"));
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
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(playerInventory.player.getUniqueID().toString(), "duplication"));
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
