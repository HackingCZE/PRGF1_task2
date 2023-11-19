package objectdata;

import rasterdata.Raster;

public class Triangle {
    Polygon pol = null;
    Polygon centerPol = null;
    Point2D center;

    Point2D end;
    public Triangle() {
        pol = new Polygon();
        centerPol = new Polygon();
    }

    public Polygon getPol() {
        return pol;
    }

    public void drawStartLine(Raster img, int gap, Point2D p1, Point2D p2, int colorShape) {
        pol.addPoint(p1);
        pol.addPoint(p2);
        center = getLineCenter(p1,p2);
        centerPol.addPoint(center);
        centerPol.addPoint(center);
        pol.addPoint(center);
        pol.drawPolygon(img,gap,colorShape,colorShape);
        end = center;
    }

    private Point2D getLineCenter(Point2D A, Point2D B) {
        int centerX = (int)(A.getX() + B.getX()) / 2;
        int centerY = (int)(A.getY() + B.getY()) / 2;
        return new Point2D(centerX, centerY);
    }

    public boolean isSetStartLine(){
        return center != null;
    }

    public Point2D getEnd() {
        return end;
    }

    public void updateShape(Raster img,int gap,int colorShape){
        if(!isSetStartLine()) return;
        pol.drawPolygon(img, gap, colorShape, colorShape);
        centerPol.drawPolygon(img, 5, colorShape, colorShape);
    }

    public void drawShape(Raster img, int gap, int x, int y, int colorShape) {
        Point2D p0 = pol.getPoint(0);
        Point2D p1 = pol.getPoint(1);


        // base
        double k0 = (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
        double q0 = y - k0 * x;

        // perpendicular line
        double k1 = -1 / k0;
        double q1 = center.getY() - k1 * center.getX();

        // parallel line
        double q3 = y - k0 * x;

        double tx = (q3 - q1) / (k1 - k0);
        double ty = k0 * tx + q0;

        end = new Point2D((int)tx, (int)ty);

        pol.setPoint(2, end);

        centerPol.setPoint(1,end);

        pol.drawPolygon(img, gap, colorShape, colorShape);
        centerPol.drawPolygon(img, 5, colorShape, colorShape);
    }
}
