package acevedor.codinggame.podrace;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Math.round;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {
    Player player;

    @Before
    public void init(){
        player = new Player();
        player.debug = true;
        player.solutionNumber = 30;
        player.init();
    }

    @Test
    public void play_ok_with_checkpoint_initialized() {
        GameState.checkpointsList.add(new Checkpoint(0, new Point(1000, 500)));
        player.startTime = System.currentTimeMillis();
        player.isTesting = true;
        player.depth = 1;
        player.generatedMoves = List.of(
                new Move(0, 0),
                new Move(0, 80),
                new Move(18, 80),
                new Move(-18, 80)
        );

        player.applyInput(
                500, 500, 0, 0, 0, 0,
                200, 500, 0, 0, 0, 0
                );
        player.play();

        assertThat(player.pod1.position.x)
                .isGreaterThan(500);
        assertThat(player.pod1.position.y)
                .isEqualTo(500);
        assertThat(player.pod1.vx)
                .isEqualTo(80*0.85);
        assertThat(player.pod1.vy)
                .isEqualTo(0);

        assertThat(player.pod1.position.x)
                .isGreaterThan(200);
        assertThat(player.pod1.position.y)
                .isEqualTo(500);
        assertThat(player.pod1.vx)
                .isEqualTo(80*0.85);
        assertThat(player.pod1.vy)
                .isEqualTo(0);
    }

    @Test
    public void moves_generated() {
        GameState.checkpointsList.add(new Checkpoint(0, new Point(1000, 500)));
        player.startTime = System.currentTimeMillis();
        player.isTesting = true;
        player.depth = 1;
        player.generatedMoves = List.of(
                new Move(0, 0),
                new Move(0, 80),
                new Move(18, 80),
                new Move(-18, 80)
        );

        player.applyInput(
                500, 500, 0, 0, 0, 0,
                200, 500, 0, 0, 0, 0
        );
        player.play();

        player.init();
        assertThat(player.generatedMoves)
                .hasSize(player.amplitube * player.speedR + player.amplitube + player.speedR +1);
    }
  /*  @Test
    public void play_ok_first_turn() {

        Result result = player.play(500, 500, 1000, 500, 0);

        assertThat(result.x)
                .isEqualTo(1000);
        assertThat(result.y)
                .isEqualTo(500);
        assertThat(result.thrust)
                .isGreaterThan(0);
    }


//    @ParameterizedTest
//    @ValueSource(doubles = {-2222, -500, -19, -18, -10, -5, -1, 0, 1, 3, 8, 17, 18, 19, 300, 1000})
    @Test
    public void generate_limits_ok() {
        int depth = 1;

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
        pod.udpate(400, 400, 500, 500, 0, 0);
        assertThat(round(pod.angle))
                .isEqualTo(45);
    }

    @Test
    public void pod_update_angle_10() {
        Pod pod = Pod.of(400, 400, 0);
        pod.udpate(400, 400, 500, 500, 0, 0);
        assertThat(round(pod.angle))
                .isEqualTo(45);
    }

    @Test
    public void update_should_correctly_calculate_speed() {
        Pod pod = Pod.of(11710, 5638, 0);
        pod.udpate(11519, 5807, 6287, 6644, 11710, 5638);
        // -162  143
        assertThat(MathUtils.truncate(pod.vx))
                .isEqualTo(-162);
        assertThat(MathUtils.truncate(pod.vy))
                .isEqualTo(143);
    }*/
    //4162,6913  450 77
//    4692,7005
}
