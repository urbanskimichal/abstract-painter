package com.tajnyprojekt.glitchEditor.midi.serialToMidi;


import com.tajnyprojekt.glitchEditor.midi.devices.custom4Knobs.Custom4KnobsMapper;
import processing.core.PApplet;
import processing.serial.Serial;
import themidibus.ControlChange;

import java.util.ArrayList;
import java.util.List;

public class SerialToMidi {

    private Serial serial;

    public SerialToMidi(PApplet parent) {
        Serial.list();
        serial = new Serial(parent, Serial.list()[0],38400);
    }

    public List<ControlChange> serialToMidi() {
        List<ControlChange> controlChanges = new ArrayList<>();
        boolean received = false;
        while (serial.available() > 0) {
            String inBuffer = serial.readStringUntil('\n');
            if (inBuffer != null) {
                inBuffer = inBuffer.substring(0, inBuffer.length() - 2);
                ControlChange controlChange = parseControlChange(inBuffer);
                if (controlChange != null) {
                    controlChanges.add(controlChange);
                }
                received = true;
            }
        }

        if (received) {
            return controlChanges;
        }
        return null;
    }

    private ControlChange parseControlChange(String msg) {
        String[] args = msg.split(" ");
        if (args.length > 1) {
            ControlChange controlChange = new ControlChange(0, 0, 0);
            controlChange.setChannel(0);
            controlChange.setNumber(Integer.parseInt(args[0]));
            controlChange.setValue(Integer.parseInt(args[1]));
            controlChange.bus_name = Custom4KnobsMapper.BUS_NAME;
            return controlChange ;
        }
        return null;
    }
}
