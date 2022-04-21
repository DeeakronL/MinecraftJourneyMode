package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.client.event.MenuResearchEvent;
import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.container.JourneyModeRecipesContainer;
import com.Deeakron.journey_mode.container.JourneyModeResearchContainer;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.ResearchList;
import com.Deeakron.journey_mode.init.UnobtainItemInit;
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
import net.minecraft.util.SoundCategory;
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
    public static final ITextComponent RESEARCH_DESC = new TranslationTextComponent("journey_mode.gui.research");
    public static final ITextComponent RESEARCH_INFO = new TranslationTextComponent("journey_mode.gui.researched");
    private static ResearchList list;
    private static JourneyModeResearchContainer serverContain;

    public JourneyModeRecipesScreen(JourneyModeRecipesContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
        //this.playerInventoryTitleY = this.ySize - 92;
        this.list = journey_mode.tempList;
        this.serverContain = journey_mode.tempContain;
    }

    protected void init() {
        super.init();
        this.addButton(new JourneyModeRecipesScreen.PowersTab(this.guiLeft -29, this.guiTop + 21));
        this.addButton(new JourneyModeRecipesScreen.ResearchTab(this.guiLeft -29, this.guiTop + 50));
        this.addButton(new JourneyModeRecipesScreen.DuplicationTab(this.guiLeft -29, this.guiTop + 79));
        ResourceLocation[] loc1 = {new ResourceLocation("minecraft:chain"), new ResourceLocation("minecraft:chain"), new ResourceLocation("minecraft:chain"), new ResourceLocation("minecraft:chain"),new ResourceLocation("minecraft:command_block"),new ResourceLocation("minecraft:chain"),new ResourceLocation("minecraft:chain"),new ResourceLocation("minecraft:chain"),new ResourceLocation("minecraft:chain"),new ResourceLocation("minecraft:chain_command_block")};
        ResourceLocation[] loc2 = {new ResourceLocation("minecraft:end_stone"),new ResourceLocation("journey_mode:unobtainium_block"),new ResourceLocation("minecraft:end_stone"),new ResourceLocation("journey_mode:unobtainium_block"),new ResourceLocation("minecraft:ender_pearl"),new ResourceLocation("journey_mode:unobtainium_block"),new ResourceLocation("minecraft:end_stone"),new ResourceLocation("journey_mode:unobtainium_block"),new ResourceLocation("minecraft:end_stone"),new ResourceLocation("minecraft:end_portal_frame")};

        ItemStack[] items1 = new ItemStack[10];
        ItemStack[] items2 = new ItemStack[10];
        for (int i = 0; i < 10; i++) {
            items1[i] = new ItemStack(ForgeRegistries.ITEMS.getValue(loc1[i]));
            items2[i] = new ItemStack(ForgeRegistries.ITEMS.getValue(loc2[i]));
        }
        //items1[0] = new ItemStack(UnobtainItemInit.AETHERIAL_VOID_DUST.get());
        //ItemStack item = new ItemStack(UnobtainItemInit.AETHERIAL_VOID_DUST.get());
        //this.container.insertItem(item);
        this.container.insertItem(items1, 1);
        this.container.insertItem(items2, 2);
        this.minecraft.playerController.sendSlotPacket(items1[0], 0);
        //journey_mode.LOGGER.info(getNarrationMessage());
        //this.buttonsNotDrawn = true;
    }

    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
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
        /*if (!this.container.getInventory().get(0).isEmpty()) {
            String key = "\"" + this.container.getInventory().get(0).getItem().getRegistryName().toString() + "\"";
            if (this.list.hasItem(key)) {
                String info1 = this.container.getInventory().get(0).getItem().getName().getString();
                String info2 = this.list.get(key)[0] + "/" + this.list.get(key)[1] + " " + this.RESEARCH_INFO.getString();
                if (this.list.reachCap(key)) {
                    this.font.drawString(matrixStack, info1, 8, 16, TextFormatting.DARK_RED.getColor());
                    this.font.drawString(matrixStack, info2, 8, 26, TextFormatting.DARK_RED.getColor());
                } else {
                    this.font.drawString(matrixStack, info1, 8, 16, 4210752);
                    this.font.drawString(matrixStack, info2, 8, 26, 4210752);
                }
            }
        }*/
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        //int x = (this.width = this.xSize) / 2;
        //int y = (this.height = this.ySize) / 2;

        //this.blit(matrixStack, this.width/2,this.height/2, 0, 0, this.xSize, this.ySize);

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
    abstract static class TextButton extends JourneyModeRecipesScreen.Button {
        protected TextButton(int x, int y) {
            super(x, y);
        }

        protected void func_230454_a_(MatrixStack p_230454_1_) {
            this.screen.font.drawString(p_230454_1_, this.screen.RESEARCH_DESC.getString(), this.screen.guiLeft + 64.0f, this.screen.guiTop + 72.0f, 4210752);
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
    class ResearchTab extends JourneyModeRecipesScreen.SpriteTab {
        public ResearchTab(int x, int y) {
            super(x, y, 198, 184);
            this.currentTab = true;
        }

        public void onPress() {
            //current tab, so nothing happens
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
            //current tab, so nothing happens
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModeRecipesScreen.this.renderTooltip(matrixStack, DUPLICATION_TAB, mouseX, mouseY);
        }
    }
}
