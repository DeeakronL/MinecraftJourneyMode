package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.container.JourneyModeResearchContainer;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.ResearchList;
import com.Deeakron.journey_mode.client.event.MenuResearchEvent;
import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class JourneyModeResearchScreen extends AbstractContainerScreen<JourneyModeResearchContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_research.png");
    public static final Component POWERS_TAB = Component.translatable("journey_mode.gui.tabs.powers");
    public static final Component RESEARCH_TAB = Component.translatable("journey_mode.gui.tabs.research");
    public static final Component DUPLICATION_TAB = Component.translatable("journey_mode.gui.tabs.duplication");
    public static final Component RESEARCH_DESC = Component.translatable("journey_mode.gui.research");
    public static final Component RESEARCH_INFO = Component.translatable("journey_mode.gui.researched");
    private static ResearchList list;
    private static JourneyModeResearchContainer serverContain;
    public static final Component RECIPES_TAB = Component.translatable("journey_mode.gui.tabs.recipes");
    public static boolean hasRecipes;

    public JourneyModeResearchScreen(JourneyModeResearchContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 92;
        this.list = journey_mode.tempList;
        this.serverContain = journey_mode.tempContain;
        this.hasRecipes = journey_mode.hasRecipes;
    }

    protected void init() {
        super.init();
        this.addRenderableWidget(new JourneyModeResearchScreen.PowersTab(this.leftPos -29, this.topPos + 21));
        this.addRenderableWidget(new JourneyModeResearchScreen.ResearchTab(this.leftPos -29, this.topPos + 50));
        this.addRenderableWidget(new JourneyModeResearchScreen.DuplicationTab(this.leftPos -29, this.topPos + 79));
        this.addRenderableWidget(new JourneyModeResearchScreen.ResearchButton(this.leftPos + 58, this.topPos + 67, this));
        if (this.hasRecipes) {
            this.addRenderableWidget(new JourneyModeResearchScreen.RecipesTab(this.leftPos -29, this.topPos + 108));
        }
    }

    @Override
    public void render(final PoseStack PoseStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack PoseStack, int mouseX, int mouseY) {
        super.renderLabels(PoseStack, mouseX, mouseY);
        this.font.draw(PoseStack, this.title.getString(), 8.0f, 6.0f, 4210752);

        for(Widget widget : this.renderables) {
            if (widget instanceof AbstractButton) {
                if (((AbstractButton) widget).isHoveredOrFocused()) {
                    ((AbstractButton) widget).renderToolTip(PoseStack, mouseX - this.leftPos, mouseY - this.topPos);
                    break;
                }
            }
        }
        if (!this.menu.getItems().get(0).isEmpty()) {
            String key = "\"" + ForgeRegistries.ITEMS.getKey(this.menu.getItems().get(0).getItem()).toString() + "\"";
            if (this.list.hasItem(key)) {
                String info1 = this.menu.getItems().get(0).getItem().getDescription().getString();
                String info2 = this.list.get(key)[0] + "/" + this.list.get(key)[1] + " " + this.RESEARCH_INFO.getString();
                if (this.list.reachCap(key)) {
                    this.font.draw(PoseStack, info1, 8, 16, ChatFormatting.DARK_RED.getColor());
                    this.font.draw(PoseStack, info2, 8, 26, ChatFormatting.DARK_RED.getColor());
                } else {
                    this.font.draw(PoseStack, info1, 8, 16, 4210752);
                    this.font.draw(PoseStack, info2, 8, 26, 4210752);
                }
            }
        }
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(PoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        public JourneyModeResearchScreen screen;
        private boolean selected;
        public boolean pressed = false;

        protected Button(int x, int y) {
            super(x, y, 59, 21, Component.empty());
        }

        public void renderButton(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShaderTexture(0, JourneyModeResearchScreen.BACKGROUND_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 228;
            int j = 77;
            if (this.pressed) {
                j += 59;
            }

            this.blit(PoseStack, this.x, this.y, j, i, this.width, this.height);
            this.renderIcon(PoseStack);
        }

        protected abstract void renderIcon(PoseStack p_230454_1_);

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

        protected Tab(int x, int y) { super(x, y, 32, 28, Component.empty());}

        public void renderButton(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShaderTexture(0, JourneyModeResearchScreen.BACKGROUND_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 221;
            int j = 0;
            if (!this.currentTab) {
                j += 32;
            }

            this.blit(PoseStack, this.x, this.y, j, i, this.width, this.height);
            this.renderIcon(PoseStack);
        }

        protected abstract void renderIcon(PoseStack p_230454_1_);
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class SpriteTab extends JourneyModeResearchScreen.Tab {
        private final int u;
        private final int v;

        protected SpriteTab(int x, int y, int u, int v) {
            super(x, y);
            this.u = u;
            this.v = v;
        }

        protected void renderIcon(PoseStack p_230454_1_) {
            this.blit(p_230454_1_, this.x + 7, this.y + 5, this.u, this.v, 18, 18);
        }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class TextButton extends JourneyModeResearchScreen.Button {
        protected TextButton(int x, int y) {
            super(x, y);
        }

        protected void renderIcon(PoseStack p_230454_1_) {
            this.screen.font.draw(p_230454_1_, this.screen.RESEARCH_DESC.getString(), this.screen.leftPos + 64.0f, this.screen.topPos + 72.0f, 4210752);
        }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    class PowersTab extends JourneyModeResearchScreen.SpriteTab {
        public PowersTab(int x, int y) {
            super(x, y, 162, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "powers"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeResearchScreen.this.renderTooltip(PoseStack, POWERS_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ResearchButton extends JourneyModeResearchScreen.TextButton {
        public ResearchButton(int x, int y, JourneyModeResearchScreen screen) {
            super(x, y);
            this.screen = screen;
        }

        public void onPress() {
            String key = "\"" + ForgeRegistries.ITEMS.getKey(this.screen.menu.getItems().get(0).getItem()).toString() + "\"";
            int count = this.screen.menu.getItems().get(0).getCount();
            if (this.screen.list.hasItem(key)) {
                if (this.screen.list.reachCap(key)) {

                } else {
                    /*if(!minecraft.player.world.isRemote){*/minecraft.player.playNotifySound(JMSounds.RESEARCH_GRIND.get(), SoundSource.BLOCKS, 0.1F, 1.0F);//}
                    int diff = this.screen.list.get(key)[1] - this.screen.list.get(key)[0];
                    if (count > diff) {
                        this.screen.serverContain.getItems().get(0).setCount(count - diff);
                        MinecraftForge.EVENT_BUS.post(new MenuResearchEvent(key, diff, Minecraft.getInstance().player.getUUID(), this.screen.menu.getItems().get(0).getItem().getDefaultInstance()));
                    } else {
                        this.screen.serverContain.getItems().get(0).setCount(0);
                        MinecraftForge.EVENT_BUS.post(new MenuResearchEvent(key, count, Minecraft.getInstance().player.getUUID(), this.screen.menu.getItems().get(0).getItem().getDefaultInstance()));
                    }
                }
            }
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ResearchTab extends JourneyModeResearchScreen.SpriteTab {
        public ResearchTab(int x, int y) {
            super(x, y, 198, 184);
            this.currentTab = true;
        }

        public void onPress() {
            //current tab, so nothing happens
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeResearchScreen.this.renderTooltip(PoseStack, RESEARCH_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuplicationTab extends JourneyModeResearchScreen.SpriteTab {
        public DuplicationTab(int x, int y) {
            super(x, y, 198, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "duplication"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeResearchScreen.this.renderTooltip(PoseStack, DUPLICATION_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class RecipesTab extends JourneyModeResearchScreen.SpriteTab {
        public RecipesTab(int x, int y) {
            super(x, y, 217, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "recipes"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModeResearchScreen.this.renderTooltip(PoseStack, RECIPES_TAB, mouseX, mouseY);
        }
    }
}
