package objectop;

import objectdata.Line;
import objectdata.Point2D;
import objectdata.Polygon;

import java.util.ArrayList;

public class PolygonCutter {

    public ArrayList<Point2D> clippedPoints(ArrayList<Point2D> points, ArrayList<Point2D> clipPoints)
    {
        if (clipPoints.size() < 2)
        {
            return points;
        }

        ArrayList<Point2D> newPoints = points;

        Point2D latestPoint = clipPoints.get(clipPoints.size() - 1);

        for (Point2D clipPoint : clipPoints)
        {
            newPoints = clippedEdges(points, new Line(latestPoint, clipPoint));

            points = newPoints;
            latestPoint = clipPoint;
        }

        return newPoints;
    }

    private ArrayList<Point2D> clippedEdges(ArrayList<Point2D> points, Line line)
    {
        if (points.size() < 2)
        {
            return points;
        }

        ArrayList<Point2D> newPoints = new ArrayList<>();

        Point2D latestPoint = points.get(points.size() - 1);

        for (Point2D point : points)
        {
            if (line.isInside(point))
            {
                if (!line.isInside(latestPoint))
                {
                    newPoints.add(line.intersection(point, latestPoint));
                }

                newPoints.add(point);
            }
            else if (line.isInside(latestPoint))
            {
                newPoints.add(line.intersection(point, latestPoint));
            }

            latestPoint = point;
        }

        return newPoints;
    }
}
