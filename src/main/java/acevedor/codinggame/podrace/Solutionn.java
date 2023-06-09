package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

public class Solutionn {
    Double score = -1d;//optimization

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
    public static Solutionn solutionCopyWithPodReference(Solutionn s){
        Solutionn news = new Solutionn(s.pod, s.moves1);
        news.moves1 = new ArrayList<>();
        s.moves1.forEach(m -> news.moves1.add(new Move(m.angle, m.thrust)));
        return news;
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
//            move.mutateAngle(amplitude);
//            move.mutateThust(thrustRange);// TODO
            move.angle= (MathUtils.random(-18.0, 18.0));
            move.thrust = MathUtils.rrandom(0, GameConstants.MAX_THRUST);
        }
    }

    double score() {
        if(pod == null){
            // solution is not initialized
            return -9999999;
        }
        if (score != -1){
            return score;
        }
        double result = simulateMoves(moves1);

        // Reset everyone to their initial states
        reset();
        score = result;
        return score;
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
                score += pod.score() * 0.1 / (GameParameters.depth * i);
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
        score = -1d;
    }

    void shift() {
        moves1.remove(0);
        moves1.add(Move.generate());
    }

    void mutate(int simulationTurn) {
        Move move = moves1.get(MathUtils.rrandom(0, moves1.size() - 1));
        move.mutate(simulationTurn);
    }

    Pod simulateMove(int i){
        List<Move> l = new ArrayList();
        l.add(moves1.get(i));
        simulateMoves(l);
        return new Pod(pod);
    }

    public static double replaceLowestSolution(List<Solutionn> solutions, Solutionn newSolution){
        if(solutions.size() != GameParameters.mutation_population){
            throw new IllegalArgumentException("there should be always the same number of population, actual: " + solutions.size());
        }
        if(newSolution.score() > solutions.get(solutions.size() - 1).score()) {
            solutions.remove(solutions.size() - 1);
            solutions.add(newSolution);
            sortSolutions(solutions);
        }
        return solutions.get(solutions.size()-1).score();
    }

    public static Solutionn getBestSolution(List<Solutionn> solutions){
        return sortSolutions(solutions).get(0);
    }

    public static List<Solutionn> sortSolutions(final List<Solutionn> solutions) {
        solutions.sort((a, b) -> (int) (b.score() - a.score()));
        return solutions;
    }

    @Override
    public String toString() {
        return "Solution{" +
                ", score=" + score +
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