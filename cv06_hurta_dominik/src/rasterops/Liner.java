package rasterops;

import rasterdata.Raster;
/**
 * Represents an algorithms for rasterization of lines on a 2D raster
 */

public interface Liner {
    /**
     * Draws a line represented by two points onto the provided raster using the provided color
     * @param raster Raster to draw onto
     * @param x1 x coordinate of the first point values ranging from 0 to the width of the raster - 1
     * @param y1 y coordinate of the first point values ranging from 0 to the height of the raster - 1
     * @param x2 x coordinate of the second point value ranging from 0 to the width of the raster - 1
     * @param y2 y coordinate of the second point value ranging from 0 to the height of the raster - 1
     * @param color color of the line
     */
    void drawLine(Raster raster, double x1, double y1, double x2, double y2, int gap, int color);
}
