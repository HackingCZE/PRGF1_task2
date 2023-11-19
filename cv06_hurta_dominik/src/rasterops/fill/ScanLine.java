package rasterops.fill;

import objectdata.Point2D;
import objectdata.Polygon;
import objectdata.Line;
import rasterdata.Raster;
import rasterops.Liner;
import rasterops.Polygoner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine {

    public boolean pattern(Point2D point) {
        double patternX = point.getX() % 10;
        double patternY = point.getY() % 10;

        return (patternX == patternY);
    }
    public void fill(Raster img, ArrayList<Point2D> points, int fillColor, boolean isPattern){
        Polygon pol = new Polygon();
        for (int i =0; i< points.size();i++){
            pol.addPoint(points.get(i));
        }

        fill(img, pol,fillColor,isPattern);
    }
    public void fill(Raster img, Polygon polygon, int fillColor, boolean isPattern) {

        if (polygon.getListPoints().size() < 2) return;
        // 1. Create a list of lines
        // 2. Remove horizontal lines from the list
        // 3. Orient the lines
        // 4. Calculate yMin and yMax
        // 5. for y in [yMin, yMax]:
        //      - create a list of intercepts
        //      - for line in lines:
        //          - compute the intercept if it exists and add it to the list of intercepts
        //      - sort the list of intercepts in ascending order
        //      - fill the area between odd and even intercepts using the `fillColor`

        List<Line> lines = getNonHorizontalLines(polygon);


        double ymin = Integer.MAX_VALUE, ymax = Integer.MIN_VALUE;

        for (Line line : lines) {
            ymin = Math.min(ymin, Math.min((int) line.getStart().getY(), (int) line.getEnd().getY()));
            ymax = Math.max(ymax, Math.max((int) line.getStart().getY(), (int) line.getEnd().getY()));
        }


        for (int y = (int) ymin; y < ymax; y++) {

            List<Double> intersections = new ArrayList<>();

            for (Line line : lines) {
                if (line.hasYIntercept(y)) {
                    intersections.add(line.yIntercept(y));
                }
            }

            Collections.sort(intersections);

            for (int i = intersections.size() - 1; i > 0; i -= 2) {
                for (int x = (intersections.get(i - 1)).intValue(); x <= (intersections.get(i)).intValue(); x++) {
                    if (isPattern)
                    {
                        if (pattern(new Point2D(x,y))){
                            img.setColor(fillColor, x, y);
                        }
                    }
                    else {
                        img.setColor(fillColor, x, y);
                    }
                }
            }

        }

    }

    private List<Line> getNonHorizontalLines(Polygon polygon) {
        List<Line> result = new ArrayList<>();
        for (Line line : polygon.getLines()) {
            if (!line.isHorizontal()) {
                result.add(line);
            }
        }
        return result;
    }

}
