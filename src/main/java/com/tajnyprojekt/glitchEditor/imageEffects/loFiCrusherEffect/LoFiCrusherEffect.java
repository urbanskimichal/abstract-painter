package com.tajnyprojekt.glitchEditor.imageEffects.loFiCrusherEffect;

import com.tajnyprojekt.glitchEditor.imageEffects.imageEffect.ImageEffect;
import processing.core.PApplet;
import processing.core.PImage;

import static processing.core.PApplet.floor;

public class LoFiCrusherEffect extends ImageEffect {

    //normFreq range: 0, 1
    private float normFreq;
    private float step;

    public LoFiCrusherEffect(PApplet parent) {
        super(parent);
        normFreq = 0;
        step = 4;
    }

    public void process(PImage img) {
        float phasor = 0;
        float last = 0;

        for (int i = 1; i < img.pixels.length; i++) {
            phasor += normFreq;
            if (phasor >= 1) {
                phasor = phasor - 1;
                last = step * floor(img.pixels[i] / step + 0.5f );
            }
            img.pixels[i] = (int)last;
        }
    }

    public void setNormFreq(float normFreq) {
        this.normFreq = normFreq;
    }

    public void setStep(float step) {
        this.step = step;
    }
}
