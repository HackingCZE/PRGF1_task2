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
        this.k = (this.end.y - this.start.y) / (this.end.x - this.start.x);
        this.q = this.start.y - this.k * this.start.x;
    }

    /**
     * Truncates the end of the line segment by 1 pixel.
     */
    public void truncate() {
        if (this.end.x != this.start.x) {
            // Zkrátíme x souřadnici koncového bodu, pokud není vertikální
            double deltaY = k; // Změna v y vzhledem ke změně v x
            this.end = new Point2D(this.end.x - 1, this.end.y - deltaY);
        } else {
            // Pro vertikální přímku zkrátíme jen y souřadnici
            this.end = new Point2D(this.end.x, this.end.y - 1);
        }
    }

    /**
     * Returns true if horizontal
     *
     * @return boolean indicating weather this line is horizontal
     */
    public boolean isHorizontal() {
        return start.x == end.y;
    }

    /**
     * Returns a new line which points downward
     *
     * @return a new oriented line
     */
    public Line oriented() {
        int deltaY = 5;
        Point2D newStart = new Point2D(this.start.x, this.start.y + deltaY);
        Point2D newEnd = new Point2D(this.end.x, this.end.y + deltaY);

        return new Line(newStart, newEnd);
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
        if (start.x == end.x) {
            return start.x; // return x-axe vertical line
        }

        double m = (end.y - start.y) / (end.x - start.x);

        double b = start.y - m * start.x;

        return (y - b) / m;
    }

    public Point2D intercript(Line other){
        // Výpočet směrnic obou přímek
        double m1 = (this.end.y - this.start.y) / (this.end.x - this.start.x);
        double m2 = (other.end.y - other.start.y) / (other.end.x - other.start.x);

        // Kontrola, zda jsou přímky rovnoběžné (včetně shodných)
        if (m1 == m2) {
            return null; // Neexistuje jedinečný průsečík
        }

        // Výpočet y-odseček obou přímek
        double b1 = this.start.y - m1 * this.start.x;
        double b2 = other.start.y - m2 * other.start.x;

        // Výpočet souřadnic průsečíku
        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;

        return new Point2D(x, y);
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
