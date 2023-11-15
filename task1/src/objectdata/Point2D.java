package objectdata;

public class Point2D
{
    final public double x;

    final public double y;

    public Point2D(double x1, double y1){
        this.x = x1;
        this.y = y1;
    }

    public double length(){
        return x-y;
    }
}
