package acevedor.codinggame.podrace;

import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.round;
import static java.lang.Math.sin;

public class Pod {

    boolean shield = false;
    int timeout = 100;

    Point position;
    double angle;
    double vx = 0d;
    double vy = 0d;
    double checkpointPassedCount = 0;
    public static Pod of(double x, double y, double angle){
        Pod p = new Pod();
        p.position = new Point(x, y);
        p.angle = angle;
        return p;
    }

    void play(Point p, int thrust) {
        this.rotate(p);
        this.boost(thrust);
        this.move(1.0d);
        this.end();
    }

    void playy(Move m) {
        this.applyRotation(m);
        this.boost(m.thrust);
        this.move(1.0d);
        this.end();
    }

    void end() {
        this.position.x = round(this.position.x);
        this.position.y = round(this.position.y);
        this.vx = truncate(this.vx * 0.85);
        this.vy = truncate(this.vy * 0.85);

        // Don't forget that the timeout goes down by 1 each turn. It is reset to 100 when you pass a checkpoint
        this.timeout -= 1;
    }

    double getAngle(Point p) {
        double d = this.position.distance(p);
        double dx = (p.x - this.position.x) / d;
        double dy = (p.y - this.position.y) / d;

        // Simple trigonometry. We multiply by 180.0 / PI to convert radiants to degrees.
        double a = acos(dx) * 180.0 / PI;

        // If the point I want is below me, I have to shift the angle for it to be correct
        if (dy < 0) {
            a = 360.0 - a;
        }

        return a;
    }

    double diffAngle(Point p) {
        double a = this.getAngle(p);

        // To know whether we should turn clockwise or not we look at the two ways and keep the smallest
        // The ternary operators replace the use of a modulo operator which would be slower
        double right = this.angle <= a ? a - this.angle : 360.0 - this.angle + a;
        double left = this.angle >= a ? this.angle - a : this.angle + 360.0 - a;

        if (right < left) {
            return right;
        } else {
            // We return a negative angle if we must rotate to left
            return -left;
        }
    }

    void applyRotation(Move move) {
        double a = angle + move.angle;

        if (a >= 360.0) {
            a = a - 360.0;
        } else if (a < 0.0) {
            a += 360.0;
        }

        // Look for a point corresponding to the angle we want
        // Multiply by 10000.0 to limit rounding errors
        a = a * PI / 180.0;
        double px = this.position.x + cos(a) * 10000.0;
        double py = this.position.y + sin(a) * 10000.0;

        rotate(new Point(px, py));
    }

    void rotate(Point p) {
        double a = this.diffAngle(p);

        // Can't turn by more than 18° in one turn
        if (a > 18.0) {
            a = 18.0;
        } else if (a < -18.0) {
            a = -18.0;
        }

        this.angle += a;

        // The % operator is slow. If we can avoid it, it's better.
        if (this.angle >= 360.0) {
            this.angle = this.angle - 360.0;
        } else if (this.angle < 0.0) {
            this.angle += 360.0;
        }
    }
    void boost(int thrust) {
        // Don't forget that a pod which has activated its shield cannot accelerate for 3 turns
        if (this.shield) {
            return;
        }

        // Conversion of the angle to radiants
        double ra = this.angle * PI / 180.0;

        // Trigonometry
        this.vx += cos(ra) * thrust;
        this.vy += sin(ra) * thrust;
    }
    void move(double t) {
        this.position.x += this.vx * t;
        this.position.y += this.vy * t;
    }
    void passCheckpoint(){
        checkpointPassedCount++;
//        System.err.println("passed checkpoint inc:" + checkpointPassedCount);
    }
    double score(Point currentChekpoint) {
        return checkpointPassedCount * 50000 - this.position.distance(currentChekpoint);
//        return - this.position.distance(currentChekpoint);
    }

    Point toResult(Move move) {
        double a = angle + move.angle;

        if (a >= 360.0) {
            a = a - 360.0;
        } else if (a < 0.0) {
            a += 360.0;
        }

        // Look for a point corresponding to the angle we want
        // Multiply by 10000.0 to limit rounding errors
        a = a * PI / 180.0;
        double px = this.position.x + cos(a) * 10000.0;
        double py = this.position.y + sin(a) * 10000.0;

        return new Point(px, py);
    }

    public static double truncate(double x) {
        if (x == 0) {
            return x;
        }else if (x > 0) {
            return floor(x);
        } else {
            return ceil(x);
        }
    }

    public void udpate(final int x, final int y, int nextCheckPointX, int nextCheckpointY, int nextCheckpointAngle) {
        this.position.x = x;
        this.position.y = y;
        this.angle = getAngle(new Point(nextCheckPointX, nextCheckpointY));
    }

    @Override
    public String toString() {
        return "Pod{" +
                "shield=" + shield +
                ", timeout=" + timeout +
                ", position=" + position +
                ", angle=" + angle +
                ", vx=" + vx +
                ", vy=" + vy +
                ", checkpointPassedCount=" + checkpointPassedCount +
                '}';
    }
}
