package abstractpainter.painter;

import abstractpainter.utils.ProcessingChild;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONObject;


public class LineGenerator extends ProcessingChild {

    private PGraphics canvas;

    private PVector point;
    private PVector lastPoint;

    private Integer color;
    private float speed;
    private float size;

    private float minSpeed, maxSpeed;
    private float minSize, maxSize;


    public LineGenerator(PApplet parent, PGraphics canvas) {
        super(parent);
        this.canvas = canvas;
        point = new PVector(parent.random(parent.width), parent.random(parent.height));
        lastPoint = new PVector(point.x, point.y);
        size = 10;
        speed = 50;
        loadConfig();
    }

    private void loadConfig() {
        JSONObject configJson = parent.loadJSONObject("config.json");
        minSize = configJson.getJSONObject("size").getFloat("min", 2);
        maxSize = configJson.getJSONObject("size").getFloat("max", 50);
        minSpeed = configJson.getJSONObject("speed").getFloat("min", 20);
        maxSpeed = configJson.getJSONObject("speed").getFloat("max", 80);
    }

    public void drawOnCanvas(Integer color) {
        this.color = color;
        update();
        canvas.beginDraw();
        style();
        canvas.line(point.x, point.y, lastPoint.x, lastPoint.y);
        canvas.endDraw();
    }

    private void style() {
        canvas.fill(color);
        canvas.stroke(color);
        canvas.strokeWeight(size);
    }

    private void update() {
        updatePosition();
        updateSpeed();
        updateSize();
    }

    private void updatePosition() {
        lastPoint.x = point.x;
        lastPoint.y = point.y;
        point.x = addNoise(point.x, parent.width);
        point.y = addNoise(point.y, parent.height);
    }

    private void updateSpeed() {
        float warmness = canvas.red(color) - canvas.blue(color);
        speed = PApplet.map(warmness, -255, 255, minSpeed, maxSpeed);
    }

    private void updateSize() {
        size = PApplet.constrain(size + parent.random(-1, 1), minSize, maxSize);
    }

    private float addNoise(float value, int maxValue) {
        float noise = parent.noise(value);
        if (parent.random(1) > 0.5) {
            noise *= -1;
        }
        return PApplet.constrain(value + noise * speed, 0, maxValue);
    }
}
