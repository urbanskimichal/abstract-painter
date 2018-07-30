package com.tajnyprojekt.glitchEditor.glitcher;

import com.tajnyprojekt.glitchEditor.imageEffects.delayEffect.DelayEffect;
import com.tajnyprojekt.glitchEditor.imageEffects.loFiCrusherEffect.LoFiCrusherEffect;
import com.tajnyprojekt.glitchEditor.imageEffects.lowPassEffect.LowPassEffect;
import com.tajnyprojekt.glitchEditor.midi.control.MidiControl;
import com.tajnyprojekt.glitchEditor.midi.devices.custom4Knobs.controls.Custom4KnobsKnobs;
import com.tajnyprojekt.glitchEditor.processingChild.ProcessingChild;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;

import javax.swing.*;

import static processing.core.PApplet.map;

public class Glitcher extends ProcessingChild{

    private Capture cam;
    private MidiControl midiControl;

    private DelayEffect imageDelay;
    private LowPassEffect lowPassEffect;
    private LoFiCrusherEffect loFiCrusherEffect;

    private PImage currentFrame;

    public Glitcher(PApplet parent, String camName) {
        super(parent);
        midiControl = MidiControl.getInstance();
        initEffects();
        initCam(camName);
    }

    private void initCam(String camName) {
        cam = new Capture(parent, camName);
        cam.start();
        while (!cam.available()) {
            System.out.println("Waiting for the camera");
        }
        System.out.println("The camera has loaded");
        cam.read();
    }

    private void initEffects() {
        imageDelay = new DelayEffect(parent);
        lowPassEffect = new LowPassEffect(parent);
        loFiCrusherEffect = new LoFiCrusherEffect(parent);
    }

    public void draw() {
        updateEffectsParameters();
            updateCam();
            currentFrame = cam;
            applyEffects();
            parent.image(currentFrame, 0, 0, parent.width, parent.height);
    }

    private void updateCam() {
        if (cam.available()) {
            cam.read();
        }
    }

    private void applyEffects() {
        currentFrame.loadPixels();
        if (midiControl.getKnobValue(Custom4KnobsKnobs.KNOB1) > 0) {
            loFiCrusherEffect.process(currentFrame);
        }

        if (midiControl.getKnobValue(Custom4KnobsKnobs.KNOB2) > 0
                || midiControl.getKnobValue(Custom4KnobsKnobs.KNOB3) > 0) {
            imageDelay.process(currentFrame);
        }

        if (midiControl.getKnobValue(Custom4KnobsKnobs.KNOB4) > 0) {
            lowPassEffect.process(currentFrame);
        }
        currentFrame.updatePixels();
    }

    private void updateEffectsParameters() {
        loFiCrusherEffect.setNormFreq(map(127 - midiControl.getKnobValue(Custom4KnobsKnobs.KNOB1), 0, 127, 0.001f, 0.7f));//mapKnobValue(LaunchControlKnobs.KNOB9, 0, 1) + 0.001f);

        imageDelay.setDecay(midiControl.mapKnobValue(Custom4KnobsKnobs.KNOB2, 0, 3));
        imageDelay.setIncrement(51 - (int) midiControl.mapKnobValue(Custom4KnobsKnobs.KNOB3, 1, 50));

        lowPassEffect.setAlpha(PApplet.constrain(1 - midiControl.mapKnobValue(Custom4KnobsKnobs.KNOB4, 0, 1), 0.01f, 1));
    }
}
