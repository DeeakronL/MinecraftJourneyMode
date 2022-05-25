package com.Deeakron.journey_mode.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import net.minecraft.client.gui.components.toasts.Toast.Visibility;

public class ResearchToast implements Toast {

    private final Component title;
    private final Component subtitle;
    private final ItemStack item;

    public ResearchToast(ItemStack item) {
        this.title = new TranslatableComponent("gui.journey_mode.research_toast.title");
        this.subtitle = new TranslatableComponent("gui.journey_mode.research_toast.subtitle");
        this.item = item;
    }

    @Override
    public Visibility render(PoseStack matrixStack, ToastComponent toastGui, long delta) {
        toastGui.getMinecraft().getTextureManager().bind(TEXTURE);
        RenderSystem.color4f(1,1,1,1);
        toastGui.blit(matrixStack, 0, 0, 0, 0,220,32);
        toastGui.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(item, 5, 7);
        toastGui.getMinecraft().font.draw(matrixStack, title, 25, 5, 0xFFFFFF);
        toastGui.getMinecraft().font.draw(matrixStack, subtitle, 25, 15, 0xFFFFFF);
        return delta >= 3000 ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public Object getToken() {
        return IToast.super.getToken();
    }
}
