package com.tajnyprojekt.glitchEditor.midi.devices.custom4Knobs;


import com.tajnyprojekt.glitchEditor.midi.devices.custom4Knobs.controls.Custom4KnobsKnobs;
import com.tajnyprojekt.glitchEditor.midi.devices.deviceMapper.DeviceMapper;
import themidibus.ControlChange;
import themidibus.MidiBus;
import themidibus.Note;
import java.util.HashMap;

public class Custom4KnobsMapper extends DeviceMapper {

    public static final String BUS_NAME = "knobs";
    private final int midiChannel = 0;

    private HashMap<Custom4KnobsKnobs, Integer> knobValues;

    public Custom4KnobsMapper(MidiBus midiBus) {
        super(midiBus);
        initKnobValues();
    }

    private void initKnobValues() {
        knobValues = new HashMap<>();
        for (Custom4KnobsKnobs knob : Custom4KnobsKnobs.values()) {
            knobValues.put(knob, 0);
        }
    }

    public void noteOn(Note note) {
        //this device doesn't send notes
    }

    public void noteOff(Note note) {
        //this device doesn't send notes
    }

    public void controllerChange(ControlChange controlChange) {
        if(isCorrectMidiChannel(controlChange.channel)) {
            mapKnobsControllerChange(controlChange);
        }
    }

    private boolean isCorrectMidiChannel(int channel) {
        return channel == midiChannel;
    }

    private void mapKnobsControllerChange(ControlChange controlChange) {
        for (Custom4KnobsKnobs knob : Custom4KnobsKnobs.values()) {
            if(controlChange.number == knob.getControlNumber()) {
                knobValues.put(knob, controlChange.value);
            }
        }
    }

    public void printControlValues() {
        printHashMap(knobValues);
    }

    public int getKnobValue(Custom4KnobsKnobs knob) {
        return knobValues.get(knob);
    }

    public void onExit() {
        midiBus.dispose();
        midiBus.close();
    }

}
