package com.Deeakron.journey_mode.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

public class ResearchToast implements Toast {

    private final Component title;
    private final Component subtitle;
    private final ItemStack item;

    public ResearchToast(ItemStack item) {
        this.title = Component.translatable("gui.journey_mode.research_toast.title");
        this.subtitle = Component.translatable("gui.journey_mode.research_toast.subtitle");
        this.item = item;
    }

    @Override
    public Visibility render(PoseStack PoseStack, ToastComponent toastGui, long delta) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1,1,1,1);
        toastGui.blit(PoseStack, 0, 0, 0, 0,220,32);
        toastGui.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(item, 5, 7);
        toastGui.getMinecraft().font.draw(PoseStack, title, 25, 5, 0xFFFFFF);
        toastGui.getMinecraft().font.draw(PoseStack, subtitle, 25, 15, 0xFFFFFF);
        return delta >= 3000 ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public Object getToken() {
        return Toast.super.getToken();
    }
}
