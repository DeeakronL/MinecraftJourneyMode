package com.Deeakron.journey_mode.client.sound;

import com.Deeakron.journey_mode.init.JMSounds;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;

public class ResearchGrinderSound extends SimpleSoundInstance implements TickableSoundInstance {
    public HorizontalDirectionalBlock grinder;
    private Boolean donePlaying = false;
    private Boolean hasStarted = false;


    public ResearchGrinderSound(HorizontalDirectionalBlock researchGrinder, BlockPos pos) {
        super(JMSounds.RESEARCH_GRIND.get(), SoundSource.BLOCKS, 0.1f, 1.0f, pos);
        this.grinder = researchGrinder;
        this.looping = false;
        this.delay = 1;
    }

    public boolean canPlaySound() {
        return !this.donePlaying;
    }

    @Override
    public boolean isStopped() {
        return this.donePlaying;
    }

    @Override
    public void tick() {


    }
}
