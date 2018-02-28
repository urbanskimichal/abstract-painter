package abstractpainter.colorPicker;

import abstractpainter.utils.ProcessingChild;
import gab.opencv.OpenCV;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.video.Capture;

import java.awt.*;

public class ColorPicker extends ProcessingChild {

    public static final int COLORS_COUNT = 4;

    private Capture cam;
    private OpenCV opencv;

    private PImage frame;
    private Integer[] colors;
    private Rectangle[] detectedFaces;

    public ColorPicker(final PApplet parent) {
        super(parent);
        setupCamera();
        setupOpenCv();
        colors = new Integer[COLORS_COUNT];
        for (int i = 0; i < COLORS_COUNT; i++) {
            colors[i] = 0;
        }
        frame = parent.createImage(320, 240, PConstants.RGB);
    }

    private void setupCamera() {
        String[] cameras = Capture.list();

        if (cameras == null) {
            System.err.println("Failed to retrieve the list of available cameras, will try the default...");
            cam = new Capture(parent);
        } else if (cameras.length == 0) {
            System.err.println("There are no cameras available for capture.");
            parent.exit();
        } else {
            System.out.println("Available cameras:");
            PApplet.printArray(cameras);

            cam = new Capture(parent, 320, 240);//cameras[3]);

            // Start capturing the images from the camera
            cam.start();
        }
    }

    private void setupOpenCv() {
        opencv = new OpenCV(parent, 320, 240);
        opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);
    }

    public void update() {
        if (cam.available()) {
            cam.read();
            cam.loadPixels();
            frame = cam.get();
            detectFace();
            pickColors();
        }
    }

    private void detectFace() {
        if (cam.width > 0 && cam.height > 0) {
            opencv.loadImage(cam);
            detectedFaces = opencv.detect();
        }
    }

    public boolean isDetected() {
        return detectedFaces != null && detectedFaces.length > 0;
    }

    private void pickColors() {
        if (isDetected()) {
            colors[0] = cam.get(detectedFaces[0].x + detectedFaces[0].width / 2,
                    detectedFaces[0].y + detectedFaces[0].height / 2);
            colors[1] = cam.get(detectedFaces[0].x - detectedFaces[0].width / 2,
                    detectedFaces[0].y + 3 * detectedFaces[0].height / 2);
            colors[2] = cam.get(detectedFaces[0].x + 3 * detectedFaces[0].width / 2,
                    detectedFaces[0].y + 3 * detectedFaces[0].height / 2);
            colors[3] = cam.get(detectedFaces[0].x + detectedFaces[0].width / 2,
                    detectedFaces[0].y + detectedFaces[0].height / 2);
        }
    }

    public void draw() {
        parent.background(0);
        parent.pushStyle();
        parent.image(frame, 0, 0);
        parent.noFill();
        parent.stroke(255, 0, 0);
        parent.strokeWeight(2);
        if (isDetected()) {
            for (Rectangle rect : detectedFaces) {
                 parent.rect(rect.x, rect.y, rect.width, rect.height);
            }
        }
        for (int i = 0; i < COLORS_COUNT; i++) {
            parent.fill(colors[i]);
            parent.stroke(colors[i]);
            parent.rect(320, 60 * i, 60, 60);
        }
        parent.popStyle();
    }

    public Integer[] getColors() {
        return colors;
    }

    public void saveFrame(final String id) {
        frame.save("output/" + id + "_source.png");
    }
}
