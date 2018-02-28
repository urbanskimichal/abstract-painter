package abstractpainter;

import abstractpainter.colorPicker.ColorPicker;
import abstractpainter.painter.Painter;
import processing.core.PApplet;

import java.util.UUID;

public class App extends PApplet {

    private ColorPicker colorPicker;
    private Painter painter;

    private String sessionId;

    private int lastDetectedTime;
    private boolean isCanvasSaved;
    private boolean colorPickerDebugMode;

    public void settings() {
        size(1280, 720);
    }

    public void setup() {
        isCanvasSaved = true;
        colorPickerDebugMode = false;
        colorPicker = new ColorPicker(this);
        painter = new Painter(this, colorPicker.getColors());
        background(0);
        lastDetectedTime = millis() - 1000;
    }

    public void draw() {
        updateFrameTitle();
        colorPicker.update();

        if (colorPicker.isDetected()) {
            lastDetectedTime = millis();
        }

        if (millis() - lastDetectedTime < 2000) {
            startDrawingSession();
            painter.drawOnCanvas();
        }
        else {
            endDrawingSession();
        }

        if (colorPickerDebugMode) {
            colorPicker.draw();
        }
        else {
            painter.draw();
        }

        drawPalette();
    }

    private void startDrawingSession() {
        if (isCanvasSaved) {
            System.out.println("reset canvas");
            painter.resetCanvas();
            sessionId = UUID.randomUUID().toString();
            colorPicker.saveFrame(sessionId);
            isCanvasSaved = false;
        }
    }

    private void endDrawingSession() {
        if (!isCanvasSaved) {
            painter.saveCanvas(sessionId);
            isCanvasSaved = true;
        }
    }
    
    private void drawPalette() {
        pushStyle();
        noStroke();
        final int rectSize = 100;
        int xOffset = width / 2 - ColorPicker.COLORS_COUNT * rectSize / 2;
        for (Integer color : colorPicker.getColors()) {
            fill(color);
            rect(xOffset, height - rectSize, rectSize, rectSize);
            xOffset += rectSize;
        }
        popStyle();
    }

    public void keyPressed() {
        if (key == ' ') {
            background(0);
            colorPickerDebugMode = !colorPickerDebugMode;
        }
    }

    private void updateFrameTitle() {
        surface.setTitle((int)frameRate + "fps");
    }

    public static void main(String[] args) {
        PApplet.main(App.class.getCanonicalName());
    }
}
