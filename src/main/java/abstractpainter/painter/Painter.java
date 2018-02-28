package abstractpainter.painter;

import abstractpainter.colorPicker.ColorPicker;
import abstractpainter.utils.ProcessingChild;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class Painter extends ProcessingChild {

    private PGraphics canvas;

    private Integer[] colors;
    private PVector[] points;
    private PVector[] lastPoints;

    private float pointSpeed;
    private float pointSize;

    public Painter(PApplet parent, Integer[] colors) {
        super(parent);
        this.colors = colors;
        points = new PVector[ColorPicker.COLORS_COUNT];
        lastPoints = new PVector[ColorPicker.COLORS_COUNT];
        for (int i = 0; i < points.length; i++) {
            points[i] = new PVector(parent.random(parent.width), parent.random(parent.height));
            lastPoints[i] = new PVector(points[i].x, points[i].y);
        }
        pointSize = 10;
        pointSpeed = 50;
        initCanvas();
    }

    private void initCanvas() {
        canvas = parent.createGraphics(parent.width, parent.height);
        canvas.beginDraw();
        canvas.strokeCap(PConstants.SQUARE);
        canvas.background(0);
        canvas.endDraw();
    }

    public void draw() {
        parent.image(canvas, 0, 0, parent.width, parent.height);
    }

    public void resetCanvas() {
        canvas.beginDraw();
        canvas.background(0);
        canvas.endDraw();
    }

    public void drawOnCanvas() {
        updatePoints();
        canvas.beginDraw();
        for (int i = 0; i < points.length; i++) {
            styleLine(i);
            canvas.line(points[i].x, points[i].y, lastPoints[i].x, lastPoints[i].y);
        }
        canvas.endDraw();
    }

    private void styleLine(int index) {
        canvas.fill(colors[index]);
        canvas.stroke(colors[index]);
        canvas.strokeWeight(pointSize);
    }

    private void updatePoints() {
        for (int i = 0; i < points.length; i++) {
            lastPoints[i].x = points[i].x;
            lastPoints[i].y = points[i].y;
            points[i].x = addNoise(points[i].x, parent.width);
            points[i].y = addNoise(points[i].y, parent.height);
        }
    }

    private float addNoise(float value, int maxValue) {
        float noise = parent.noise(value);
        if (parent.random(1) > 0.5) {
            noise *= -1;
        }
        return PApplet.constrain(value + noise * pointSpeed, 0, maxValue);
    }

    public void saveCanvas(final String id) {
        canvas.save("output/" + id + "_picture.png");
    }
}
