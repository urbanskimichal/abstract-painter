package com.tajnyprojekt.glitchEditor.midi.devices.deviceMapper;

import themidibus.ControlChange;
import themidibus.MidiBus;
import themidibus.Note;

import java.util.HashMap;

public abstract class DeviceMapper {

    protected MidiBus midiBus;

    public DeviceMapper(MidiBus midiBus) {
        this.midiBus = midiBus;
    }

    public abstract void noteOn(Note note);

    public abstract void noteOff(Note note);

    public abstract void controllerChange(ControlChange controllerChange);

    public abstract void printControlValues();

    protected void printHashMap(HashMap hashMap) {
        for (Object o : hashMap.entrySet()) {
            HashMap.Entry entry = (HashMap.Entry) o;
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
