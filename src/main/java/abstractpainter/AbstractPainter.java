package abstractpainter;


import abstractpainter.colorPicker.ColorPicker;
import abstractpainter.painter.Painter;
import abstractpainter.utils.ProcessingChild;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.UUID;

public class AbstractPainter extends ProcessingChild {

    private ColorPicker colorPicker;
    private Painter painter;

    private String sessionId;

    private int lastDetectedTime;
    private boolean isCanvasSaved;
    private boolean colorPickerDebugMode;

    public AbstractPainter(final PApplet parent) {
        super(parent);
        isCanvasSaved = true;
        colorPickerDebugMode = false;
        colorPicker = new ColorPicker(parent);
        painter = new Painter(parent, colorPicker.getColors());
        lastDetectedTime = parent.millis() - 1000;
    }

    public void draw() {
        parent.background(0);
        colorPicker.update();

        if (colorPicker.isDetected() && !colorPicker.isPersonTooClose()) {
            lastDetectedTime = parent.millis();
        }

        if (parent.millis() - lastDetectedTime < 2000) {
            startDrawingSession();
            painter.drawOnCanvas();
        }
        else {
            endDrawingSession();
        }


        if (colorPicker.isPersonTooClose()) {
            parent.textAlign(PConstants.CENTER, PConstants.CENTER);
            parent.textSize(40);
            parent.text("Stoisz zbyt blisko...", parent.width / 2, parent.height / 2);
            painter.resetCanvas();
        }
        else {
            painter.draw();
            drawPalette();
        }

        colorPicker.draw();

    }

    private void startDrawingSession() {
        if (isCanvasSaved) {
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
        parent.pushStyle();
        parent.noStroke();
        final int rectSize = 100;
        int xOffset = parent.width / 2 - ColorPicker.COLORS_COUNT * rectSize / 2;
        for (Integer color : colorPicker.getColors()) {
            parent.fill(color);
            parent.rect(xOffset, parent.height - rectSize, rectSize, rectSize);
            xOffset += rectSize;
        }
        parent.popStyle();
    }
}
