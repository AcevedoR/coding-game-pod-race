package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SolutionTest {
    Pod pod = new Pod(0, 0, 0, 0, 0, null);

    @Test
    public void shift_ok(){
        // given
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(0, 0));
        moves.add(new Move(1, 1));
        moves.add(new Move(2, 2));
        moves.add(new Move(3, 3));
        Solutionn solution = new Solutionn(moves);
        solution.setPod(pod);

        // when
        solution.shift();

        // then
        assertThat(solution.moves1.get(0).angle).isEqualTo(1.0);
        assertThat(solution.moves1.get(0).thrust).isEqualTo(1);
        assertThat(solution.moves1).hasSize(4);
    }

    @Test
    public void random_moves_ok(){
        // given
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(0, 0));
        moves.add(new Move(1, 1));
        moves.add(new Move(2, 2));
        moves.add(new Move(3, 3));
        Solutionn solution = new Solutionn(moves);
        solution.setPod(pod);

        // when
        solution.replaceMovesWithRandom(18, 5);

        // then
        assertThat(solution.moves1)
                .allSatisfy(move -> assertThat(move.angle).isBetween(-18.0, 18.0))
                .allSatisfy(move -> assertThat(move.thrust).isBetween(0, GameConstants.MAX_THRUST));
    }

    @Test
    public void scoring_ok(){
        GameCache.precalculateAngles();
        GameCache.checkpointsList.add(new Checkpoint(0, new Point(1000, 500)));
        Pod pod = new Pod(500, 500, 0, 0, 0, GameCache.checkpointsList.get(0));

        Solutionn worstSolution = new Solutionn(pod, List.of(
                new Move(0, 0)
        ));
        Solutionn bestSolution = new Solutionn(pod, List.of(
                new Move(0, 100)
        ));
        Solutionn goodSolutionMaxAngle = new Solutionn(pod, List.of(
                new Move(-18, 100)
        ));

        assertThat(bestSolution.score())
                .isGreaterThan(worstSolution.score())
                .isGreaterThan(goodSolutionMaxAngle.score());

        assertThat(worstSolution.score())
                .isLessThan(bestSolution.score())
                .isLessThan(goodSolutionMaxAngle.score());
    }

    @Test
    public void replaceLowest_ok(){
        GameParameters.mutation_population = 1;
        List<Solutionn> solutionns = new ArrayList<>();
        Solutionn worstSolution = new Solutionn(pod, List.of());
        worstSolution.score = 1d;
        solutionns.add(worstSolution);

        // when
        Solutionn bestSolution = new Solutionn(pod, List.of());
        bestSolution.score = 22d;
        Solutionn.replaceLowestSolution(solutionns, bestSolution);

        // then
        assertThat(solutionns).containsExactly(bestSolution);
    }
    @Test
    public void doNot_replace_Highest(){
        GameParameters.mutation_population = 1;
        List<Solutionn> solutionns = new ArrayList<>();
        Solutionn bestSolution = new Solutionn(pod, List.of());
        bestSolution.score = 22d;
        solutionns.add(bestSolution);

        // when
        Solutionn worstSolution = new Solutionn(pod, List.of());
        worstSolution.score = 1d;
        Solutionn.replaceLowestSolution(solutionns, worstSolution);

        // then
        assertThat(solutionns).containsExactly(bestSolution);
    }

}
