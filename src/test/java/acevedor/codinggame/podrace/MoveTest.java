package acevedor.codinggame.podrace;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveTest {

    @Test
    public void generateRandom_angle_within_range() {
        Move move = new Move(0, 0);
        int amplitude = GameParameters.amplitube;
            for (int a = 0; a <= amplitude; a++) {
                move.mutateAngleInternal(amplitude, a);
                assertThat(move.angle).isBetween(-18.0, 18.0);
            }
    }

    @Test
    public void generateRandom_angle_always_reach_max_and_min() {
        Move move = new Move(0, 0);
        int amplitude = GameParameters.amplitube;

        move.mutateAngleInternal(amplitude,0);
        assertThat(move.angle).isEqualTo(0);

        move = new Move(0, 0);
        move.mutateAngleInternal(amplitude, amplitude);
        assertThat(move.angle).isEqualTo(18.0);
    }

    @Test
    public void generateRandom_angle_should_handle_negative() {
        Move move = new Move(0, 0);
        int amplitude = 18;

        move.mutateAngleInternal(amplitude,-1);
        assertThat(move.angle).isEqualTo(-1);
    }

    @Test
    public void mutation_angle_should_average() {
        Move move = new Move(0, 0);

        int sum = 0;
        int mutations = 10000;
        for (int i = 0; i < mutations; i++) {
            move.mutateAngle(GameParameters.amplitube, 1);
            sum += move.angle + 18;
        }
        assertThat(sum / mutations).isGreaterThanOrEqualTo(17);
        assertThat(sum / mutations).isLessThanOrEqualTo(19);
    }

    @Test
    public void generateRandom_ok_within_range() {
        Move move = new Move(-1, -1);
        int speerR = GameParameters.speedR;
        for (int s = 0; s <= speerR; s++) {
                move.mutateThrustInternal(speerR, s);
                assertThat(move.thrust).isBetween(0, GameConstants.MAX_THRUST);
            }
    }

    @Test
    public void generateRandom_speed_always_reach_max_and_min() {
        Move move = new Move(0, 0);
        int speedR = GameParameters.speedR;

        move.mutateThrustInternal(speedR,0);
        assertThat(move.thrust).isEqualTo(0);

        move = new Move(0, 0);
        move.mutateThrustInternal(speedR, speedR);
        assertThat(move.thrust).isEqualTo(GameConstants.MAX_THRUST);
    }

    @Test
    public void mutation_thrust_should_average() {
        Move move = new Move(0, 50);

        int sum = 0;
        int mutations = 10000;
        for (int i = 0; i < mutations; i++) {
            move.mutateThust(GameParameters.speedR, 1);
            sum += move.thrust;
        }
        assertThat(sum / mutations).isGreaterThanOrEqualTo(49);
        assertThat(sum / mutations).isLessThanOrEqualTo(51);
    }

}
