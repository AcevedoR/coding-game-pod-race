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
    boolean isLoggingPositions = false;
    boolean withOptimization = true;

    int turn = 1;
    Pod pod2;
    Pod pod1;
    long startTime;
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
            moveWithSimulationOptimized();
        }
    }

    private void moveWithSimulationOptimized() {
        long solutionsGenerationTime = System.currentTimeMillis();
        if(isLoggingPerfs) {
            System.err.println("starting simulation time: " + (solutionsGenerationTime - startTime));
        }

        Solutionn currentSolution = new Solutionn(pod1, getEmptyMoveList(GameParameters.depth));
        Solutionn currentSolution2 = new Solutionn(pod2, getEmptyMoveList(GameParameters.depth));

        boolean lastSolutionWasBetter = false;
        boolean lastSolution2WasBetter = false;
        if(lastSolution != null){
            lastSolution.setPod(pod1);
            lastSolution.shift();
        }
        if(lastSolution2 != null){
            lastSolution2.setPod(pod2);
            lastSolution2.shift();
        }

        Solutionn best = null;
        Solutionn best2 = null;
        double maxScore = -9999999;
        double maxScore2 = -9999999;
        if(isLoggingPositions) {
            System.err.println("x:" + pod1.position.x + " y:" + pod1.position.y + " vx:" + pod1.vx + " vy:" + pod1.vy + " angle" + pod1.angle);
        }
        int i= 0;
        while (true) {
            if ((System.currentTimeMillis() - startTime > GameParameters.TIMEOUT_MAX && !isTesting ) || (isTesting && i > 50000)) {
                if (isLoggingPerfs) {
                    System.err.println("Breaked out of simulation loop at index: " + i);
                }
                break;
            }
            currentSolution.replaceMovesWithRandom(GameParameters.amplitube, GameParameters.speedR);
            currentSolution2.replaceMovesWithRandom(GameParameters.amplitube, GameParameters.speedR);

            double score = currentSolution.score();
            if (score > maxScore) {
                best = new Solutionn(currentSolution);
                maxScore = score;
                lastSolutionWasBetter = false;
            }
            double score2 = currentSolution2.score();
            if (score2 > maxScore2) {
                best2 = new Solutionn(currentSolution2);
                maxScore2 = score2;
                lastSolution2WasBetter = false;
            }

            /*if(this.lastSolution != null && this.lastSolution2 != null){
                lastSolution.mutate();
                lastSolution2.mutate();
                double scoreLastMutated = lastSolution.score();
                if (scoreLastMutated > maxScore) {
                    best = new Solutionn(lastSolution);
                    maxScore = scoreLastMutated;
                    lastSolutionWasBetter = true;
                }
                double score2LastMutated = lastSolution2.score();
                if (score2LastMutated > maxScore2) {
                    best2 = new Solutionn(lastSolution2);
                    maxScore2 = score2LastMutated;
                    lastSolution2WasBetter = true;
                }
            }*/
            i++;
        }
        if(isLoggingPerfs) {
            System.err.println("time: " + (System.currentTimeMillis() - startTime));
        }
        lastSolution = new Solutionn(best);
        lastSolution2 = new Solutionn(best2);
        if(lastSolutionWasBetter) {
            System.err.println("last solution was better");
        }
        if(lastSolution2WasBetter) {
            System.err.println("last solution 2 was better");
        }
        movePods(best, best2);
    }

    private void movePods(final Solutionn best, final Solutionn best2) {
        pod1.playy(best.moves1.get(0));
        lastSolution = best;
        pod2.playy(best2.moves1.get(0));
        lastSolution2 = best2;
        if(isLoggingPositions) {
            System.err.println("moving:" + best.moves1.get(0));
            System.err.println("x:" + pod1.position.x + " y:" + pod1.position.y + " vx:" + pod1.vx + " vy:" + pod1.vy + " angle" + pod1.angle);
        }
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

    private List<Move> getEmptyMoveList(int depth){
        List<Move> moves = new ArrayList<>();
        for(int i = 0; i < depth; i++) {
            moves.add(new Move(0, 0));
        }
        return moves;
    }
}