package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.container.JourneyModePowersContainer;
import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.client.event.PowersCommandEvent;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public class JourneyModePowersScreen extends AbstractContainerScreen<JourneyModePowersContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_powers.png");
    public static final Component DAWN_BUTTON = new TranslatableComponent("journey_mode.gui.powers.dawn");
    public static final Component NOON_BUTTON = new TranslatableComponent("journey_mode.gui.powers.noon");
    public static final Component DUSK_BUTTON = new TranslatableComponent("journey_mode.gui.powers.dusk");
    public static final Component MIDNIGHT_BUTTON = new TranslatableComponent("journey_mode.gui.powers.midnight");
    public static final Component FREEZE_TIME_BUTTON = new TranslatableComponent("journey_mode.gui.powers.freeze");
    public static final Component UNFREEZE_TIME_BUTTON = new TranslatableComponent("journey_mode.gui.powers.unfreeze");
    public static final Component CLEAR_BUTTON = new TranslatableComponent("journey_mode.gui.powers.clear");
    public static final Component RAIN_BUTTON = new TranslatableComponent("journey_mode.gui.powers.rain");
    public static final Component STORM_BUTTON = new TranslatableComponent("journey_mode.gui.powers.storm");
    public static final Component NORMAL_BUTTON = new TranslatableComponent("journey_mode.gui.powers.normal");
    public static final Component DOUBLE_BUTTON = new TranslatableComponent("journey_mode.gui.powers.double");
    public static final Component QUADRUPLE_BUTTON = new TranslatableComponent("journey_mode.gui.powers.quadruple");
    public static final Component OCTUPLE_BUTTON = new TranslatableComponent("journey_mode.gui.powers.octuple");
    public static final Component ENABLE_MOB_SPAWN_BUTTON = new TranslatableComponent("journey_mode.gui.powers.enable_spawn");
    public static final Component DISABLE_MOB_SPAWN_BUTTON = new TranslatableComponent("journey_mode.gui.powers.disable_spawn");
    public static final Component ENABLE_MOB_GRIEFING_BUTTON = new TranslatableComponent("journey_mode.gui.powers.enable_grief");
    public static final Component DISABLE_MOB_GRIEFING_BUTTON = new TranslatableComponent("journey_mode.gui.powers.disable_grief");
    public static final Component ENABLE_GOD_MODE_BUTTON = new TranslatableComponent("journey_mode.gui.powers.enable_god_mode");
    public static final Component DISABLE_GOD_MODE_BUTTON = new TranslatableComponent("journey_mode.gui.powers.disable_god_mode");
    public static final Component LOSE_INVENTORY_BUTTON = new TranslatableComponent("journey_mode.gui.powers.lose_inv");
    public static final Component KEEP_INVENTORY_BUTTON = new TranslatableComponent("journey_mode.gui.powers.keep_inv");
    public static final Component POWERS_TAB = new TranslatableComponent("journey_mode.gui.tabs.powers");
    public static final Component RESEARCH_TAB = new TranslatableComponent("journey_mode.gui.tabs.research");
    public static final Component DUPLICATION_TAB = new TranslatableComponent("journey_mode.gui.tabs.duplication");
    public static final Component RECIPES_TAB = new TranslatableComponent("journey_mode.gui.tabs.recipes");

    private static boolean freeze;
    private int tickSpeed;
    private static boolean mobSpawn;
    private static boolean mobGrief;
    private static boolean godMode;
    private static boolean keepInv;
    private static boolean hasRecipes;

    public JourneyModePowersScreen(JourneyModePowersContainer container, Inventory inv, Component titleIn) {//, int window, boolean freeze, int tickSpeed, boolean mobSpawn, boolean mobGrief, boolean godMode, boolean keepInv) {
        super(container, inv, titleIn);
        this.freeze = journey_mode.freeze;
        this.tickSpeed = journey_mode.tickSpeed;
        this.mobSpawn = journey_mode.mobSpawn;
        this.mobGrief = journey_mode.mobGrief;
        this.godMode = journey_mode.godMode;
        this.keepInv = journey_mode.keepInv;
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 92;
        this.hasRecipes = journey_mode.hasRecipes;
    }

    protected void init() {
        super.init();
        this.addRenderableWidget(new JourneyModePowersScreen.DawnButton(this.leftPos + 25, this.topPos + 17, this));
        this.addRenderableWidget(new JourneyModePowersScreen.NoonButton(this.leftPos + 61, this.topPos + 17, this));
        this.addRenderableWidget(new JourneyModePowersScreen.DuskButton(this.leftPos + 97, this.topPos + 17, this));
        this.addRenderableWidget(new JourneyModePowersScreen.MidnightButton(this.leftPos + 133, this.topPos + 17, this));
        this.addRenderableWidget(new JourneyModePowersScreen.FreezeButton(this.leftPos + 7, this.topPos + 35, this, this.freeze));
        this.addRenderableWidget(new JourneyModePowersScreen.ClearButton(this.leftPos + 43, this.topPos + 35, this));
        this.addRenderableWidget(new JourneyModePowersScreen.RainButton(this.leftPos + 79, this.topPos + 35, this));
        this.addRenderableWidget(new JourneyModePowersScreen.StormButton(this.leftPos + 115, this.topPos + 35, this));
        this.addRenderableWidget(new JourneyModePowersScreen.NormalButton(this.leftPos + 25, this.topPos + 53, this));
        this.addRenderableWidget(new JourneyModePowersScreen.DoubleButton(this.leftPos + 61, this.topPos + 53, this));
        this.addRenderableWidget(new JourneyModePowersScreen.QuadrupleButton(this.leftPos + 97, this.topPos + 53, this));
        this.addRenderableWidget(new JourneyModePowersScreen.OctupleButton(this.leftPos + 133, this.topPos + 53, this));
        this.addRenderableWidget(new JourneyModePowersScreen.MobSpawnButton(this.leftPos + 7, this.topPos + 71, this, this.mobSpawn));
        this.addRenderableWidget(new JourneyModePowersScreen.MobGriefButton(this.leftPos + 43, this.topPos + 71, this, this.mobGrief));
        this.addRenderableWidget(new JourneyModePowersScreen.GodModeButton(this.leftPos + 79, this.topPos + 71, this, this.godMode));
        this.addRenderableWidget(new JourneyModePowersScreen.InventoryButton(this.leftPos + 115, this.topPos + 71, this, this.keepInv));
        this.addRenderableWidget(new JourneyModePowersScreen.PowersTab(this.leftPos -29, this.topPos + 21));
        this.addRenderableWidget(new JourneyModePowersScreen.ResearchTab(this.leftPos -29, this.topPos + 50));
        this.addRenderableWidget(new JourneyModePowersScreen.DuplicationTab(this.leftPos -29, this.topPos + 79));
        if (this.hasRecipes) {
            this.addRenderableWidget(new JourneyModePowersScreen.RecipesTab(this.leftPos -29, this.topPos + 108));
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
        //might need to redo render?
        for(Widget widget : this.renderables) {
            if (widget instanceof AbstractButton) {
                if (((AbstractButton) widget).isHoveredOrFocused()) {

                    ((AbstractButton) widget).renderToolTip(PoseStack, mouseX - this.leftPos, mouseY - this.topPos);
                    break;
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
        public JourneyModePowersScreen screen;
        private boolean selected;
        public boolean pressed = false;
        public boolean gameTick = false;
        public int speed = 0;

        protected Button(int x, int y) {
            super(x, y, 18, 18, TextComponent.EMPTY);
        }

        public void renderButton(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShaderTexture(0, JourneyModePowersScreen.BACKGROUND_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 184;
            int j = 0;
            if (this.pressed) {
                j += 18;
            } else if (this.gameTick == true && this.speed == this.screen.tickSpeed) {
                j += 18;
            } else if (this.selected) {
                j += 0;
            } else if (this.isHovered) {
                i += 18;
                j += 0;
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

        protected Tab(int x, int y) { super(x, y, 32, 28, TextComponent.EMPTY);}

        public void renderButton(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShaderTexture(0, JourneyModePowersScreen.BACKGROUND_TEXTURE);
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

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class SpriteButton extends JourneyModePowersScreen.Button {
        private final int u;
        private final int v;

        protected SpriteButton(int x, int y, int u, int v) {
            super(x, y);
            this.u = u;
            this.v = v;
        }

        protected void renderIcon(PoseStack p_230454_1_) {
            this.blit(p_230454_1_, this.x + 1, this.y + 1, this.u, this.v, 18, 18);
        }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class SpriteTab extends JourneyModePowersScreen.Tab {
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
    }

    @OnlyIn(Dist.CLIENT)
    class DawnButton extends JourneyModePowersScreen.SpriteButton {
        public DawnButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 55, 185);
            this.screen = screen;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("dawn"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, DAWN_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class NoonButton extends JourneyModePowersScreen.SpriteButton {
        public NoonButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 73, 185);
            this.screen = screen;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("noon"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, NOON_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuskButton extends JourneyModePowersScreen.SpriteButton {
        public DuskButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 91, 185);
            this.screen = screen;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("dusk"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, DUSK_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class MidnightButton extends JourneyModePowersScreen.SpriteButton {
        public MidnightButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 109, 185);
            this.screen = screen;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("midnight"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, MIDNIGHT_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class FreezeButton extends JourneyModePowersScreen.SpriteButton {
        public FreezeButton(int x, int y, JourneyModePowersScreen screen, Boolean activate) {
            super(x, y, 127, 185);
            this.screen = screen;
            this.pressed = activate;
        }

        public void onPress() {
            if (!this.pressed) {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("freeze"));
                this.pressed = true;
            } else {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("unfreeze"));
                this.pressed = false;
            }

        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            if (!this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, FREEZE_TIME_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, UNFREEZE_TIME_BUTTON, mouseX, mouseY);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    class ClearButton extends JourneyModePowersScreen.SpriteButton {
        public ClearButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 145, 185);
            this.screen = screen;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("clear"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, CLEAR_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class RainButton extends JourneyModePowersScreen.SpriteButton {
        public RainButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 163, 185);
            this.screen = screen;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("rain"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, RAIN_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class StormButton extends JourneyModePowersScreen.SpriteButton {
        public StormButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 181, 185);
            this.screen = screen;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("storm"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, STORM_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class NormalButton extends JourneyModePowersScreen.SpriteButton {
        public NormalButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 55, 203);
            this.screen = screen;
            this.gameTick = true;
            this.speed = 1;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("normal_speed"));
            this.screen.tickSpeed = 1;
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, NORMAL_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DoubleButton extends JourneyModePowersScreen.SpriteButton {
        public DoubleButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 73, 203);
            this.screen = screen;
            this.gameTick = true;
            this.speed = 20;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("double_speed"));
            this.screen.tickSpeed = 20;
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, DOUBLE_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class QuadrupleButton extends JourneyModePowersScreen.SpriteButton {
        public QuadrupleButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 91, 203);
            this.screen = screen;
            this.gameTick = true;
            this.speed = 40;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("quadruple_speed"));
            this.screen.tickSpeed = 40;
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, QUADRUPLE_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class OctupleButton extends JourneyModePowersScreen.SpriteButton {
        public OctupleButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 109, 203);
            this.screen = screen;
            this.gameTick = true;
            this.speed = 80;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("octuple_speed"));
            this.screen.tickSpeed = 80;
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, OCTUPLE_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class MobSpawnButton extends JourneyModePowersScreen.SpriteButton {
        public MobSpawnButton(int x, int y, JourneyModePowersScreen screen, Boolean activate) {
            super(x, y, 127, 203);
            this.screen = screen;
            this.pressed = activate;
        }

        public void onPress() {
            if (!this.pressed) {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("disable_spawn"));
                this.pressed = true;
            } else {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("enable_spawn"));
                this.pressed = false;
            }

        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            if (!this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, DISABLE_MOB_SPAWN_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, ENABLE_MOB_SPAWN_BUTTON, mouseX, mouseY);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    class MobGriefButton extends JourneyModePowersScreen.SpriteButton {
        public MobGriefButton(int x, int y, JourneyModePowersScreen screen, Boolean activate) {
            super(x, y, 145, 203);
            this.screen = screen;
            this.pressed = activate;
        }

        public void onPress() {
            if (!this.pressed) {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("disable_grief"));
                this.pressed = true;
            } else {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("enable_grief"));
                this.pressed = false;
            }

        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            if (!this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, DISABLE_MOB_GRIEFING_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, ENABLE_MOB_GRIEFING_BUTTON, mouseX, mouseY);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    class GodModeButton extends JourneyModePowersScreen.SpriteButton {
        public GodModeButton(int x, int y, JourneyModePowersScreen screen, Boolean activate) {
            super(x, y, 163, 203);
            this.screen = screen;
            this.pressed = activate;
        }

        public void onPress() {
            if (this.pressed) {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("disable_god_mode"));
                this.pressed = false;
            } else {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("enable_god_mode"));
                this.pressed = true;
            }

        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            if (this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, DISABLE_GOD_MODE_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, ENABLE_GOD_MODE_BUTTON, mouseX, mouseY);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    class InventoryButton extends JourneyModePowersScreen.SpriteButton {
        public InventoryButton(int x, int y, JourneyModePowersScreen screen, Boolean activate) {
            super(x, y, 181, 203);
            this.screen = screen;
            this.pressed = activate;
        }

        public void onPress() {
            if (this.pressed) {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("lose_inv"));
                this.pressed = false;
            } else {
                MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("keep_inv"));
                this.pressed = true;
            }

        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            if (this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, LOSE_INVENTORY_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(PoseStack, KEEP_INVENTORY_BUTTON, mouseX, mouseY);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    class PowersTab extends JourneyModePowersScreen.SpriteTab {
        public PowersTab(int x, int y) {
            super(x, y, 162, 202);
            this.currentTab = true;
        }

        public void onPress() {
            //current tab, so nothing happens
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, POWERS_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ResearchTab extends JourneyModePowersScreen.SpriteTab {
        public ResearchTab(int x, int y) {
            super(x, y, 198, 184);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "research"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, RESEARCH_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuplicationTab extends JourneyModePowersScreen.SpriteTab {
        public DuplicationTab(int x, int y) {
            super(x, y, 199, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "duplication"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, DUPLICATION_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class RecipesTab extends JourneyModePowersScreen.SpriteTab {
        public RecipesTab(int x, int y) {
            super(x, y, 217, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(Minecraft.getInstance().player.getStringUUID(), "recipes"));
        }

        public void renderToolTip(PoseStack PoseStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(PoseStack, RECIPES_TAB, mouseX, mouseY);
        }
    }
}
