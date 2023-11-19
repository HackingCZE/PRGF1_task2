package objectdata;

public class Point2D
{
    private double x;

    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point2D(double x1, double y1){
        this.x = x1;
        this.y = y1;
    }

    public double length(){
        return x-y;
    }
}
