package acevedor.codinggame.podrace;

import org.junit.Test;

import static java.lang.Math.round;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    @Test
    public void play_ok_first_turn() {
        Player player = new Player();

        Result result = player.play(500, 500, 1000, 500, 0);

        assertThat(result.x)
                .isEqualTo(1000);
        assertThat(result.y)
                .isEqualTo(500);
        assertThat(result.thrust)
                .isEqualTo(30);
    }

    @Test
    public void play_ok_with_checkpoint_initialized() {
        Player player = new Player();
        player.gs.checkpointsList.add(new Point(1000, 500));
        player.gs.areCheckpointsInitialized = true;
        player.startTime = System.currentTimeMillis();

        Result result = player.play(500, 500, 1000, 500, 0);

        assertThat(result.x)
                .isGreaterThanOrEqualTo(500);
        assertThat(result.y)
                .isEqualTo(500);
        assertThat(result.thrust)
                .isGreaterThanOrEqualTo(1);
    }
//    @ParameterizedTest
//    @ValueSource(doubles = {-2222, -500, -19, -18, -10, -5, -1, 0, 1, 3, 8, 17, 18, 19, 300, 1000})
    @Test
    public void generate_limits_ok() {
        int depth = 1;

        Player player = new Player();
        player.pod = Pod.of(500, 500, 0);

        Solutionn[] solutions = player.generatePopulation(depth, 1);

        assertThat(solutions)
                .allSatisfy(solution -> {
                    assertThat(solution.moves1)
                            .size()
                            .isEqualTo(depth);
                    assertThat(solution.moves1)
                            .allSatisfy(move -> assertThat(move).extracting(Move::getAngle).matches(angle -> angle <= 18 && angle >= -18, "is legal angle"))
                            .allSatisfy(move -> assertThat(move).extracting(Move::getThrust).matches(thrust -> thrust <= 100 && thrust >= 0, "is legal thrust"));
                });
    }

    @Test
    public void pod_update_angle_0() {
        Pod pod = Pod.of(400, 400, 0);
        pod.udpate(400, 400, 500, 500, 0);
        assertThat(round(pod.angle))
                .isEqualTo(45);
    }

    @Test
    public void pod_update_angle_10() {
        Pod pod = Pod.of(400, 400, 0);
        pod.udpate(400, 400, 500, 500, 0);
        assertThat(round(pod.angle))
                .isEqualTo(45);
    }
}
