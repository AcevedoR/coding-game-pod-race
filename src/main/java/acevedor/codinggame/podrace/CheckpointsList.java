package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

public class CheckpointsList {
    private List<Checkpoint> checkpoints = new ArrayList<>();
    public Checkpoint currentCheckpoint;

    public boolean exists(int newx, int newy){
        return checkpoints.stream().noneMatch(cp -> newx == cp.position.x && newy == cp.position.y);
    }
    public void add(Point p){
        checkpoints.add(new Checkpoint(checkpoints.size(), p));
    }
    public Checkpoint getNextCheckpoint() {
        int i = currentCheckpoint.id;
        if (i + 1 < checkpoints.size()) {
            return checkpoints.get(i + 1);
        } else {
            return checkpoints.get(0);
        }
    }

}
