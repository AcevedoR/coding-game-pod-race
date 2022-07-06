package acevedor.codinggame.podrace;

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

        angle = MathUtils.random(ramin, ramax);


        int pmin = (int) (this.thrust - 200 * amplitude);
        int pmax = (int) (this.thrust + 200 * amplitude);

        if (pmin < 0) {
            pmin = 0;
        }

        if (pmax > 0) {
            pmax = 200;
        }

        this.thrust = (int) MathUtils.random(pmin, pmax);
    }

    public void mutate(int amplitude, int speeR, int amplitudeFactor, int speeRFactor){
        angle = (int) (-18 + (39.0 / amplitude) * amplitudeFactor);
        thrust = (int) (((double) GameConstants.MAX_THRUST / speeR) * speeRFactor);
    }

    public static Move generate(){
        double angle = MathUtils.random(-18.0, 18.0);
        int thrust = MathUtils.rrandom(0, GameConstants.MAX_THRUST);
        return new Move(angle, thrust);
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