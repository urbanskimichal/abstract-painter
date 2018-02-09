package abstractpainter;

import processing.core.PApplet;

public class App extends PApplet {

    public void settings() {
        size(1280, 720, P3D);
    }

    public void setup() {
        stroke(255);
    }

    public void draw() {
        background(0);
        rect(20, 20, width - 40, height - 40);
    }

    public static void main(String[] args) {
        PApplet.main(App.class.getCanonicalName());
    }
}
