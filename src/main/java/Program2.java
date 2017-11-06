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


    public void start() {
    }

    public Program2(GLCanvas canvas) {
        super("CSCI 5631 - Program #2");
        boundingBox = new BoundingBox(100, 200, MAX_BBOX_WIDTH, MAX_BBOX_HEIGHT);
        JOGLEventMediator listener = new JOGLEventMediator(canvas);
        canvas.addGLEventListener(listener);
        canvas.addKeyListener(listener);
        this.setName("CSCI 5631 - Program #2");
        this.getContentPane().add(canvas);
        this.setSize(MAX_WIDTH, MAX_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        //add user input
//        JPanel southRegion = new JPanel(new GridLayout(2, 1));
//        JPanel firstRow = new JPanel(new GridLayout(1, 3));
//        JTextField points = new JTextField();
//        points.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                logger.info("POINTS FIELD ENTERED");
//                logger.info(points.getText());
//                numPoints = Integer.valueOf(points.getText());
//            }
//        });
//        JLabel pointsName = new JLabel("# of points: ");
//        firstRow.add(pointsName);
//        firstRow.add(points);
//        JButton random = new JButton("Choose Random");
//        random.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                logger.info("Random button clicked");
//                randomChosen = true;
//            }
//        });
//        firstRow.add(random);
//        southRegion.add(firstRow);
//
//        JPanel secondRow = new JPanel(new GridLayout(1, 3));
//        JTextField lines = new JTextField();
//        lines.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                logger.info("LINES FIELD ENTERED");
//                logger.info(lines.getText());
//                numLines = Integer.valueOf(lines.getText());
//            }
//        });
//        JLabel linesName = new JLabel("# of lines: ");
//        secondRow.add(linesName);
//        secondRow.add(lines);
//        southRegion.add(secondRow);
//
//        JPanel center = new JPanel(new GridLayout(1,1 ));
//        center.add(canvas);
//        this.add(southRegion, BorderLayout.SOUTH);
//        this.add(center, BorderLayout.CENTER);
        canvas.requestFocusInWindow();
    }
}
