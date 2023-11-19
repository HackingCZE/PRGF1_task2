package objectdata;

public class Line {

    private Point2D start;
    private Point2D end;
    private double k;
    private double q;

    public Point2D getStart() {
        return start;
    }

    public Point2D getEnd() {
        return end;
    }

    public Line(Line line){
        this.start = new Point2D(line.start.getX(),line.start.getY());
        this.end = new Point2D(line.end.getX(),line.end.getY());

        k = (end.getX() - start.getX()) / (end.getY() - start.getY());
        q = start.getY() - k * start.getY();

        orientate();
    }
    public Line(Point2D start, Point2D end) {
        this.start = new Point2D(start.getX(),start.getY());
        this.end = new Point2D(end.getX(),end.getY());

        k = (end.getX() - start.getX()) / (end.getY()- start.getY());
        q = start.getX() - k * start.getY();

        orientate();
    }

    /**
     * Changes the orientation of a line segment by swapping the start and end points.
     */
    public void orientate() {
        if (start.getY() > end.getY())
        {
            start.setX(start.getX() - end.getX());
            end.setX(start.getX() + end.getX());
            start.setX(end.getX() - start.getX());

            start.setY( start.getY() - end.getY());
            end.setY( start.getY() + end.getY());
            start.setY( end.getY() - start.getY());
        }
    }



    /**
     * Returns true if horizontal
     *
     * @return boolean indicating weather this line is horizontal
     */
    public boolean isHorizontal() {
        return start.getY() == end.getY();
    }



    /**
     * Return true if this line intercepts with the given horizontal line
     *
     * @param y y coordinate of the line
     * @return boolean indicating the existence of some intercept
     */
    public boolean hasYIntercept(int y) {
        return (y >= start.getY() && y < end.getY());
    }

    /**
     * Returns the x coordinate of the intercept with y; assumes such intercept exists
     *
     * @param y y coordinate of the line
     * @return x coordinate of the intercept
     */
    public double yIntercept(double y) {
        return k * y + q;
    }


    public Point2D intersection(Point2D p0, Point2D p1)
    {
        // used for calculation of both x and y (same formula)
        double denominator = ((p0.getX() - p1.getX()) * (start.getY() - end.getY()) - (p0.getY() - p1.getY()) * (start.getX() - end.getX()));

        double x = ((p0.getX() * p1.getY() - p0.getY() * p1.getX()) * (start.getX() - end.getX()) - (start.getX() * end.getY() - start.getY() * end.getX()) * (p0.getX() - p1.getX())) / denominator;

        double y = ((p0.getX() * p1.getY() - p0.getY() * p1.getX()) * (start.getY() - end.getY()) - (start.getX() * end.getY() - start.getY() * end.getX()) * (p0.getY() - p1.getY())) / denominator;

        return new Point2D(x, y);
    }

    public boolean isInside(Point2D point)
    {
        return ((end.getY() - start.getY()) * point.getX() - (end.getX() - start.getX()) * point.getY() + end.getX() * start.getY() - end.getY() * start.getX() > 0);
    }

}
