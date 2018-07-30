package com.tajnyprojekt.glitchEditor.midi.control;

import com.tajnyprojekt.glitchEditor.midi.devices.custom4Knobs.Custom4KnobsMapper;
import com.tajnyprojekt.glitchEditor.midi.devices.custom4Knobs.controls.Custom4KnobsKnobs;
import processing.core.PApplet;
import themidibus.ControlChange;
import themidibus.MidiBus;

public class MidiControl {

    private Custom4KnobsMapper custom4KnobsMapper;

    private static MidiControl instance = new MidiControl();

    // exists only to prevent instantiation.
    protected MidiControl() {}

    public static MidiControl getInstance() {
        return instance;
    }

    public void init(PApplet parent, int inputNumber, int outputNumber) {
        MidiBus.list();

        MidiBus custom4KnobsMidiBus = new MidiBus(parent, inputNumber, outputNumber, Custom4KnobsMapper.BUS_NAME);
        custom4KnobsMapper = new Custom4KnobsMapper(custom4KnobsMidiBus);
    }

    public void controllerChange(ControlChange controllerChange) {
        logControlChange(controllerChange);
        if(controllerChange.bus_name.equals(Custom4KnobsMapper.BUS_NAME)) {
            custom4KnobsMapper.controllerChange(controllerChange);
        }
    }

    private void logControlChange(ControlChange controllerChange) {
        System.out.println();
        System.out.println("Controller Change:");
        System.out.println("--------");
        System.out.println("Bus name:" + controllerChange.bus_name);
        System.out.println("Channel:" + controllerChange.channel());
        System.out.println("Number:" + controllerChange.number());
        System.out.println("Value:" + controllerChange.value());
    }

    public float mapKnobValue(Custom4KnobsKnobs knob, int min, int max) {
        return PApplet.map(getKnobValue(knob), 0, 127, min, max);
    }

    public int getKnobValue(Custom4KnobsKnobs knob) {
        return custom4KnobsMapper.getKnobValue(knob);
    }
}
