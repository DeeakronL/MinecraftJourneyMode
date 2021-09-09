package com.Deeakron.journey_mode;

import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ResearchGrinderSound extends SimpleSound implements ITickableSound {
    public HorizontalBlock grinder;
    private Boolean donePlaying = false;
    private Boolean hasStarted = false;


    public ResearchGrinderSound(HorizontalBlock researchGrinder, BlockPos pos) {
        super(JMSounds.RESEARCH_GRIND.get(), SoundCategory.BLOCKS, 0.1f, 1.0f, pos);
        this.grinder = researchGrinder;
        this.repeat = false;
        this.repeatDelay = 1;
    }

    public boolean shouldPlaySound() {
        return !this.donePlaying;
    }

    @Override
    public boolean isDonePlaying() {
        return this.donePlaying;
    }

    @Override
    public void tick() {


    }
}
