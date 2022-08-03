package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

public class Solutionn {
    Checkpoint initialCurrentCheckpoint;
    Pod pod;
    int timeout;
    double px;
    double py;
    double angle;
    double vx;
    double vy;
    double checkpointPassedCount;
    List<Move> moves1 = new ArrayList<>();
    public Solutionn(Solutionn s){
        this.setPod(new Pod(s.pod));
        s.moves1.forEach(m -> this.moves1.add(new Move(m.angle, m.thrust)));
    }
    public Solutionn(List<Move> moves1){
        this.moves1 = moves1;
    }

    public Solutionn(Pod pod, List<Move> moves1){
        this.moves1 = moves1;
        this.setPod(pod);
    }
    public void setPod(Pod pod) {
        initialCurrentCheckpoint = pod.currentCheckpoint;
        this.pod = pod;
        timeout = pod.timeout;
        px = pod.position.x;
        py = pod.position.y;
        angle = pod.angle;
        vx = pod.vx;
        vy = pod.vy;
        checkpointPassedCount = pod.checkpointPassedCount;
    }

    public void replaceMovesWithRandom(int amplitude, int thrustRange) {
        for(Move move : moves1){
            move.mutate(amplitude, thrustRange, MathUtils.rrandom(0, amplitude), MathUtils.rrandom(0, thrustRange + 1));
        }
    }

    double score() {
        if(pod == null){
            // solution is not initialized
            return -9999999;
        }
        // Play out the turns
        // Apply the moves to the pods before playing
        double score = simulateMoves(moves1);

//        double result = pod.score();
        double result = score;

        // Reset everyone to their initial states
        reset();
        return result;
    }

    private double simulateMoves(List<Move> moves) {
        double score = 0;
        for (int i = 0; i < moves.size(); i++) {
            pod.playy(moves.get(i));
            if (pod.checkNewCPAndUpdate(pod.position)) {
                pod.passCheckpoint();
            }
            if(i == moves.size() - 1){
                score = pod.score();
            } else {
                score += pod.score() * 0.5 /GameParameters.depth * i;
            }
        }
        return score;
    }

    void reset() {
        pod.currentCheckpoint = initialCurrentCheckpoint;
        pod.timeout = timeout;
        pod.position.x = px;
        pod.position.y = py;
        pod.angle = angle;
        pod.vx = vx;
        pod.vy = vy;
        pod.checkpointPassedCount = checkpointPassedCount;
    }

    void shift() {
        moves1.remove(0);
        moves1.add(Move.generate());
    }

    Pod simulateMove(int i){
        List<Move> l = new ArrayList();
        l.add(moves1.get(i));
        simulateMoves(l);
        return new Pod(pod);
    }

    @Override
    public String toString() {
        return "Solution{" +
                ", initialCurrentCheckpoint=" + initialCurrentCheckpoint +
                ", pod=" + pod +
                ", timeout=" + timeout +
                ", px=" + px +
                ", py=" + py +
                ", angle=" + angle +
                ", vx=" + vx +
                ", vy=" + vy +
                ", checkpointPassedCount=" + checkpointPassedCount +
                ", moves1=" + moves1 +
                '}';
    }
}