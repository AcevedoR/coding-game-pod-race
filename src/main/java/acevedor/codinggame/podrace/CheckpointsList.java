package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

public class CheckpointsList {
    private List<Checkpoint> checkpoints = new ArrayList<>();
    public Checkpoint currentCheckpoint;

    public boolean exists(int newx, int newy){
        return checkpoints.stream().anyMatch(cp -> newx == cp.position.x && newy == cp.position.y);
    }
    public void add(Point p){
        currentCheckpoint = new Checkpoint(checkpoints.size(), p);
        checkpoints.add(currentCheckpoint);
    }

    public int size(){
        return checkpoints.size();
    }
    public boolean isPassingNextCheckpoint(Point p){
        if(p.distance(currentCheckpoint.position) < 595){// TODO should be 600
            passToNextCheckpoint();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "CheckpointsList{" +
                "checkpoints=" + checkpoints +
                ", currentCheckpoint=" + currentCheckpoint +
                '}';
    }

    public Checkpoint passToNextCheckpoint() {
        int i = currentCheckpoint.id;
        Checkpoint res;
        if (i + 1 < checkpoints.size()) {
            res = checkpoints.get(i + 1);
        } else {
            res= checkpoints.get(0);
        }
        currentCheckpoint = res;
        return res;
    }



}
