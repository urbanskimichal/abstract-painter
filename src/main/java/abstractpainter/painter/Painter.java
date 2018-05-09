package abstractpainter.painter;

import abstractpainter.colorPicker.ColorPicker;
import abstractpainter.utils.ProcessingChild;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class Painter extends ProcessingChild {

    private PGraphics canvas;

    private Integer[] colors;
    private LineGenerator[] lineGenerators;

    public Painter(PApplet parent, Integer[] colors) {
        super(parent);
        this.colors = colors;
        initCanvas();
        initLineGenerators();
    }

    private void initCanvas() {
        canvas = parent.createGraphics(parent.width, parent.height);
        canvas.beginDraw();
        canvas.strokeCap(PConstants.SQUARE);
        canvas.background(0);
        canvas.endDraw();
    }

    private void initLineGenerators() {
        lineGenerators = new LineGenerator[ColorPicker.COLORS_COUNT];
        for (int i = 0; i < ColorPicker.COLORS_COUNT; i++) {
            lineGenerators[i] = new LineGenerator(parent, canvas);
        }
    }

    public void resetCanvas() {
        canvas.beginDraw();
        canvas.background(0);
        canvas.endDraw();
    }

    public void drawOnCanvas() {
        for (int i = 0; i < ColorPicker.COLORS_COUNT; i++) {
            lineGenerators[i].drawOnCanvas(colors[i]);
        }
    }

    public void draw() {
        parent.image(canvas, 0, 0, parent.width, parent.height);
    }

    public void saveCanvas(final String id) {
        canvas.save("output/" + id + "_picture.png");
    }
}
