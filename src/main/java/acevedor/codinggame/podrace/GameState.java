package acevedor.codinggame.podrace;

public class GameState {
    boolean areCheckpointsInitialized = false;
    CheckpointsList checkpointsList = new CheckpointsList();
    Pod pod;

    public void toto(){

    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
