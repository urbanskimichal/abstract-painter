package com.tajnyprojekt.glitchEditor.midi.devices.custom4Knobs.controls;


public enum Custom4KnobsKnobs {

    KNOB1(0),
    KNOB2(1),
    KNOB3(2),
    KNOB4(3);

    private int controlNumber;

    Custom4KnobsKnobs(int controlNumber) {
        this.controlNumber = controlNumber;
    }

    public int getControlNumber() {
        return controlNumber;
    }
}
