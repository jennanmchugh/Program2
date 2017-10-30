import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The public methods in this class will be called by JOGLEventMediator when an event-based action is required.
 */
public class CommandMediator {
    private static final Logger logger = LoggerFactory.getLogger(CommandMediator.class);
    private BoundingBox boundingBox = Program2.boundingBox;
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
        Point2D[] randomPoints = generateRandomPoints();
        Line2D[] randomLines = generateRandomLines();
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl2.glLoadIdentity();
        gl2.glViewport(0,0,600,600);
        drawClippingWindow(gl2);
        drawPoints(randomPoints, gl2);
        drawLines(randomLines, gl2);

        //TODO: draw line/border in between viewports
        gl2.glViewport(600,0,600,600);
        drawShapesInViewport(randomPoints, randomLines, gl2);

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
        glu.gluOrtho2D(0, 600, 0, 600);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    /**
     * Moves clipping window by x or y units in the x or y direction.
     * Called from JOGLEventMediator.keyPressed() method.
     * @param x float value by which the clipping window should move in the x direction
     * @param y float value by which the clipping window should move in the y direction
     */
    public void moveClippingWindowBy(float x, float y) {
        Point2D bboxCenter = getBoundingBoxCenter(boundingBox);
        boundingBox = new BoundingBox(boundingBox.getMinX() + x, boundingBox.getMinY() + y, Program2.MAX_BBOX_WIDTH, Program2.MAX_BBOX_HEIGHT);
        logger.info("test");
    }

    /**
     * This method will create a random number of points with the range being anywhere from 1-30 points
     * Min x,y value for points = 1
     * Max x,y value for points = 600 (Max of viewport)
     * @return Point2D[] of randomly generated points
     */
    private Point2D[] generateRandomPoints() {
        int min = 1;
        int max = 600;
        //limit the number of points to a maximum of 30
        int maxNumPoints = 30;
        int numPoints = (int)(Math.random() * (maxNumPoints - min) + min);
        Point2D[] points = new Point2D[numPoints];
        for (int i=0; i<points.length; i++) {
            points[i] = new Point2D((int)(Math.random() * (max-min) + min), (int)(Math.random() * (max-min) + min));
        }

        return points;
    }

    /**
     * This method will create a random number of lines with the range being anywhere from 1-15 lines.
     * Min x,y value for line points = 1
     * Max x,y value for line points = 600 (Max. of viewport)
     * @return Line2D[] of randomly generated lines
     */
    private Line2D[] generateRandomLines() {
        int min = 1;
        int max = 600;
        //limit the number of lines to a maximum of 35
        int maxNumLines = 15;
        int numLines = (int)(Math.random() * (maxNumLines - min) + min);
        Line2D[] lines = new Line2D[numLines];
        for (int i=0; i<lines.length; i++) {
            lines[i] = new Line2D((float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min);
        }

        return lines;
    }

    /**
     * This method will render the points on the viewport.
     * If a point is within the clipping window, that point will be red.
     * All other points will be a randomly generated color.
     * @param points The randomly generated (or user-selected) points to be drawn.
     * @param gl2
     */
    private void drawPoints(Point2D[] points, GL2 gl2) {
        gl2.glPointSize(5.0f);

        for (Point2D p : points) {
            gl2.glBegin(GL2.GL_POINTS);
            setRandomColor(gl2);
            if (boundingBox.contains(p)) {
                gl2.glColor3f(1.0f, 0.0f, 0.0f);
            }
            gl2.glVertex2i((int)p.getX(), (int)p.getY());
            gl2.glEnd();
        }
    }

    /**
     * This method will render the lines on the viewport.
     * If any point on the line is within the clipping window, that point will be red.
     * All other points on the line will be a randomly generated color.
     * @param lines The randomly generated (or user-selected) lines to be drawn.
     * @param gl2
     */
    private void drawLines(Line2D[] lines, GL2 gl2) {
        for (Line2D line : lines) {
            //change point size back to original size
            gl2.glPointSize(2.0f);
            float[] newColor = setRandomColor(gl2);
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
                    //grab the random color that was first set
                    gl2.glColor3f(newColor[0], newColor[1], newColor[2]);
                }
                gl2.glVertex2f((float)x,(float)y);
            }
            gl2.glEnd();
        }
    }

    /**
     * This method simply takes all the points and lines that are within the clipping window bounds and
     * displays them in the clipping window's viewport.
     * @param points The randomly generated (or user-selected) points to be rendered.
     * @param lines The randomly generated (or user-selected) lines to be rendered.
     * @param gl2
     */
    private void drawShapesInViewport(Point2D[] points, Line2D[] lines, GL2 gl2) {
        //copy shapes from clipping window to viewport
        //TODO: use clipping algorithm so this is scaled to viewport
        gl2.glPointSize(5.0f);

        for (Point2D p : points) {
            if (boundingBox.contains(p)) {
                gl2.glBegin(GL2.GL_POINTS);
                gl2.glColor3f(1.0f, 0.0f, 0.0f);
                gl2.glVertex2i((int)p.getX(), (int)p.getY());
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
                    gl2.glBegin(GL2.GL_POINTS);
                    gl2.glColor3f(1.0f, 0.0f, 0.0f);
                    gl2.glVertex2f((float)x,(float)y);
                    gl2.glEnd();
                }
            }
        }
    }

    /**
     * This method will draw the red clipping window on the main viewport based on bounding box values.
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
    private float[] setRandomColor(GL2 gl2) {
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
    private Point2D getBoundingBoxCenter(BoundingBox bbox) {
        return new Point2D((bbox.getMinX() + bbox.getMaxX())/2, (bbox.getMinY() + bbox.getMaxY())/2);
    }

}