package rasterops.fill;

import objectdata.Polygon;
import objectdata.Line;
import rasterdata.Raster;
import rasterops.Liner;
import rasterops.Polygoner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine {
    public void fill(Polygon polygon, Raster raster, int fillColor) {

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

        int ymin = Integer.MAX_VALUE, ymax = Integer.MIN_VALUE;

        for (Line line : lines) {
            line.calculate();

            ymin = Math.min(ymin, Math.min((int) line.start.y, (int) line.end.y));
            ymax = Math.max(ymax, Math.max((int) line.start.y, (int) line.end.y));
        }


        for (int y = ymin; y <= ymax; y++) {
            List<Double> intercepts = new ArrayList<>();
            for (Line line : lines) {
                if (line.hasYIntercept(y)) {
                    intercepts.add(line.yIntercept(y));
                }
            }
            Collections.sort(intercepts);
            for (int i = 0; i < intercepts.size() - 1; i += 2) {
                int startX = (int) Math.round(intercepts.get(i));
                int endX = (int) Math.round(intercepts.get(i + 1));
                for (int x = startX; x <= endX; x++) {
                    raster.setColor(fillColor, x, y);
                }
            }
        }

    }

    private List<Line> getNonHorizontalLines(Polygon polygon) {
        List<Line> result = new ArrayList<>();
        for (Line line : polygon.getLines()) {
            if (!line.isHorizontal()) {
                result.add(line.swap());
            }
        }
        return result;
    }

}
