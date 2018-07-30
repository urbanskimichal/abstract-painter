package com.tajnyprojekt.glitchEditor.imageEffects.delayEffect;

import com.tajnyprojekt.glitchEditor.imageEffects.imageEffect.ImageEffect;
import processing.core.PApplet;
import processing.core.PImage;


public class DelayEffect extends ImageEffect {

    //decay range: 0, 1
    private float decay;

    //increment range: 1, 30
    private int increment;

    public DelayEffect(PApplet parent) {
        super(parent);
        decay = 0;
        increment = 1;
    }

    public void process(PImage img) {
        for (int i = 0; i < img.pixels.length - increment; i += increment)
        {
            img.pixels[img.pixels.length - 1 - i] += (int)(img.pixels[i] * decay);
        }
    }

    public void setDecay(float decay) {
        this.decay = decay;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }
}
