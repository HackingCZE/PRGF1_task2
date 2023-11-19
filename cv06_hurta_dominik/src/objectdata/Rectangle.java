package objectdata;

import rasterdata.Raster;

public class Rectangle {
    Polygon pol = null;
    Point2D endRect;
    Point2D startRect;

    boolean drawed;
    boolean wasSmart;

    public Rectangle(int x, int y) {
        pol = new Polygon();
        startRect = new Point2D(x,y);
        endRect = new Point2D(0,0);
        for (int i = 0; i < 4; i++) {
            pol.addPoint(startRect);
        }
        drawed = false;
    }

    public Polygon getPol() {
        return pol;
    }


    public void updateShape(Raster img, int gap, int colorShape){
        if (!drawed) return;
        if (wasSmart)
        {
            double dx = Math.abs(endRect.getX()  - startRect.getX());

            if (endRect.getY() > startRect.getY()) {
                endRect.setY( startRect.getY() + (int) dx);
            } else {
                endRect.setY(startRect.getY() - (int) dx);
            }
        }


        pol.setPoint(0, startRect);
        pol.setPoint(1, new Point2D(startRect.getX(), endRect.getY()));
        pol.setPoint(2, endRect);
        pol.setPoint(3, new Point2D(endRect.getX(), startRect.getY()));

        pol.drawPolygon(img, gap, colorShape, colorShape);
    }

    public void drawShape(Raster img, int gap, int x, int y, int colorShape, boolean smart) {
        drawed = true;
        wasSmart = smart;

        endRect = new Point2D(x,y);
        if (smart)
        {
            double dx = Math.abs(endRect.getX()  - startRect.getX());

            if (endRect.getY() > startRect.getY()) {
                endRect.setY( startRect.getY() + (int) dx);
            } else {
                endRect.setY(startRect.getY() - (int) dx);
            }
        }


        pol.setPoint(0, startRect);
        pol.setPoint(1, new Point2D(startRect.getX(), endRect.getY()));
        pol.setPoint(2, endRect);
        pol.setPoint(3, new Point2D(endRect.getX(), startRect.getY()));

        pol.drawPolygon(img, gap, colorShape, colorShape);
    }
}
