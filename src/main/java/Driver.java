import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class Driver {
    public static void main(String[] args) {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        GLCanvas glCanvas = new GLCanvas(glCapabilities);
        Program2 program2 = new Program2(glCanvas);
        program2.start();
    }
}
