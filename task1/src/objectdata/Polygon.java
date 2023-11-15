package objectdata;

import rasterdata.Raster;
import rasterops.LinerTrivial;
import rasterops.Polygoner;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements Polygoner {

    ArrayList<Point2D> listPoints;


    public Polygon() {
        this.listPoints = new ArrayList<Point2D>();
    }

    public ArrayList<Point2D> getListPoints() {
        return listPoints;
    }

    public void addPoint(Point2D point) {
        this.listPoints.add(point);
        if (this.listPoints.size() > 1) {

        }
    }

    public void setPoint(int index, Point2D point) {
        this.listPoints.set(index, point);
    }

    public Point2D getPoint(int index) {
        return listPoints.get(index);
    }

    public boolean containMoreThanOneLine() {
        return listPoints.size() > 1;
    }

    public List<Line> getLines() {
        ArrayList<Line> listLines = new ArrayList<>();
        for (int i = 0; i < listPoints.size(); i++) {
            Point2D start = listPoints.get(i);
            Point2D end = listPoints.get((i + 1) % listPoints.size()); // Toto zajistí, že pro poslední bod se jako koncový bod nastaví první bod.

            listLines.add(new Line(start, end));

        }
        return listLines;
    }


    /**
     * @param raster   where will polygon drew
     * @param gap      between points
     * @param r        radius of circle
     * @param h        center of circle on x axe
     * @param k        center of circle on y axe
     * @param color    color of circle
     * @param segments how will be circle detailed
     */
    @Override
    public void drawCircle(Raster raster, double h, double k, double r, int gap, int segments, int color) {
        Point2D[] points = new Point2D[segments];

        // generate points on circle
        for (int i = 0; i < segments; i++) {
            double angle = Math.toRadians(i * (360.0 / segments));
            double x = h + r * Math.cos(angle);
            double y = k + r * Math.sin(angle);
            points[i] = new Point2D(x, y);
        }

        drawPolygon(raster, gap, color, color);
    }

    /**
     * create lines between points in array
     *
     * @param raster          where will polygon drew
     * @param gap             between points
     * @param colorPolygon    color of polygon
     * @param colorEndPolygon color of polygon when add new point
     */
    @Override
    public void drawPolygon(Raster raster, int gap, int colorPolygon, int colorEndPolygon) {
        LinerTrivial linerTrivial = new LinerTrivial();
        for (int i = 0; i < listPoints.size(); i++) {
            Point2D currentPoint = listPoints.get(i);
            Point2D nextPoint = listPoints.get((i + 1) % listPoints.size());
            linerTrivial.drawLine(raster, currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y, gap, i != listPoints.size() - 2 && i != listPoints.size() - 1 ? colorPolygon : colorEndPolygon);
        }
    }

}
