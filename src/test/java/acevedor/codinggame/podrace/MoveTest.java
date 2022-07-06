package acevedor.codinggame.podrace;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveTest {

    @Test
    public void generateRandom_ok(){
        Move move = new Move(-1, -1);
        int amplitude = 18;
        int speerR = 7;
        for(int s = 0; s < speerR; s++) {
            for (int a = 0; a < amplitude; a++) {
                move.mutate(amplitude, speerR, a, s);
                assertThat(move.angle).isBetween(-18.0, 18.0);
                assertThat(move.thrust).isBetween(0, GameConstants.MAX_THRUST);
            }
        }
    }
}
