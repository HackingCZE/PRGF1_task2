package rasterops;

import objectdata.Point2D;
import rasterdata.Raster;

import java.awt.*;

public interface Polygoner {
    void drawPolygon(Raster raster, int gap, int colorPolygon, int colorEndPolygon);
    void drawCircle(Raster raster, double h, double k, double r, int gap, int segments, int color);
}
