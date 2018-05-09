package abstractpainter;

import processing.core.PApplet;

public class App extends PApplet {

    private AbstractPainter abstractPainter;

    public void settings() {
        size(1280, 720);
    }

    public void setup() {
        abstractPainter = new AbstractPainter(this);
    }

    public void draw() {
        abstractPainter.draw();
    }

    private void updateFrameTitle() {
        surface.setTitle((int)frameRate + "fps");
    }

    public static void main(String[] args) {
        PApplet.main(App.class.getCanonicalName());
    }
}
