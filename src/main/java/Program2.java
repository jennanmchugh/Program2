import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import javafx.geometry.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


/**
 * Created by Jenna McHugh on 10/30/17.
 */
public class Program2 extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Program2.class);
    public static final int MAX_BBOX_WIDTH = 400;
    public static final int MAX_BBOX_HEIGHT = 200;
    private static final int MAX_WIDTH = 1200;
    private static final int MAX_HEIGHT = 600;
    public static BoundingBox boundingBox;

    public static void main(String[] args) {
        new Program2();
    }

    private Program2() {
        super("CSCI 5631 - Program #2");
        boundingBox = new BoundingBox(100, 200, MAX_BBOX_WIDTH, MAX_BBOX_HEIGHT);
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        GLCanvas glCanvas = new GLCanvas(glCapabilities);
        JOGLEventMediator listener = new JOGLEventMediator(glCanvas);
        glCanvas.addGLEventListener(listener);
        glCanvas.addKeyListener(listener);
        this.setName("CSCI 5631 - Program #2");
        this.getContentPane().add(glCanvas);
        this.setSize(MAX_WIDTH, MAX_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        glCanvas.requestFocusInWindow();
    }
}
