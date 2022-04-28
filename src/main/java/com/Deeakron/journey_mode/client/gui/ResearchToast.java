package com.Deeakron.journey_mode.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ResearchToast implements IToast {

    private final ITextComponent title;
    private final ITextComponent subtitle;
    private final ItemStack item;

    public ResearchToast(ItemStack item) {
        this.title = new TranslationTextComponent("gui.journey_mode.research_toast.title");
        this.subtitle = new TranslationTextComponent("gui.journey_mode.research_toast.subtitle");
        this.item = item;
    }

    @Override
    public Visibility func_230444_a_(MatrixStack matrixStack, ToastGui toastGui, long delta) {
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        RenderSystem.color4f(1,1,1,1);
        toastGui.blit(matrixStack, 0, 0, 0, 0,220,32);
        toastGui.getMinecraft().getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(item, 5, 7);
        toastGui.getMinecraft().fontRenderer.drawText(matrixStack, title, 25, 5, 0xFFFFFF);
        toastGui.getMinecraft().fontRenderer.drawText(matrixStack, subtitle, 25, 15, 0xFFFFFF);
        return delta >= 3000 ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public Object getType() {
        return IToast.super.getType();
    }
}
