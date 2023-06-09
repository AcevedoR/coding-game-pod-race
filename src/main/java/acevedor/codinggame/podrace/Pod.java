package acevedor.codinggame.podrace;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;

public class Pod {

    boolean shield = false;
    int timeout = 100;

    Point position = new Point(0, 0);
    double angle;
    double vx = 0d;
    double vy = 0d;
    double checkpointPassedCount = 0;
    Checkpoint currentCheckpoint;

    public Pod(Pod pod){
        this.position = new Point(pod.position);
        this.angle = pod.angle;
        this.vx = pod.vx;
        this.vy = pod.vy;
        this.checkpointPassedCount = pod.checkpointPassedCount;
        this.currentCheckpoint = pod.currentCheckpoint;
    }
    public Pod(int x, int y, int vx, int vy, int angle, Checkpoint nextCheckpoint){
        this.position.x = x;
        this.position.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.currentCheckpoint = nextCheckpoint;
    }

    void playy(Move m) {
        angle += m.angle;
        this.boost(m.thrust);
        this.move(1.0d);
        this.end();
    }

    void end() {
        this.position.x = MathUtils.truncate(this.position.x);
        this.position.y = MathUtils.truncate(this.position.y);
        this.vx = MathUtils.truncate(this.vx * 0.85);
        this.vy = MathUtils.truncate(this.vy * 0.85);

        // Don't forget that the timeout goes down by 1 each turn. It is reset to 100 when you pass a checkpoint
        this.timeout -= 1;
    }

    double getAngle(Point p) {
        double d = this.position.distance(p);
        double dx = (p.x - this.position.x) / d;
        double dy = (p.y - this.position.y) / d;

        // Simple trigonometry. We multiply by 180.0 / PI to convert radiants to degrees.
        //double a = acos(dx) * 180.0 / PI;// fix this is too slow
        double a = acosApprox(dx) * 180.0 / PI;
        // If the point I want is below me, I have to shift the angle for it to be correct
        if (dy < 0) {
            a = 360.0 - a;
        }

        return a;
    }

    double acosApprox(double x) {
        return (-0.69813170079773212 * x * x - 0.87266462599716477) * x + 1.5707963267948966;
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

  /*  void applyRotation(Move move) {
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
    }*/

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

//        // Conversion of the angle to radiants
//        double ra = this.angle * PI / 180.0;
//
//        // Trigonometry
//        this.vx += cos(ra) * thrust;
//        this.vy += sin(ra) * thrust;
        int aaangle = (int) angle;
        if (angle >= 360.0) {
            aaangle = (int) angle - 360;
        } else if (angle < 0.0) {
            aaangle += 360.0;
        }
        this.vx += GameCache.precalculatedCos[aaangle] * thrust;
        this.vy += GameCache.precalculatedSin[aaangle] * thrust;
    }
    void move(double t) {
        this.position.x += this.vx * t;
        this.position.y += this.vy * t;
    }
    void passCheckpoint(){
        checkpointPassedCount++;
    }
    double score() {
        return checkpointPassedCount * 500000
                - this.position.distance(currentCheckpoint.position)
                - Math.abs(diffAngle(currentCheckpoint.position))
                + MathUtils.speed(vx, vy) * 0.3
                ;
    }

    Point toResult() {
        if (angle >= 360.0) {
            angle = angle - 360.0;
        } else if (angle < 0.0) {
            angle += 360.0;
        }

        // Look for a point corresponding to the angle we want
        // Multiply by 10000.0 to limit rounding errors
        double angledddd = angle * PI / 180.0;
        double px = this.position.x + cos(angledddd) * 10000.0;
        double py = this.position.y + sin(angledddd) * 10000.0;

        return new Point(px, py);
    }

    public void udpate(int x, int y, int  vx, int vy, int angle, Checkpoint nextCheckpoint) {
//        System.err.println("simu xy: "+position+" real :"+x+ ","+y+" simuspeed:("+this.vx+","+this.vy+") real:("+vx+","+vy+")");   to debug rounding errors
//        System.err.println("simu angle: "+this.angle+" real :"+angle);
        this.vx = vx;
        this.vy = vy;
        this.position.x = x;
        this.position.y = y;

        this.currentCheckpoint = nextCheckpoint;
        this.angle = angle;
    }

    public boolean checkNewCPAndUpdate(Point p){
        if(p.distance(currentCheckpoint.position) < 599){// TODO should be 600
            currentCheckpoint = GameCache.checkpointsList.next(currentCheckpoint);
            return true;
        }
        return false;
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
