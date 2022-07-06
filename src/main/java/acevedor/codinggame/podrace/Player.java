package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class Player {

    boolean isTesting = false;
    boolean debug=false;
    boolean isLoggingPerfs = true;
    boolean withOptimization = true;

    int turn = 1;
    Pod pod2;
    Pod pod1;
    long startTime;
    List<Move> generatedMoves;
    Solutionn lastSolution = null;
    Solutionn lastSolution2 = null;

    public static void main(String args[]) {
        Player player = new Player();
        player.init();

        Scanner in = new Scanner(System.in);
        int laps = in.nextInt();
        int checkpointCount = in.nextInt();
        for (int i = 0; i < checkpointCount; i++) {
            int checkpointX = in.nextInt();
            int checkpointY = in.nextInt();
            GameCache.checkpointsList.add(new Checkpoint(i, new Point(checkpointX, checkpointY)));
        }

        // game loop
        while (true) {
            int x1 = in.nextInt(); // x position of your pod
            player.startTime = System.currentTimeMillis();
            int y1 = in.nextInt(); // y position of your pod
            int vx1 = in.nextInt(); // x speed of your pod
            int vy1 = in.nextInt(); // y speed of your pod
            int angle1 = in.nextInt(); // angle of your pod
            int nextCheckPointId1 = in.nextInt(); // next check point id of your pod
            int x2 = in.nextInt(); // x position of your pod
            int y2 = in.nextInt(); // y position of your pod
            int vx2 = in.nextInt(); // x speed of your pod
            int vy2 = in.nextInt(); // y speed of your pod
            int angle2 = in.nextInt(); // angle of your pod
            int nextCheckPointId2 = in.nextInt(); // next check point id of your pod
            for (int i = 0; i < 2; i++) {
                int eeeex2 = in.nextInt(); // x position of the opponent's pod
                int eeeey2 = in.nextInt(); // y position of the opponent's pod
                int eeeevx2 = in.nextInt(); // x speed of the opponent's pod
                int eeeevy2 = in.nextInt(); // y speed of the opponent's pod
                int eeeeangle2 = in.nextInt(); // angle of the opponent's pod
                int eeeenextCheckPointId2 = in.nextInt(); // next check point id of the opponent's pod
            }

            player.applyInput(
                x1, y1, vx1, vy1, angle1, nextCheckPointId1,
                x2, y2, vx2, vy2, angle2, nextCheckPointId2
            );
            player.play();
        }
    }

    public void init() {
         GameCache.precalculateAngles();
        for (int i = 0; i < GameParameters.solutionNumber; i++) {
            Solutionn ssss = new Solutionn(
                new ArrayList<Move>()
            );
            GameCache.emptySolutions.add(ssss);
            Solutionn ssss2 = new Solutionn(
                    new ArrayList<Move>()
            );
            GameCache.emptySolutions2.add(ssss2);
        }


        double aInc = 36/ GameParameters.amplitube;
        double speedInc = GameConstants.MAX_THRUST / GameParameters.speedR;
        generatedMoves = new ArrayList<>(GameParameters.amplitube * GameParameters.speedR + GameParameters.amplitube + GameParameters.speedR + 1);
        for (int a = 0; a <= GameParameters.amplitube; a++) {
            for (int s = 0; s <= GameParameters.speedR; s++) {

                double angle = -18 + aInc * a;

                if (angle > 18.0) {
                    angle = 18.0;
                }

                int speed = (int) ( s * speedInc);
                if(s == GameParameters.speedR){
                    speed = GameConstants.MAX_THRUST;
                }

                if (speed < 0) {
                    speed = 0;
                }
                if (speed > GameConstants.MAX_THRUST) {
                    speed = GameConstants.MAX_THRUST;
                }
                generatedMoves.add(new Move(angle, speed));
            }
        }
    }

    void applyInput(
            int x1, int y1, int vx1, int vy1, int angle1, int nextCheckPointId1,
            int x2, int y2, int vx2, int vy2, int angle2, int nextCheckPointId2
    ) {
        // PLAY
        if (turn == 1) {
            pod1 = new Pod(x1, y1, vx1, vy1, angle1, GameCache.checkpointsList.get(nextCheckPointId1));
            pod2 = new Pod(x2, y2, vx2, vy2, angle2, GameCache.checkpointsList.get(nextCheckPointId2));
        }          
        pod1.udpate(x1, y1, vx1, vy1, angle1, GameCache.checkpointsList.get(nextCheckPointId1));
        pod2.udpate(x2, y2, vx2, vy2, angle2, GameCache.checkpointsList.get(nextCheckPointId2));
    }

    void play(){
        if(turn==1 && !isTesting) {
            System.out.println((int) pod1.currentCheckpoint.position.x + " " + (int) pod1.currentCheckpoint.position.y + " BOOST");
            System.out.println((int) pod2.currentCheckpoint.position.x + " " + (int) pod2.currentCheckpoint.position.y + " 100");
            turn++;

        } else {
            turn++;
            if (!withOptimization) {
                moveWithSimulation();
            } else{
                moveWithSimulationOptimized();
            }
        }
    }
    private void moveWithSimulation() {
        long solutionsGenerationTime = System.currentTimeMillis();
        if(isLoggingPerfs) {
            System.err.println("starting simulation time: " + (solutionsGenerationTime - startTime));
        }
        List<Solutionn> ssolutions = generatePopulation(GameCache.emptySolutions, GameParameters.depth, pod1, System.currentTimeMillis());
        List<Solutionn> ssolutions2 = generatePopulation(GameCache.emptySolutions2, GameParameters.depth, pod2, System.currentTimeMillis());

        if(lastSolution != null){
            lastSolution.shift();
            ssolutions.add(0, lastSolution);
        }
        if(lastSolution2 != null){
            lastSolution2.shift();
            ssolutions2.add(0, lastSolution2);
        }
        if(isLoggingPerfs) {
            System.err.println("solutions generation duration: " + (System.currentTimeMillis() - solutionsGenerationTime));
        }
        Solutionn best = null;
        Solutionn best2 = null;
        double maxScore = -9999999;
        double maxScore2 = -9999999;
        System.err.println("x:" + pod1.position.x + " y:" + pod1.position.y + " vx:" +pod1.vx + " vy:" +pod1.vy+ " angle" + pod1.angle);

        for (int i = 0; i < GameParameters.solutionNumber; i++) {
            if(System.currentTimeMillis() - startTime > GameParameters.TIMEOUT_MAX){
                if(isLoggingPerfs) {
                    System.err.println("Breaked out of simulation loop at index: " + i);
                }
                break;
            }
            double score = ssolutions.get(i).score();
            if (score > maxScore) {
                best = ssolutions.get(i);
                maxScore = score;
            }
            double score2 = ssolutions2.get(i).score();
            if (score2 > maxScore2) {
                best2 = ssolutions2.get(i);
                maxScore2 = score2;
            }
        }
        System.err.println("time: "+ ( System.currentTimeMillis() - startTime));
        movePods(best, best2);
    }

    private void moveWithSimulationOptimized() {
        long solutionsGenerationTime = System.currentTimeMillis();
        if(isLoggingPerfs) {
            System.err.println("starting simulation time: " + (solutionsGenerationTime - startTime));
        }

        Solutionn currentSolution = new Solutionn(pod1, getEmptyMoveList(GameParameters.depth));
        Solutionn currentSolution2 = new Solutionn(pod2, getEmptyMoveList(GameParameters.depth));

        if(lastSolution != null){
            lastSolution.shift();
        }
        if(lastSolution2 != null){
            lastSolution2.shift();
        }
        if(isLoggingPerfs) {
            System.err.println("solutions generation duration: " + (System.currentTimeMillis() - solutionsGenerationTime));
        }
        Solutionn best = null;
        Solutionn best2 = null;
        double maxScore = -9999999;
        double maxScore2 = -9999999;
        System.err.println("x:" + pod1.position.x + " y:" + pod1.position.y + " vx:" +pod1.vx + " vy:" +pod1.vy+ " angle" + pod1.angle);

        int i= 0;
        while (true) {
            if(System.currentTimeMillis() - startTime > GameParameters.TIMEOUT_MAX){
                if(isLoggingPerfs) {
                    System.err.println("Breaked out of simulation loop at index: " + i);
                }
                break;
            }
            currentSolution.replaceMovesWithRandom(GameParameters.amplitube, GameParameters.speedR);
            currentSolution.setPod(pod1);
            currentSolution2.replaceMovesWithRandom(GameParameters.amplitube, GameParameters.speedR);
            currentSolution2.setPod(pod2);

            double score = currentSolution.score();
            if (score > maxScore) {
                best = new Solutionn(currentSolution);
                maxScore = score;
            }
            double score2 = currentSolution2.score();
            if (score2 > maxScore2) {
                best2 = new Solutionn(currentSolution2);
                maxScore2 = score2;
            }

//            if(this.lastSolution != null && this.lastSolution2 != null) {
//                double scoreLastMutated = lastSolution.score();
//                if (scoreLastMutated > maxScore) {
//                    best = new Solutionn(lastSolution);
//                    maxScore = scoreLastMutated;
//                }
//                double score2LastMutated = lastSolution2.score();
//                if (score2LastMutated > maxScore2) {
//                    best2 = new Solutionn(lastSolution2);
//                    maxScore2 = score2LastMutated;
//                }
//            }
            i++;
        }
        System.err.println("time: "+ ( System.currentTimeMillis() - startTime));
        lastSolution = new Solutionn(currentSolution);
        lastSolution2 = new Solutionn(currentSolution2);
        movePods(best, best2);
    }

    private void movePods(final Solutionn best, final Solutionn best2) {
        pod1.playy(best.moves1.get(0));
        lastSolution = best;
        pod2.playy(best2.moves1.get(0));
        lastSolution2 = best2;
        System.err.println("moving:"+ best.moves1.get(0));
        System.err.println("x:" + pod1.position.x + " y:" + pod1.position.y + " vx:" +pod1.vx + " vy:" +pod1.vy+ " angle" + pod1.angle);
        Point res = pod1.toResult();
        Point res2 = pod2.toResult();
        Result result = new Result((int) res.x, (int) res.y, best.moves1.get(0).thrust);
        Result result2 = new Result((int) res2.x, (int) res2.y, best2.moves1.get(0).thrust);
        printMove(result);
        printMove(result2);
    }

    private void printMove(Result result) {
        System.out.println(result.x + " " + result.y + " " + result.thrust);
    }

    public List<Solutionn> generatePopulation(List<Solutionn> s, int depth, Pod pod, long methodStartTime) {
        for(int i = 0; i < s.size(); i++) {
            if(System.currentTimeMillis() - methodStartTime > GameParameters.SOLUTIONS_GENERATION_TIMEOUT) {
                if(isLoggingPerfs) {
                    System.err.println("Breaked out of solution generation at index: " + i);
                }
                break;
            }
            Solutionn solution = s.get(i);
            solution.moves1.clear();
            solution.setPod(pod);
            for (int j = 0; j < depth; j++) {
                solution.moves1.add(generatedMoves.get(MathUtils.rrandom(0, generatedMoves.size() - 1)));
            }
        }
        return s;
    }
    private List<Move> getEmptyMoveList(int depth){
        List<Move> moves = new ArrayList<>();
        for(int i = 0; i < depth; i++) {
            moves.add(new Move(0, 0));
        }
        return moves;
    }
}