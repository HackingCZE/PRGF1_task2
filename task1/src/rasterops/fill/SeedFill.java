package rasterops.fill;

import rasterdata.Raster;
import rasterops.fill.conditions.TestBackground;

import javax.swing.*;
import java.util.function.Predicate;

public interface SeedFill {
    void fill(Raster img, int c, int r, int fillColor, Predicate<Integer> isInArea);
}
