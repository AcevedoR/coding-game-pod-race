package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

public class GameCache {

    public static CheckpointsList checkpointsList = new CheckpointsList();
    public static Double[] precalculatedCos;
    public static Double[] precalculatedSin;
    // opti
    static List<Solutionn> emptySolutions = new ArrayList<>();
    static List<Solutionn> emptySolutions2 = new ArrayList<>();

    public static void precalculateAngles() {
        GameCache.precalculatedCos = new Double[361];
        GameCache.precalculatedSin = new Double[361];
        for (int i = 0; i <= 360; i++) {
            GameCache.precalculatedCos[i] = Math.cos(i * Math.PI / 180);
            GameCache.precalculatedSin[i] = Math.sin(i * Math.PI / 180);
        }
    }

    public void toto(){

    }
}
