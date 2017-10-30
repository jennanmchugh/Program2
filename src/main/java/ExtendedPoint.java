public class ExtendedPoint extends javafx.geometry.Point2D {
    private float[] pointColor;

    /**
     * Creates a new instance of {@code Point2D}.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    public ExtendedPoint(double x, double y) {
        super(x, y);
    }

    public void setPointColor(float[] pointColor) {
        this.pointColor = pointColor;
    }

    public float[] getPointColor() {
        return this.pointColor;
    }
}
