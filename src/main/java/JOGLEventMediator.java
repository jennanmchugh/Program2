import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class will handle OpenGL and Keyboard events, either directly in the class or by passing it to the CommandMediator class
 */
public class JOGLEventMediator implements GLEventListener, KeyListener {
    private static final Logger logger = LoggerFactory.getLogger(JOGLEventMediator.class);
    private GLCanvas glCanvas = null;
    private CommandMediator commandMediator;

    public JOGLEventMediator(GLCanvas glCanvas) {
        this.glCanvas = glCanvas;
        this.commandMediator = new CommandMediator();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        logger.debug("init method called");
        commandMediator.initialize(glAutoDrawable);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        logger.debug("display method called");
        commandMediator.draw(glAutoDrawable);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        logger.info("Key typed: " + String.valueOf(keyEvent.getKeyCode()));

        if (keyEvent.getKeyChar() == 'Q' || keyEvent.getKeyChar() == 'q') {
            //quit the program by using 'Q' key
            logger.info(Messages.QUIT_PROGRAM);
            System.exit(0);
        }
        if (keyEvent.getKeyChar() == 'R' || keyEvent.getKeyChar() == 'r') {
            logger.info(Messages.ROTATE_KEY_PRESSED);
            //OPTIONAL TODO: implement counterclockwise rotation of clipping window
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            logger.info(Messages.UP_KEY_PRESSED);
            this.commandMediator.moveClippingWindowBy(0.0f, 2.0f);
            this.refreshDisplay();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            logger.info(Messages.DOWN_KEY_PRESSED);
            this.commandMediator.moveClippingWindowBy(0.0f, -2.0f);
            this.refreshDisplay();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            logger.info(Messages.LEFT_KEY_PRESSED);
            this.commandMediator.moveClippingWindowBy(-2.0f, 0.0f);
            this.refreshDisplay();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            logger.info(Messages.RIGHT_KEY_PRESSED);
            this.commandMediator.moveClippingWindowBy(2.0f, 0.0f);
            this.refreshDisplay();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_ADD) {
            logger.info(Messages.ADD_KEY_PRESSED);
            //float[] fixedPts = this.commandMediator.scaleClippingWindow();
            //this.refreshDisplay();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_SUBTRACT) {
            logger.info(Messages.SUBTRACT_KEY_PRESSED);
            //float[] fixedPts = this.commandMediator.scaleClippingWindow();
            //this.refreshDisplay();
        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        //don't do anything here
    }

    private void refreshDisplay() {
        this.glCanvas.display();
    }
}
