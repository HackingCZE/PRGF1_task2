package rasterops.fill;

import objectdata.Point2D;
import rasterdata.Raster;
import java.util.Optional;

import java.util.Stack;
import java.util.function.Predicate;

public class SeedFill4Stack implements SeedFill {

    @Override
    public void fill(Raster img, int startX, int startY, int fillColor, Predicate<Integer> isInArea) {
        Stack<Point2D> stack = new Stack<Point2D>();
        stack.push(new Point2D(startX, startY));

        while (!stack.isEmpty()) {
            Point2D p = stack.pop();
            int c = (int) p.getX();
            int r = (int) p.getY();

            if (c > 0 && r > 0 && c < img.getWidth() && r < img.getHeight()) {
                Optional<Integer> colorOpt = img.getColor(c, r);
                if (colorOpt.isPresent() && isInArea.test(colorOpt.get())) {
                    img.setColor(fillColor, c, r);

                    stack.push(new Point2D(c + 1, r));
                    stack.push(new Point2D(c - 1, r));
                    stack.push(new Point2D(c, r + 1));
                    stack.push(new Point2D(c, r - 1));

                }
            }
        }
    }
}
