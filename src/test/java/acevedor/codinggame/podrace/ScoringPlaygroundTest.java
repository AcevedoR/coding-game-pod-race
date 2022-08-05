package acevedor.codinggame.podrace;

import com.almasb.fxgl.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class ScoringPlaygroundTest {

    public static void main(String[] args) {

        ScoringPlaygroundTest scoringPlaygroundTest = new ScoringPlaygroundTest();
        List<TestInput> testInputs = scoringPlaygroundTest.getInputs();
        long startTime = System.currentTimeMillis();
        TestResult bestResult = null;
        TestInput bestInput = null;
        List<TestData> testDatas = new ArrayList<>();
        
        
        for (int i = 0; i < testInputs.size(); i++) {
            TestInput currentInput = testInputs.get(i);
            TestResult testResult = scoringPlaygroundTest.doXRepetitions(3, currentInput);
            if (bestResult == null || testResult.turns < bestResult.turns) {
                bestResult = testResult;
                bestInput = currentInput;
            }
            scoringPlaygroundTest.addTestData(testDatas, currentInput, testResult);
        }
        long totalDuration = System.currentTimeMillis() - startTime;

        for (TestData test: testDatas) {
            System.out.println(
                    "\ninput: " + test.testInput +
                            "\naverage turns: " + test.testResult.turns
                            + "\nduration: " + test.testResult.duration / 1000 + " s"
            );
        }
        System.out.println(
        "\n=======\nbest input: " + bestInput +
                "\naverage turns: " + bestResult.turns
                + "\nduration: " + bestResult.duration / 1000 + " s"
                + "\ntotal duration: " + totalDuration / 1000 + " s"
        );
    }

    private List<TestInput> getInputs() {
        return List.of(
                new TestInput(GameParameters.amplitube, GameParameters.speedR, GameParameters.depth)
                ,
                 new TestInput(18, 7, 5),
                new TestInput(18, 8, 5),
                new TestInput(30, 10, 4)
                //,new TestInput(20, 4, 4),
                //new TestInput(20, 4, 4),
                //,new TestInput(24, 10, 4)
        );
    }

    private TestResult doXRepetitions(int testRepetitions, TestInput testInput) {
        List<Integer> repetitionResults = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        GameParameters.depth = testInput.depth;
        GameParameters.speedR = testInput.speedR;
        GameParameters.amplitube = testInput.amplitube;

        for (int i = 0; i < testRepetitions; i++) {
            Entity playerEntity = new Entity();
            SimulateEngine simulationEngine = new SimulateEngine(
                    1,
                    playerEntity,
                    List.of(
                            spawn("checkpoint1", 1000, 500),
                            spawn("checkpoint2", 15000, 500),
                            spawn("checkpoint3", 500, 8000),
                            spawn("checkpoint4", 3000, 2000)
//                            spawn("checkpoint5", 4000, 1000),
//                            spawn("checkpoint6", 100, 8000)
                    )
            );
            playerEntity.setX(500);
            playerEntity.setY(500);

            simulationEngine.playerCodingGame.debug = true;
            simulationEngine.playerCodingGame.isTesting = false;

            boolean firstPodDidOneCompleteTurn = false;
            boolean firstPodHasPassedFirstCheckpoint = false;
            while (!firstPodDidOneCompleteTurn) {
                simulationEngine.playerCodingGame.startTime = System.currentTimeMillis();
                simulationEngine.play();
                if (!firstPodHasPassedFirstCheckpoint && simulationEngine.playerCodingGame.pod1.currentCheckpoint.id == 0) {
                    firstPodHasPassedFirstCheckpoint = true;
                } else if (simulationEngine.playerCodingGame.pod1.currentCheckpoint.id == 0) {
                    firstPodDidOneCompleteTurn = true;
                }
            }
            repetitionResults.add(simulationEngine.playerCodingGame.turn);
        }
        return new TestResult(repetitionResults.stream().mapToInt(Integer::intValue).average().getAsDouble(), System.currentTimeMillis() - startTime);
    }

    class TestInput {
        int amplitube = 18;
        int speedR = 7;
        int depth = 5;

        public TestInput(final int amplitube, final int speedR, final int depth) {
            this.amplitube = amplitube;
            this.speedR = speedR;
            this.depth = depth;
        }

        @Override
        public String toString() {
            return "TestInput{" +
                    "amplitube=" + amplitube +
                    ", speedR=" + speedR +
                    ", depth=" + depth +
                    '}';
        }
    }

    class TestResult {
        double turns;
        long duration;

        public TestResult(final double turns, final long duration) {
            this.turns = turns;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "TestResult{" +
                    "turns=" + turns +
                    ", duration=" + duration +
                    '}';
        }
    }

    class TestData {
        TestInput testInput;
        TestResult testResult;

        public TestData(final TestInput testInput, final TestResult testResult) {
            this.testInput = testInput;
            this.testResult = testResult;
        }

        @Override
        public String toString() {
            return "TestData{" +
                    "testInput=" + testInput +
                    ", testResult=" + testResult +
                    '}';
        }
    }
    void addTestData(List<TestData> list, TestInput i, TestResult r){
        list.add(new TestData(i, r));
    }

    private static Entity spawn(String name, double x, double y) {
        Entity e = new Entity();
        e.setType(name);
        e.setX(x);
        e.setY(y);
        return e;
    }
}
