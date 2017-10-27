import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame implements GLEventListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private BoundingBox boundingBox;

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        super("CSCI 5631 - Program #2");
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        GLCanvas glCanvas = new GLCanvas(glCapabilities);
        glCanvas.addGLEventListener(this);
        this.setName("CSCI 5631 - Program #2");
        this.getContentPane().add(glCanvas);
        this.setSize(1200, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        glCanvas.requestFocusInWindow();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        GLU glu = new GLU();
        glu.gluOrtho2D(0, 600, 0, 600);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        Point2D[] randomPoints = generateRandomPoints();
        Line2D[] randomLines = generateRandomLines();
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl2.glLoadIdentity();
        //for the main view with clipping window
        gl2.glViewport(0,0,600,600);
        drawClippingWindow(gl2);
        drawPoints(randomPoints, gl2);
        drawLines(randomLines, gl2);

        //not yet - for the viewport of clipping window
        // gl2.glViewport(600,0,600,600);

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    //TODO: handle user input (allow the user to enter the number of points & lines they want, allow moving of clipping window, etc)
    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    private Point2D[] generateRandomPoints() {
        int min = 1;
        int max = 600;
        //limit the number of points to a maximum of 35
        int maxNumPoints = 35;
        int numPoints = (int)(Math.random() * (maxNumPoints - min) + min);
        Point2D[] points = new Point2D[numPoints];
        for (int i=0; i<points.length; i++) {
            points[i] = new Point2D((int)(Math.random() * (max-min) + min), (int)(Math.random() * (max-min) + min));
        }

        return points;
    }

    private Line2D[] generateRandomLines() {
        int min = 1;
        int max = 600;
        //limit the number of lines to a maximum of 35
        int maxNumLines = 35;
        int numLines = (int)(Math.random() * (maxNumLines - min) + min);
        Line2D[] lines = new Line2D[numLines];
        for (int i=0; i<lines.length; i++) {
            lines[i] = new Line2D((float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min, (float)Math.random()* (max-min) + min);
        }

        return lines;
    }

    private void drawPoints(Point2D[] points, GL2 gl2) {
        gl2.glPointSize(5.0f);

        for (Point2D p : points) {
            gl2.glBegin(GL2.GL_POINTS);
            if (this.boundingBox.contains(p)) {
                gl2.glColor3f(1.0f, 0.0f, 0.0f);
            }
            else {
                setRandomColor(gl2);
            }
            gl2.glVertex2i((int)p.getX(), (int)p.getY());
            gl2.glEnd();
        }
    }

    private void drawLines(Line2D[] lines, GL2 gl2) {
        for (Line2D line : lines) {
            gl2.glBegin(GL2.GL_LINES);
            double dx = line.x2 - line.x1;
            double dy = line.y2 - line.y1;
            for (int x = (int) line.x1; x<=line.x2; x++){
                int y = (int)(line.y1 + dy * (x - line.x1) / dx);
                if (this.boundingBox.contains(new Point2D(x, y))) {
                    gl2.glColor3f(1.0f, 0.0f, 0.0f);
                }
                else {
                    setRandomColor(gl2);
                }
            }
            gl2.glVertex2i((int) line.x1, (int) line.y1);
            gl2.glVertex2i((int) line.x2, (int) line.y2);
            gl2.glEnd();
        }
    }

    private void drawClippingWindow(GL2 gl2) {
        //set the color to red
        gl2.glColor3f(1.0f, 0.0f, 0.0f);
        //draw the clipping window outline
        gl2.glBegin(gl2.GL_LINES);
        //L1
        gl2.glVertex2i(100, 200);
        gl2.glVertex2i(100, 400);
        //L2
        gl2.glVertex2i(100, 400);
        gl2.glVertex2i(500, 400);
        //L3
        gl2.glVertex2i(500, 400);
        gl2.glVertex2i(500, 200);
        //L4
        gl2.glVertex2i(100, 200);
        gl2.glVertex2i(500, 200);
        gl2.glEnd();
        //set bounding box to the bounds of clipping window.
        this.boundingBox = new BoundingBox(200, 200, 300, 200);
    }

    private void setRandomColor(GL2 gl2) {
        //TODO: random color can't be red! (this would be confusing if something is bordering the clipping window)
        float color1, color2, color3;
        int min = 0;
        int max = 1;
        color1 = (float)(Math.random() * (0.5-min) + min);
        color2 = (float)(Math.random() * (max-min) + min);
        color3 = (float)(Math.random() * (max-min) + min);
        gl2.glColor3f(color1, color2, color3);
    }
}
