package com.Deeakron.journey_mode.data;

import com.Deeakron.journey_mode.init.UnobtainItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BrokenLightParticle extends TextureSheetParticle {

    public BrokenLightParticle(ClientLevel p_172356_, double p_172357_, double p_172358_, double p_172359_, ItemLike p_172360_) {
        super(p_172356_, p_172357_, p_172358_, p_172359_);
        this.setSprite(Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel((Item) p_172360_).getParticleIcon());
        this.gravity = 0.0F;
        this.lifetime = 80;
        this.hasPhysics = false;
    }

    public float getQuadSize(float p_172363_) {
        return 0.5F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class BrokenLightProvider implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType p_172394_, ClientLevel p_172395_, double p_172396_, double p_172397_, double p_172398_, double p_172399_, double p_172400_, double p_172401_) {
            return new BrokenLightParticle(p_172395_, p_172396_, p_172397_, p_172398_, UnobtainItemInit.BROKEN_LIGHT.get());
        }
    }



    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }
}
