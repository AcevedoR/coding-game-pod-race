package acevedor.codinggame.podrace;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveTest {

    @Test
    public void generateRandom_ok_within_range() {
        Move move = new Move(-1, -1);
        int amplitude = 18;
        int speerR = 7;
        for (int s = 0; s <= speerR; s++) {
            for (int a = 0; a <= amplitude; a++) {
                move.mutate(amplitude, speerR, a, s);
                assertThat(move.angle).isBetween(-18.0, 18.0);
                assertThat(move.thrust).isBetween(0, GameConstants.MAX_THRUST);
            }
        }
    }

    @Test
    public void generateRandom_ok_always_reach_max_and_min() {
        Move move = new Move(-1, -1);
        int amplitude = 18;
        int speerR = 7;

        move.mutate(amplitude, speerR, 0, 0);
        assertThat(move.angle).isEqualTo(-18.0);
        assertThat(move.thrust).isEqualTo(0);

        move.mutate(amplitude, speerR, amplitude, speerR);
        assertThat(move.angle).isEqualTo(18.0);
        assertThat(move.thrust).isEqualTo(GameConstants.MAX_THRUST);
    }
}
