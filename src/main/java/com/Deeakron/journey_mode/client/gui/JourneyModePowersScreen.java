package com.Deeakron.journey_mode.client.gui;

import com.Deeakron.journey_mode.JourneyModePowersContainer;
import com.Deeakron.journey_mode.client.event.PowersCommandEvent;
import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public class JourneyModePowersScreen extends ContainerScreen<Container> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(journey_mode.MODID, "textures/gui/jm_powers.png");
    public static final ITextComponent DAWN_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.dawn");
    public static final ITextComponent NOON_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.noon");
    public static final ITextComponent DUSK_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.dusk");
    public static final ITextComponent MIDNIGHT_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.midnight");
    public static final ITextComponent FREEZE_TIME_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.freeze");
    public static final ITextComponent UNFREEZE_TIME_BUTTON = new TranslationTextComponent("journey_mode.gui.powers.unfreeze");

    public JourneyModePowersScreen(PlayerInventory inv, ITextComponent titleIn, int window) {
        super(new JourneyModePowersContainer(window, inv), inv, titleIn);
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
        private boolean selected;

        protected Button(int x, int y) {
            super(x, y, 18, 18, StringTextComponent.EMPTY);
        }

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(JourneyModePowersScreen.BACKGROUND_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 184;
            int j = 0;
            if (!this.active) {
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
    class DawnButton extends JourneyModePowersScreen.SpriteButton {
        private JourneyModePowersScreen screen;
        public DawnButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 55, 185);
            this.screen = screen;
        }

        public void onPress() {
            //UnobtainiumAntikytheraTileEntity tileEntity = this.screen.container.getATileEntity();
            //ITextComponent message = new StringTextComponent("/time set day");
            //playerInventory.player.sendMessage(message, playerInventory.player.getUniqueID());
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("dawn"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, DAWN_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class NoonButton extends JourneyModePowersScreen.SpriteButton {
        private JourneyModePowersScreen screen;
        public NoonButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 73, 185);
            this.screen = screen;
        }

        public void onPress() {
            //UnobtainiumAntikytheraTileEntity tileEntity = this.screen.container.getATileEntity();
            //ITextComponent message = new StringTextComponent("/time set day");
            //playerInventory.player.sendMessage(message, playerInventory.player.getUniqueID());
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("noon"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, NOON_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class DuskButton extends JourneyModePowersScreen.SpriteButton {
        private JourneyModePowersScreen screen;
        public DuskButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 91, 185);
            this.screen = screen;
        }

        public void onPress() {
            //UnobtainiumAntikytheraTileEntity tileEntity = this.screen.container.getATileEntity();
            //ITextComponent message = new StringTextComponent("/time set day");
            //playerInventory.player.sendMessage(message, playerInventory.player.getUniqueID());
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("dusk"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, DUSK_BUTTON, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class MidnightButton extends JourneyModePowersScreen.SpriteButton {
        private JourneyModePowersScreen screen;
        public MidnightButton(int x, int y, JourneyModePowersScreen screen) {
            super(x, y, 109, 185);
            this.screen = screen;
        }

        public void onPress() {
            //UnobtainiumAntikytheraTileEntity tileEntity = this.screen.container.getATileEntity();
            //ITextComponent message = new StringTextComponent("/time set day");
            //playerInventory.player.sendMessage(message, playerInventory.player.getUniqueID());
            MinecraftForge.EVENT_BUS.post(new PowersCommandEvent("midnight"));
        }

        public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
            JourneyModePowersScreen.this.renderTooltip(matrixStack, MIDNIGHT_BUTTON, mouseX, mouseY);
        }
    }
}
