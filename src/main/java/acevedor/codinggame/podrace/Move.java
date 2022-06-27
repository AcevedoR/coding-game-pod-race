package acevedor.codinggame.podrace;

import java.util.concurrent.ThreadLocalRandom;

public class Move {
    double angle; // Between -18.0 and +18.0
    int thrust; // Between -1 and 200

    public Move(final double angle, final int thrust) {
        this.angle = angle;
        this.thrust = thrust;
    }

    void mutate(double amplitude) {
        double ramin = this.angle - 36.0 * amplitude;
        double ramax = this.angle + 36.0 * amplitude;

        if (ramin < -18.0) {
            ramin = -18.0;
        }

        if (ramax > 18.0) {
            ramax = 18.0;
        }

        angle = random(ramin, ramax);


        int pmin = (int) (this.thrust - 200 * amplitude);
        int pmax = (int) (this.thrust + 200 * amplitude);

        if (pmin < 0) {
            pmin = 0;
        }

        if (pmax > 0) {
            pmax = 200;
        }

        this.thrust = (int) random(pmin, pmax);

    }

    double random(double min, double max) {
        return min + (max - min) * ThreadLocalRandom.current().nextDouble();
    }

    @Override
    public String toString() {
        return "Move{" +
                "angle=" + angle +
                ", thrust=" + thrust +
                '}';
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(final double angle) {
        this.angle = angle;
    }

    public int getThrust() {
        return thrust;
    }

    public void setThrust(final int thrust) {
        this.thrust = thrust;
    }
}