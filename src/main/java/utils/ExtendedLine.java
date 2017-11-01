package utils;

import com.sun.javafx.geom.Line2D;

public class ExtendedLine extends Line2D {
    private float[] lineColor;

    /**
     * Constructs and initializes a Line from the specified coordinates.
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     */
    public ExtendedLine(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
    }

    public void setLineColor(float[] lineColor) {
        this.lineColor = lineColor;
    }

    public float[] getLineColor() {
        return this.lineColor;
    }
}
