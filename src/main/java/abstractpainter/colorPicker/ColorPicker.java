package abstractpainter.colorPicker;

import abstractpainter.utils.ProcessingChild;
import gab.opencv.OpenCV;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Capture;

import java.awt.*;

public class ColorPicker extends ProcessingChild {

    public static final int COLORS_COUNT = 4;

    private Capture cam;
    private OpenCV opencv;

    private PImage frame;
    private Integer[] colors;
    private Rectangle[] detectedFaces;
    private PVector[] probePoints;

    public ColorPicker(final PApplet parent) {
        super(parent);
        setupCamera();
        setupOpenCv();
        colors = new Integer[COLORS_COUNT];
        probePoints = new PVector[COLORS_COUNT];
        for (int i = 0; i < COLORS_COUNT; i++) {
            colors[i] = 0;
            probePoints[i] = new PVector(0, 0);
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
            updateProbePoints();
            for (int i = 0; i < COLORS_COUNT; i++) {
                colors[i] = cam.get((int) probePoints[i].x, (int) probePoints[i].y);
            }
        }
    }

    private void updateProbePoints() {
        probePoints[0].x = detectedFaces[0].x + detectedFaces[0].width / 2;
        probePoints[0].y = detectedFaces[0].y + detectedFaces[0].height / 2;
        probePoints[1].x = detectedFaces[0].x;
        probePoints[1].y = detectedFaces[0].y + 2 * detectedFaces[0].height;
        probePoints[2].x = detectedFaces[0].x + detectedFaces[0].width;
        probePoints[2].y = detectedFaces[0].y + 2 * detectedFaces[0].height;
        probePoints[3].x = detectedFaces[0].x + detectedFaces[0].width / 2;
        probePoints[3].y = detectedFaces[0].y + 2.5f * detectedFaces[0].height;
    }

    public boolean isPersonTooClose() {
        return probePoints[3].y > cam.height;
    }

    public void draw() {
//        parent.background(0);
        parent.pushMatrix();
        parent.translate(parent.width - frame.width, 0);
        parent.pushStyle();
        parent.image(frame, 0, 0);
        parent.noFill();
        parent.stroke(255, 0, 0);
        parent.strokeWeight(2);
        if (isDetected()) {
            for (Rectangle rect : detectedFaces) {
                 parent.rect(rect.x, rect.y, rect.width, rect.height);
            }
            for (int i = 0; i < COLORS_COUNT; i++) {
                if (probePoints[i].y < frame.height){
                    parent.stroke(255, 0, 0);
                    parent.strokeWeight(2);
                    parent.fill(colors[i]);
                    parent.ellipseMode(PConstants.CENTER);
                    parent.ellipse(probePoints[i].x, probePoints[i].y, 10, 10);
                }
            }
        }
        parent.popStyle();
        parent.popMatrix();
    }

    public Integer[] getColors() {
        return colors;
    }

    public void saveFrame(final String id) {
        frame.save("output/" + id + "_source.png");
    }
}
