package acevedor.codinggame.podrace;

public class GameState {
    public static final int MAX_THRUST = 100;

    public static CheckpointsList checkpointsList = new CheckpointsList();
    public static Double[] precalculatedCos;
    public static Double[] precalculatedSin;

    public static void precalculateAngles() {
        GameState.precalculatedCos = new Double[361];
        GameState.precalculatedSin = new Double[361];
        for (int i = 0; i <= 360; i++) {
            GameState.precalculatedCos[i] = Math.cos(i * Math.PI / 180);
            GameState.precalculatedSin[i] = Math.sin(i * Math.PI / 180);
        }
    }

    public void toto(){

    }
}
