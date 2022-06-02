package com.Deeakron.journey_mode.item;

import com.Deeakron.journey_mode.journey_mode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ScannerModel<T extends LivingEntity> extends HumanoidModel<T> {
    public ScannerModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSizeIn, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(renderTypeIn, modelSizeIn, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    @Override
    public void renderToBuffer(PoseStack PoseStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(new ResourceLocation(journey_mode.MODID, "textures/armor/scanner_layer_1.png")));
        super.renderToBuffer(PoseStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
