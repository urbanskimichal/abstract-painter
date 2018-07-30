package com.tajnyprojekt.glitchEditor;

import com.tajnyprojekt.glitchEditor.glitcher.Glitcher;
import com.tajnyprojekt.glitchEditor.midi.control.MidiControl;
import com.tajnyprojekt.glitchEditor.midi.serialToMidi.SerialToMidi;
import processing.core.PApplet;
import processing.video.Capture;
import themidibus.ControlChange;

import javax.swing.*;
import java.util.List;

public class App extends PApplet {

    private SerialToMidi serialToMidi;
    private MidiControl midiControl;
    private Glitcher glitcher;

    public void settings() {
        size(1280, 720, P3D);
    }

    public void setup() {
        serialToMidi = new SerialToMidi(this);
        midiControl = MidiControl.getInstance();
        midiControl.init(this, 0, 3);
        glitcher = new Glitcher(this, selectCam());
    }

    private String selectCam() {
        JOptionPane.setRootFrame(frame);
        String[] camList = Capture.list();
        String s = (String) JOptionPane.showInputDialog(
                frame,
                "Select a camera and click ok",
                "Select cam",
                JOptionPane.PLAIN_MESSAGE,
                null,
                camList,
                "name=Integrated Camera,size=640x480,fps=15");
        System.out.println("Selected cam: " + s);
        return s;
    }

    public void draw() {
        updateFrameTitle();
        glitcher.draw();
        List<ControlChange> controlChanges = serialToMidi.serialToMidi();
        if (controlChanges != null) {
            for (ControlChange controlChange : controlChanges) {
                midiControl.controllerChange(controlChange);
            }
        }
    }

    private void updateFrameTitle() {
        surface.setTitle((int)frameRate + "fps");
    }

    public void controllerChange(ControlChange controllerChange) {
//        midiControl.controllerChange(controllerChange);
    }

    public static void main(String[] args) {
        PApplet.main(App.class.getCanonicalName());
    }
}
