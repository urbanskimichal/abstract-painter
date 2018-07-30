package com.tajnyprojekt.glitchEditor.imageEffects.imageEffect;

import com.tajnyprojekt.glitchEditor.processingChild.ProcessingChild;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class ImageEffect extends ProcessingChild {

    public ImageEffect(PApplet parent) {
        super(parent);
    }

    public abstract void process(PImage img);
}
