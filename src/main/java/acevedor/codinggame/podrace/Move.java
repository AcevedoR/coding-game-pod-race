package acevedor.codinggame.podrace;

public class Move {
    double angle; // Between -18.0 and +18.0
    int thrust; // Between -1 and 200

    public Move(final double angle, final int thrust) {
        this.angle = angle;
        this.thrust = thrust;
    }

    /*void mutate(double angleMutationStep) {
        double ramin = this.angle - 36.0 * angleMutationStep;
        double ramax = this.angle + 36.0 * angleMutationStep;

        if (ramin < -18.0) {
            ramin = -18.0;
        }

        if (ramax > 18.0) {
            ramax = 18.0;
        }

        angle = MathUtils.random(ramin, ramax);


        int pmin = (int) (this.thrust - 200 * angleMutationStep);
        int pmax = (int) (this.thrust + 200 * angleMutationStep);

        if (pmin < 0) {
            pmin = 0;
        }

        if (pmax > 0) {
            pmax = 200;
        }

        this.thrust = (int) MathUtils.random(pmin, pmax);
    }
*/
    public void mutateAngle(int angleMutationStep, int simulationTurn){
            double precision = 1;
            if(simulationTurn < 10000){
                precision = 1;
            }else if (simulationTurn < 30000) {
                precision = 0.6;
            } else {
                precision = 0.1;
            }
        int amplitudeFactor = MathUtils.rrandom((int)(-angleMutationStep* precision), (int)(angleMutationStep * precision));
        this.mutateAngleInternal(angleMutationStep, amplitudeFactor);
    }

    public void mutateThust(int thrustMutationStep, int simulationTurn) {
        double precision = 1;
        if(simulationTurn < 10000){
            precision = 1;
        }else if (simulationTurn < 20000) {
            precision = 0.7;
        } else {
            precision = 0.2;
        }
        int speeRFactor = MathUtils.rrandom((int)(-thrustMutationStep * precision), (int)(thrustMutationStep * precision));
        this.mutateThrustInternal(thrustMutationStep, speeRFactor);
    }

    void mutateAngleInternal(int amplitude, int amplitudeFactor){
        double nextangle = angle + ((18 / amplitude) * amplitudeFactor);

        if (nextangle < -18.0) {
            nextangle += 18;
        }
        if (nextangle > 18.0) {
            nextangle -= 18;
        }
        this.angle = nextangle;
    }

    void mutateThrustInternal(int speedRange, int speedRFactor){
        int nextthrust = thrust + ((int) (((double) GameConstants.MAX_THRUST / speedRange) * speedRFactor));
        if (nextthrust < 0) {
            nextthrust += GameConstants.MAX_THRUST;
        }
        if (nextthrust > GameConstants.MAX_THRUST) {
            // TODO bug
            nextthrust -= GameConstants.MAX_THRUST;
        }

        if(nextthrust > GameConstants.MAX_THRUST - 5 && nextthrust < GameConstants.MAX_THRUST){
            nextthrust = GameConstants.MAX_THRUST;
        }
        this.thrust = nextthrust;
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