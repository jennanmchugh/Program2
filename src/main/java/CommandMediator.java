import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import utils.ExtendedLine;
import utils.ExtendedPoint;

import javax.swing.*;
import java.awt.*;

/**
 * The public methods in this class will be called by JOGLEventMediator when an event-based action is required.
 */
public class CommandMediator {
    private static final int BBOX_MINX = 1;
    private static final int BBOX_MAXX = 600;
    private static final int BBOX_MINY = 1;
    private static final int BBOX_MAXY = 570;
    private static final double XvMin = 600;
    private static final double XvMax = 1200;
    private static final double YvMin = 0;
    private static final double YvMax = 600;
    private BoundingBox boundingBox = Program2.boundingBox;
    private ExtendedPoint[] randomPoints = generateRandomPoints();
    private ExtendedLine[] randomLines = generateRandomLines();
    private ExtendedPoint[] fixedPoints;
    private ExtendedLine[] fixedLines;

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Empty constructor
     */
    public CommandMediator() {
    }

    /**
     * Draws points and lines on the viewports
     * Called from JOGLEventMediator.display() method.
     * @param glAutoDrawable Where the points and lines will be drawn.
     */
    public void draw(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl2.glLoadIdentity();
        gl2.glViewport(0,0,1200,600);

//        if (Program2.numPoints > 0 && Program2.numLines > 0){
//            fixedPoints = generateFixedPoints(Program2.numPoints);
//            fixedLines = generateFixedLines(Program2.numLines);
//            drawPoints(fixedPoints, gl2);
//            drawLines(fixedLines, gl2);
//            drawShapesInViewport(fixedPoints, fixedLines, gl2);
//            drawClippingWindow(gl2);
//        }

        drawPoints(randomPoints, gl2);
        drawLines(randomLines, gl2);
        drawShapesInViewport(randomPoints, randomLines, gl2);
        drawClippingWindow(gl2);

        gl2.glFlush();
    }

