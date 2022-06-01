package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

public class Solutionn {
    CheckpointsList checkpointsList;
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

    public Solutionn(CheckpointsList checkpointsList, List<Move> moves1, Pod pod) {
        this.checkpointsList = checkpointsList;
        initialCurrentCheckpoint = checkpointsList.currentCheckpoint;
        this.pod = pod;
        timeout = pod.timeout;
        px = pod.position.x;
        py = pod.position.y;
        angle = pod.angle;
        vx = pod.vx;
        vy = pod.vy;
        checkpointPassedCount = pod.checkpointPassedCount;
        this.moves1 = moves1;
    }

    double score() {
        // Play out the turns
        // Apply the moves to the pods before playing
        for (Move move : moves1) {
            pod.playy(move);
            if (checkpointsList.isPassingNextCheckpoint(pod.position)) {
                pod.passCheckpoint();
            }
        }

        // Compute the score
        double result = pod.score(checkpointsList.currentCheckpoint.position);
        // Reset everyone to their initial states
        reset();
        return result;
    }

    void reset() {
        checkpointsList.currentCheckpoint = initialCurrentCheckpoint;
        pod.timeout = timeout;
        pod.position.x = px;
        pod.position.y = py;
        pod.angle = angle;
        pod.vx = vx;
        pod.vy = vy;
        pod.checkpointPassedCount = checkpointPassedCount;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "checkpointsList=" + checkpointsList +
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