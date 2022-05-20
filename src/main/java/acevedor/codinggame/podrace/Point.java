package acevedor.codinggame.podrace;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;

public class Point {
    double x;
    double y;

    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    double distance2(Point p) {
        return (this.x - p.x)*(this.x - p.x) + (this.y - p.y)*(this.y - p.y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    double distance(Point p) {
        return sqrt(this.distance2(p));
    }
}
