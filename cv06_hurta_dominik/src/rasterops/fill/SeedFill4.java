package rasterops.fill;

import rasterdata.Raster;
import rasterops.fill.conditions.TestBackground;

import javax.swing.*;
import java.util.function.Predicate;

public class SeedFill4 implements SeedFill {

    @Override
    public void fill(Raster img, int c, int r, int fillColor, Predicate<Integer> isInArea) {
        img.getColor(c, r).ifPresent(color -> {
            if (isInArea.test(color)) { //checking the background color
                img.setColor(fillColor, c, r);
                fill(img, c + 1, r, fillColor, isInArea);
                fill(img, c - 1, r, fillColor, isInArea);
                fill(img, c, r + 1, fillColor, isInArea);
                fill(img, c, r - 1, fillColor, isInArea);
            }
        });

    }
}
