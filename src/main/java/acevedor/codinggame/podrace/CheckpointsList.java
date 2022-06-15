package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

public class CheckpointsList {
    private List<Checkpoint> checkpoints = new ArrayList<>();
    public boolean exists(int newx, int newy){
        return checkpoints.stream().anyMatch(cp -> newx == cp.position.x && newy == cp.position.y);
    }
    public void add(Checkpoint c){
        checkpoints.add(c);
    }
    public Checkpoint get(int i){
        return checkpoints.get(i);
    }

    public int size(){
        return checkpoints.size();
    }

    @Override
    public String toString() {
        return "CheckpointsList{" +
                "checkpoints=" + checkpoints +
                '}';
    }

    public Checkpoint next(final Checkpoint currentCheckpoint) {
        if(currentCheckpoint.id + 1 >= checkpoints.size()){
            return checkpoints.get(0);
        }else {
            return checkpoints.get(currentCheckpoint.id + 1);
        }
    }
}
