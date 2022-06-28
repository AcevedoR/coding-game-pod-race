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
        moves.add(new Move(4, 5));
        Solutionn solution = new Solutionn(moves, pod);

        // when
        solution.shift();

        // then
        assertThat(solution.moves1.get(0).angle).isEqualTo(1.0);
        assertThat(solution.moves1.get(0).thrust).isEqualTo(1);
    }
}