    /**
     * Initializes glAutoDrawable
     * Called from JOGLEventMediator.init() method.
     * @param glAutoDrawable
     */
    public void initialize(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        GLU glu = new GLU();
        glu.gluOrtho2D(0, 1200, 0, 600);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    /**
     * Moves clipping window by x or y units in the x or y direction.
     * Called from JOGLEventMediator.keyPressed() method.
     * @param x float value by which the clipping window should move in the x direction
     * @param y float value by which the clipping window should move in the y direction
     */
    public void moveClippingWindowBy(float x, float y) {
        if ((boundingBox.getMinX() + x >= BBOX_MINX) && (boundingBox.getMinY() + y >= BBOX_MINY)) {
            if ((boundingBox.getMaxX() + x <= BBOX_MAXX) && (boundingBox.getMaxY() + y <= BBOX_MAXY)) {
                boundingBox = new BoundingBox(boundingBox.getMinX() + x, boundingBox.getMinY() + y, Program2.MAX_BBOX_WIDTH, Program2.MAX_BBOX_HEIGHT);
            }
        }
    }

    /** TODO: implement this method
     * Scales the clipping window by either enlarging or shrinking it by a fixed scale value
     */
    public void scaleClippingWindow(float sx, float sy) {
          double newMinX = boundingBox.getMinX() * sx;
          double newMaxX = boundingBox.getMaxX() * sx;
          double newMinY = boundingBox.getMinY() * sy;
          double newMaxY = boundingBox.getMaxY() * sy;
        if ((boundingBox.getMinX() * sx >= BBOX_MINX) && (boundingBox.getMinY() * sy >= BBOX_MINY)) {
            if ((boundingBox.getMaxX() * sx <= BBOX_MAXX) && (boundingBox.getMaxY() * sy <= BBOX_MAXY)) {
                boundingBox = new BoundingBox(boundingBox.getMinX() * sx, boundingBox.getMinY() * sy, Program2.MAX_BBOX_WIDTH, Program2.MAX_BBOX_HEIGHT);
            }
        }
    }

    /**
     * This method will create a random number of points with the range being anywhere from 1-30 points
     * Min x,y value for points = 1
     * Max x,y value for points = 600 (Max of viewport)
     * @return ExtendedPoint[] of randomly generated points
     */
    private ExtendedPoint[] generateRandomPoints() {
        int min = 1;
        int max = 600;
        int maxNumPoints = 30;
        int numPoints = (int)(Math.random() * (maxNumPoints - min) + min);
        ExtendedPoint[] points = new ExtendedPoint[numPoints];
        for (int i=0; i<points.length; i++) {
            points[i] = new ExtendedPoint((int)(Math.random() * (max-min) + min), (int)(Math.random() * (max-min) + min));
        }

        return points;
    }

    /**
     * This method will create a fixed number of points based on user input.
     * Min x,y value for points = 1
     * Max x,y value for points = 600 (Max of viewport)
     * @return ExtendedPoint[] of randomly generated points
     */
    private ExtendedPoint[] generateFixedPoints(int pointSize) {
        int min = 1;
        int max = 600;
        ExtendedPoint[] points = new ExtendedPoint[pointSize];
        for (int i=0; i<points.length; i++) {
            points[i] = new ExtendedPoint((int)(Math.random() * (max-min) + min), (int)(Math.random() * (max-min) + min));
        }

        return points;
    }

    /**
     * This method will create a random number of lines with the range being anywhere from 1-15 lines.
     * Min x,y value for line points = 1
     * Max x,y value for line points = 600 (Max. of viewport)
     * @return ExtendedLine[] of randomly generated lines
     */
    private ExtendedLine[] generateRandomLines() {
        int min = 1;
        int max = 600;
        int maxNumLines = 15;
        int numLines = (int)(Math.random() * (maxNumLines - min) + min);
        ExtendedLine[] lines = new ExtendedLine[numLines];
        for (int i=0; i<lines.length; i++) {
            lines[i] = new ExtendedLine((float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min);
        }

        return lines;
    }

    /**
     * This method will create a fixed number of lines based on user input.
     * Min x,y value for line points = 1
     * Max x,y value for line points = 600 (Max. of viewport)
     * @return ExtendedLine[] of randomly generated lines
     */
    private ExtendedLine[] generateFixedLines(int numOfLines) {
        int min = 1;
        int max = 600;
        ExtendedLine[] lines = new ExtendedLine[numOfLines];
        for (int i=0; i<lines.length; i++) {
            lines[i] = new ExtendedLine((float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min);
        }

        return lines;
    }

    /**
     * This method will render the points on the main viewport.
     * If a point is within the clipping window, that point will be red.
     * All other points will be a randomly generated color.
     * @param points The randomly generated (or user-selected) points to be drawn.
     * @param gl2
     */
    private void drawPoints(ExtendedPoint[] points, GL2 gl2) {
        gl2.glPointSize(5.0f);

        for (ExtendedPoint p : points) {
            gl2.glBegin(GL2.GL_POINTS);
            if (boundingBox.contains(p)) {
                gl2.glColor3f(1.0f, 0.0f, 0.0f);
            }
            else {
                if (p.getPointColor() != null) {
                    gl2.glColor3f(p.getPointColor()[0], p.getPointColor()[1], p.getPointColor()[2]);
                }
                else {
                    p.setPointColor(getRandomColor(gl2));
                    gl2.glColor3f(p.getPointColor()[0], p.getPointColor()[1], p.getPointColor()[2]);
                }
            }
            gl2.glVertex2f((float)p.getX(), (float)p.getY());
            gl2.glEnd();
        }
    }

    /**
     * This method will render the lines on the main viewport.
     * If any point on the line is within the clipping window, that point will be red.
     * All other points on the line will be a randomly generated color.
     * @param lines The randomly generated (or user-selected) lines to be drawn.
     * @param gl2
     */
    private void drawLines(ExtendedLine[] lines, GL2 gl2) {
        for (ExtendedLine line : lines) {
            gl2.glPointSize(2.0f);
            gl2.glBegin(GL2.GL_POINTS);
            //draw each point on the line
            double dx = line.x2 - line.x1;
            double dy = line.y2 - line.y1;
            for (double x = (int) line.x1; x<=line.x2; x++) {
                double y = (line.y1 + dy * (x - line.x1) / dx);
                if (boundingBox.contains(x, y)) {
                    gl2.glColor3f(1.0f, 0.0f, 0.0f);
                }
                else {
                    if (line.getLineColor() != null) {
                        gl2.glColor3f(line.getLineColor()[0], line.getLineColor()[1], line.getLineColor()[2]);
                    }
                    else {
                        line.setLineColor(getRandomColor(gl2));
                        gl2.glColor3f(line.getLineColor()[0], line.getLineColor()[1], line.getLineColor()[2]);
                    }
                }
                gl2.glVertex2f((float)x,(float)y);
            }
            gl2.glEnd();
        }
    }

    /**
     * This method iterates through all the points contained within the clipping window
     * and then scales the points to the viewport for a "zoom" effect.
     * @param points The randomly generated (or user-selected) points to be rendered.
     * @param lines The randomly generated (or user-selected) lines to be rendered.
     * @param gl2
     */
    private void drawShapesInViewport(Point2D[] points, Line2D[] lines, GL2 gl2) {
        double XwMax = boundingBox.getMaxX();
        double XwMin = boundingBox.getMinX();
        double YwMax = boundingBox.getMaxY();
        double YwMin = boundingBox.getMinY();

        //set up equations for sx, sy, tx, ty - from Chapter 4 notes on mapping a window to a viewport.
        double sx = (XvMax - XvMin)/(XwMax - XwMin);
        double sy = (YvMax - YvMin)/(YwMax - YwMin);
        double tx = (XwMax*XvMin - XwMin*XvMax)/(XwMax - XwMin);
        double ty = (YwMax*YvMin - YwMin*YvMax)/(YwMax - YwMin);

        for (Point2D p : points) {
            gl2.glPointSize(5.0f);
            if (boundingBox.contains(p)) {
                double xv = sx*p.getX() + tx;
                double yv = sy*p.getY() + ty;
                gl2.glBegin(GL2.GL_POINTS);
                gl2.glColor3f(1.0f, 0.0f, 0.0f);
                gl2.glVertex2f((float)xv, (float)yv);
                gl2.glEnd();
            }
        }

        for (Line2D line : lines) {
            //change point size back to original size
            gl2.glPointSize(2.0f);

            //draw each point on the line
            double dx = line.x2 - line.x1;
            double dy = line.y2 - line.y1;
            for (double x = (int) line.x1; x<=line.x2; x++) {
                double y = (line.y1 + dy * (x - line.x1) / dx);
                if (boundingBox.contains(x, y)) {
                    double xv = sx*x + tx;
                    double yv = sy*y + ty;
                    gl2.glBegin(GL2.GL_POINTS);
                    gl2.glColor3f(1.0f, 0.0f, 0.0f);
                    gl2.glVertex2f((float)xv,(float)yv);
                    gl2.glEnd();
                }
            }
        }
    }

    /**
     * This method will draw the red-bordered clipping window on the main viewport based on bounding box values.
     * @param gl2
     */
    private void drawClippingWindow(GL2 gl2) {
        gl2.glColor3f(1.0f, 0.0f, 0.0f);
        gl2.glBegin(gl2.GL_LINES);
        //L1
        gl2.glVertex2i((int)boundingBox.getMinX(), (int) boundingBox.getMinY());
        gl2.glVertex2i((int)boundingBox.getMinX(), (int) boundingBox.getMaxY());
        //L2
        gl2.glVertex2i((int)boundingBox.getMinX(), (int) boundingBox.getMaxY());
        gl2.glVertex2i((int)boundingBox.getMaxX(), (int) boundingBox.getMaxY());
        //L3
        gl2.glVertex2i((int)boundingBox.getMaxX(), (int) boundingBox.getMaxY());
        gl2.glVertex2i((int)boundingBox.getMaxX(), (int) boundingBox.getMinY());
        //L4
        gl2.glVertex2i((int)boundingBox.getMinX(), (int) boundingBox.getMinY());
        gl2.glVertex2i((int)boundingBox.getMaxX(), (int) boundingBox.getMinY());
        gl2.glEnd();
    }

    /**
     * This method will generate a random color. It will NOT return a red color.
     * @param gl2
     * @return float[] of red, green, blue values
     */
    private float[] getRandomColor(GL2 gl2) {
        float color1, color2, color3;
        int min = 0;
        int max = 1;
        color1 = (float)(Math.random() * (0.5-min) + min);
        color2 = (float)(Math.random() * (max-min) + min);
        color3 = (float)(Math.random() * (max-min) + min);
        gl2.glColor3f(color1, color2, color3);

        return new float[]{color1, color2, color3};
    }

    /**
     * A simple method to return the center point of a bounding box. May be used with scaling and rotation.
     * @param bbox The bounding box of the clipping window
     * @return Point2D the center coordinate of the bounding box
     */
    public Point2D getBoundingBoxCenter(BoundingBox bbox) {
        return new Point2D((bbox.getMinX() + bbox.getMaxX())/2, (bbox.getMinY() + bbox.getMaxY())/2);
    }

}
