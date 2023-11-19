package rasterdata;

import java.awt.*;
import java.util.Optional;

public interface Raster {


    int getWidth();
    int getHeight();
    boolean setColor(int color, int c, int r);
    Optional<Integer> getColor(int c, int r);
    void clear(int backgroundColor);

}
