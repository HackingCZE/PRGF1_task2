package objectdata;

public class Line {

    public Point2D start;
    public Point2D end;
    private double k;
    private double q;

    public Line(Line line){
        this.start = line.start;
        this.end = line.end;
    }
    public Line(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Changes the orientation of a line segment by swapping the start and end points.
     */
    public Line swap() {
        Point2D temp = this.start;
        this.start = this.end;
        this.end = temp;

        return this;
    }

    /**
     * Calculates and sets the parameters k (directive) and q (y-intercepts) of the line.
     */
    public void calculate() {
        final double EPSILON = 1e-10; // malá hodnota pro porovnání s nulou
        if (Math.abs(end.x - start.x) < EPSILON) { // pokud je x-rozdíl velmi malý, považujeme linii za vertikální
            this.k = Double.POSITIVE_INFINITY; // nebo jiná reprezentace nekonečné směrnice
            this.q = start.x; // vertikální linie nemá y-odseček, takže použijeme x-souřadnici
        } else {
            this.k = (end.y - start.y) / (end.x - start.x);
            this.q = start.y - this.k * start.x;
        }
    }

    /**
     * Truncates the end of the line segment by 1 pixel.
     */
    public void truncate() {
        if (this.end.x != this.start.x) {
            double deltaY = k;
            this.end = new Point2D(this.end.x - 1, this.end.y - deltaY);
        } else {
            this.end = new Point2D(this.end.x, this.end.y - 1);
        }
    }

    /**
     * Returns true if horizontal
     *
     * @return boolean indicating weather this line is horizontal
     */
    public boolean isHorizontal() {
        return start.y == end.y;
    }



    /**
     * Return true if this line intercepts with the given horizontal line
     *
     * @param y y coordinate of the line
     * @return boolean indicating the existence of some intercept
     */
    public boolean hasYIntercept(double y) {
        return (y >= Math.min(start.y, end.y)) && (y <= Math.max(start.y, end.y));
    }

    /**
     * Returns the x coordinate of the intercept with y; assumes such intercept exists
     *
     * @param y y coordinate of the line
     * @return x coordinate of the intercept
     */
    public double yIntercept(double y) {
        // if it is vertical
        if (Double.isInfinite(this.k)) {
            return Math.round(this.q); // Předpokládáme, že q byla nastavena na x-souřadnici pro vertikální linie
        }

        double m = this.k; // již vypočítáno v calculate()
        double b = this.q; // již vypočítáno v calculate()

        double xIntercept = (y - b) / m;
        return Math.round(xIntercept); // Zaokrouhlení na nejbližší celé číslo
    }



    public boolean isInside(Point2D p) {
        final Point2D t = new Point2D(end.x - start.x, end.y - start.y);
        final Point2D n = new Point2D(-t.y, t.x);
        final Point2D v = new Point2D(p.x - start.x, p.y - start.y);
        final Point2D nNorm = new Point2D(n.x / n.length(), n.y / n.length());
        final Point2D vNorm = new Point2D(v.x / v.length(), v.y / v.length());
        final double cosAlpha = nNorm.x * vNorm.x + nNorm.y * vNorm.y;
        return cosAlpha > 0;
    }

}
