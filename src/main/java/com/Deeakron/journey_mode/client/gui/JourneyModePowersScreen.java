package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.container.JourneyModePowersContainer;
import com.Deeakron.journey_mode.client.event.MenuSwitchEvent;
import com.Deeakron.journey_mode.client.event.PowersCommandEvent;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public class JourneyModePowersScreen extends ContainerScreen<JourneyModePowersContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_powers.png");
    public static final ITextComponent DAWN_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.dawn");
    public static final ITextComponent NOON_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.noon");
    public static final ITextComponent DUSK_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.dusk");
    public static final ITextComponent MIDNIGHT_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.midnight");
    public static final ITextComponent FREEZE_TIME_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.freeze");
    public static final ITextComponent UNFREEZE_TIME_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.unfreeze");
    public static final ITextComponent CLEAR_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.clear");
    public static final ITextComponent RAIN_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.rain");
    public static final ITextComponent STORM_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.storm");
    public static final ITextComponent NORMAL_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.normal");
    public static final ITextComponent DOUBLE_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.double");
    public static final ITextComponent QUADRUPLE_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.quadruple");
    public static final ITextComponent OCTUPLE_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.octuple");
    public static final ITextComponent ENABLE_MOB_SPAWN_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.enable_spawn");
    public static final ITextComponent DISABLE_MOB_SPAWN_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.disable_spawn");
    public static final ITextComponent ENABLE_MOB_GRIEFING_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.enable_grief");
    public static final ITextComponent DISABLE_MOB_GRIEFING_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.disable_grief");
    public static final ITextComponent ENABLE_GOD_MODE_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.enable_god_mode");
    public static final ITextComponent DISABLE_GOD_MODE_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.disable_god_mode");
    public static final ITextComponent LOSE_INVENTORY_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.lose_inv");
    public static final ITextComponent KEEP_INVENTORY_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.keep_inv");
    public static final ITextComponent POWERS_TAB = new TranslationTextComponent("journey_mode.gui.tabs.powers");
    public static final ITextComponent RESEARCH_TAB = new TranslationTextComponent("journey_mode.gui.tabs.research");
    public static final ITextComponent DUPLICATION_TAB = new TranslationTextComponent("journey_mode.gui.tabs.duplication");

    private static boolean freeze;
    private int tickSpeed;
    private static boolean mobSpawn;
    private static boolean mobGrief;
    private static boolean godMode;
    private static boolean keepInv;

    public JourneyModePowersScreen(JourneyModePowersContainer container, PlayerInventory inv, ITextComponent titleIn) {//, int window, boolean freeze, int tickSpeed, boolean mobSpawn, boolean mobGrief, boolean godMode, boolean keepInv) {
        super(container, inv, titleIn);
        //journey_mode.LOGGER.info("the world really is: " + inv.player.world.isRemote());
        this.freeze = journey_mode.freeze;
        this.tickSpeed = journey_mode.tickSpeed;
        this.mobSpawn = journey_mode.mobSpawn;
        this.mobGrief = journey_mode.mobGrief;
        this.godMode = journey_mode.godMode;
        this.keepInv = journey_mode.keepInv;
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 175;
        this.ySize = 183;
        this.playerInventoryTitleY = this.ySize - 92;
    }

    protected void init() {
        super.init();

        this.addButton(new JourneyModePowersScreen.DawnButton(this.guiLeft + 25, this.guiTop + 17, this));
        this.addButton(new JourneyModePowersScreen.NoonButton(this.guiLeft + 61, this.guiTop + 17, this));
        this.addButton(new JourneyModePowersScreen.DuskButton(this.guiLeft + 97, this.guiTop + 17, this));
        this.addButton(new JourneyModePowersScreen.MidnightButton(this.guiLeft + 133, this.guiTop + 17, this));
        this.addButton(new JourneyModePowersScreen.FreezeButton(this.guiLeft + 7, this.guiTop + 35, this, this.freeze));
        this.addButton(new JourneyModePowersScreen.ClearButton(this.guiLeft + 43, this.guiTop + 35, this));
        this.addButton(new JourneyModePowersScreen.RainButton(this.guiLeft + 79, this.guiTop + 35, this));
        this.addButton(new JourneyModePowersScreen.StormButton(this.guiLeft + 115, this.guiTop + 35, this));
        this.addButton(new JourneyModePowersScreen.NormalButton(this.guiLeft + 25, this.guiTop + 53, this));
        this.addButton(new JourneyModePowersScreen.DoubleButton(this.guiLeft + 61, this.guiTop + 53, this));
        this.addButton(new JourneyModePowersScreen.QuadrupleButton(this.guiLeft + 97, this.guiTop + 53, this));
        this.addButton(new JourneyModePowersScreen.OctupleButton(this.guiLeft + 133, this.guiTop + 53, this));
        this.addButton(new JourneyModePowersScreen.MobSpawnButton(this.guiLeft + 7, this.guiTop + 71, this, this.mobSpawn));
        this.addButton(new JourneyModePowersScreen.MobGriefButton(this.guiLeft + 43, this.guiTop + 71, this, this.mobGrief));
        this.addButton(new JourneyModePowersScreen.GodModeButton(this.guiLeft + 79, this.guiTop + 71, this, this.godMode));
        this.addButton(new JourneyModePowersScreen.InventoryButton(this.guiLeft + 115, this.guiTop + 71, this, this.keepInv));
        this.addButton(new JourneyModePowersScreen.PowersTab(this.guiLeft -29, this.guiTop + 21));
        this.addButton(new JourneyModePowersScreen.ResearchTab(this.guiLeft -29, this.guiTop + 50));
        this.addButton(new JourneyModePowersScreen.DuplicationTab(this.guiLeft -29, this.guiTop + 79));
        journey_mode.LOGGER.info(getNarrationMessage());
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

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        public JourneyModePowersScreen screen;
        private boolean selected;
        public boolean pressed = false;
        public boolean gameTick = false;
        public int speed = 0;

        protected Button(int x, int y) {
            super(x, y, 18, 18, StringTextComponent.EMPTY);
        }

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModePowersScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 184;
            int j = 0;
            if (this.pressed) {
                j += 18;
            } else if (this.gameTick == true && this.speed == this.screen.tickSpeed) {
                j += 18;
            } else if (this.selected) {
                j += 0;
            } else if (this.isHovered()) {
                i += 18;
                j += 0;
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
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModePowersScreen.BACKGROUND_TEXTURE);
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
    abstract static class SpriteButton extends JourneyModePowersScreen.Button {
        private final int u;
        private final int v;

        protected SpriteButton(int x, int y, int u, int v) {
            super(x, y);
            this.u = u;
            this.v = v;
        }

        protected void func_230454_a_(MatrixStack p_230454_1_) {
            this.blit(p_230454_1_, this.x + 1, this.y + 1, this.u, this.v, 18, 18);
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

        protected void func_230454_a_(MatrixStack p_230454_1_) {
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, DAWN_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, NOON_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, DUSK_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, MIDNIGHT_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            if (!this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, FREEZE_TIME_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, UNFREEZE_TIME_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, CLEAR_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, RAIN_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, STORM_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, NORMAL_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, DOUBLE_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, QUADRUPLE_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, OCTUPLE_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            if (!this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, DISABLE_MOB_SPAWN_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, ENABLE_MOB_SPAWN_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            if (!this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, DISABLE_MOB_GRIEFING_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, ENABLE_MOB_GRIEFING_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            if (this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, DISABLE_GOD_MODE_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, ENABLE_GOD_MODE_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            if (this.pressed) {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, LOSE_INVENTORY_BUTTON, mouseX, mouseY);
            } else {
                JourneyModePowersScreen.this.renderTooltip(matrixStack, KEEP_INVENTORY_BUTTON, mouseX, mouseY);
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

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, POWERS_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ResearchTab extends JourneyModePowersScreen.SpriteTab {
        public ResearchTab(int x, int y) {
            super(x, y, 198, 184);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(playerInventory.player.getUniqueID().toString(), "research"));
            //NetworkHooks.openGui((ServerPlayerEntity) playerInventory.player, new JourneyModeResearchContainerProvider());
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, RESEARCH_TAB, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuplicationTab extends JourneyModePowersScreen.SpriteTab {
        public DuplicationTab(int x, int y) {
            super(x, y, 199, 202);
            this.currentTab = false;
        }

        public void onPress() {
            MinecraftForge.EVENT_BUS.post(new MenuSwitchEvent(playerInventory.player.getUniqueID().toString(), "duplication"));
            //current tab, so nothing happens
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, DUPLICATION_TAB, mouseX, mouseY);
        }
    }
}
