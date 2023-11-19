package rasterops;

import rasterdata.Raster;

public class LinerTrivial implements Liner {
    /**
     * Algoritmus Bresenham
     * <p>
     * Advantages
     * Efficiency, Precision - Minimizes error when drawing lines in raster graphics, carefully selecting pixels that best approximate the ideal line.
     * <p>
     * Disadvantages
     * Limited to Raster Graphics -Primarily designed for raster graphics and may not be ideal for vector graphics.
     */

    @Override
    public void drawLine(Raster raster, double x1, double y1, double x2, double y2, int gap, int color) {
        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);

        if (steep) {
            // swap x1 and y1
            double temp = x1;
            x1 = y1;
            y1 = temp;

            // swap x2 and y2
            temp = x2;
            x2 = y2;
            y2 = temp;
        }

        if (x1 > x2) {
            // swap x1 and x2
            double temp = x1;
            x1 = x2;
            x2 = temp;

            // swap y1 and y2
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        double dx = x2 - x1;
        double dy = Math.abs(y2 - y1);
        double error = dx / 2.0;
        int ystep = (y1 < y2) ? 1 : -1;
        int y = (int) y1;

        int maxX = (int) x2;

        for (int x = (int) x1; x < maxX; x++) {
            if (gap > 2 && x % gap == 0 || gap <= 2) {
                if (steep) {
                    raster.setColor(color, y, x);
                } else {
                    raster.setColor(color, x, y);
                }

            }
            error -= dy;
            if (error < 0) {
                y += ystep;
                error += dx;
            }
        }
    }
}
