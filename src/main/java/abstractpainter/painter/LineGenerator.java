package abstractpainter.painter;

import abstractpainter.utils.ProcessingChild;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class LineGenerator extends ProcessingChild {

    private PGraphics canvas;

    private PVector point;
    private PVector lastPoint;

    private Integer color;
    private float speed;
    private float size;

    public LineGenerator(PApplet parent, Integer color, PGraphics canvas) {
        super(parent);
        this.color = color;
        this.canvas = canvas;
        point = new PVector(parent.random(parent.width), parent.random(parent.height));
        lastPoint = new PVector(point.x, point.y);
        size = 10;
        speed = 50;
    }

    public void draw() {
        update();
        style();
        canvas.line(point.x, point.y, lastPoint.x, lastPoint.y);
    }

    private void style() {
        canvas.fill(color);
        canvas.stroke(color);
        canvas.strokeWeight(size);
    }

    private void update() {
        lastPoint.x = point.x;
        lastPoint.y = point.y;
        point.x = addNoise(point.x, parent.width);
        point.y = addNoise(point.y, parent.height);
    }

    private float addNoise(float value, int maxValue) {
        float noise = parent.noise(value);
        if (parent.random(1) > 0.5) {
            noise *= -1;
        }
        return PApplet.constrain(value + noise * speed, 0, maxValue);
    }
}
