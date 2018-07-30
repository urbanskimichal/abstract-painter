package com.tajnyprojekt.glitchEditor.imageEffects.lowPassEffect;

import com.tajnyprojekt.glitchEditor.imageEffects.imageEffect.ImageEffect;
import processing.core.PApplet;
import processing.core.PImage;

public class LowPassEffect extends ImageEffect {

    //alpha range: 0, 1
    private float alpha;

    public LowPassEffect(PApplet parent) {
        super(parent);
        alpha = 0;
    }

    public void process(PImage img) {
        img.pixels[0] = (int)(alpha * img.pixels[0]);
        for (int i = 1; i < img.pixels.length; i++) {
            img.pixels[i] = (int)(img.pixels[i - 1] + alpha * (img.pixels[i] - img.pixels[i-1]));
        }
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
