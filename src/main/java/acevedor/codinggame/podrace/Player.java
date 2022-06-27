package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class Player {

    boolean isTesting = false;
    boolean debug=false;
    boolean isLoggingPerfs = true;
    boolean boostAvailable = true;
    int turn = 1;
    Pod pod2;
    Pod pod1;
    int oldx;
    int oldy;
    int maxThrust = 100;
    int depth = 5;
    int solutionNumber = 20000;
    long startTime;

    int amplitube = 18;
    int speedR = 7;
    List<Move> generatedMoves = new ArrayList<>();

    public static void main(String args[]) {
        Player player = new Player();
        player.init();

        Scanner in = new Scanner(System.in);
        int laps = in.nextInt();
        int checkpointCount = in.nextInt();
        for (int i = 0; i < checkpointCount; i++) {
            int checkpointX = in.nextInt();
            int checkpointY = in.nextInt();
            GameState.checkpointsList.add(new Checkpoint(i, new Point(checkpointX, checkpointY)));
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
        double aInc = 36/ amplitube;
        double speedInc = 100 / speedR;
        for (int a = 0; a <= amplitube; a++) {
            for (int s = 0; s <= speedR; s++) {

                double angle = -18 + aInc * a;

                if (angle > 18.0) {
                    angle = 18.0;
                }

                int speed = (int) ( s * speedInc);
                if(s == speedR){
                    speed = 100;
                }

                if (speed < 0) {
                    speed = 0;
                }
                if (speed > 100) {
                    speed = 100;
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
            pod1 = new Pod(x1, y1, vx1, vy1, angle1, GameState.checkpointsList.get(nextCheckPointId1));
            pod2 = new Pod(x2, y2, vx2, vy2, angle2, GameState.checkpointsList.get(nextCheckPointId2));
        }          
        pod1.udpate(x1, y1, vx1, vy1, angle1, GameState.checkpointsList.get(nextCheckPointId1));
        pod2.udpate(x2, y2, vx2, vy2, angle2, GameState.checkpointsList.get(nextCheckPointId2));
    }

    void play(){
        if(turn==1 && !isTesting) {
            System.out.println((int) pod1.currentCheckpoint.position.x + " " + (int) pod1.currentCheckpoint.position.y + " BOOST");
            System.out.println((int) pod2.currentCheckpoint.position.x + " " + (int) pod2.currentCheckpoint.position.y + " 100");
            turn++;

        } else {
            turn++;
            moveWithSimulation();
        }
    }
    private void moveWithSimulation() {
        long solutionsGenerationTime = System.currentTimeMillis();
        Solutionn[] solutions = generatePopulation(depth, solutionNumber, pod1);
        Solutionn[] solutions2 = generatePopulation(depth, solutionNumber, pod2);
        if(isLoggingPerfs) {
            System.err.println("solutions generation duration: " + (System.currentTimeMillis() - solutionsGenerationTime));
        }
        Solutionn best = null;
        Solutionn best2 = null;
        double maxScore = -9999999;
        double maxScore2 = -9999999;
        System.err.println("x:" + pod1.position.x + " y:" + pod1.position.y + " vx:" +pod1.vx + " vy:" +pod1.vy+ " angle" + pod1.angle);

        long lastLoopTime = System.currentTimeMillis();
        for (int i = 0; i < solutionNumber; i++) {
            long lastLoopDuration = System.currentTimeMillis() - lastLoopTime;
            if(System.currentTimeMillis() + lastLoopDuration - startTime > 73){
                if(isLoggingPerfs) {
                    System.err.println("loop time: " + lastLoopDuration);
                    System.err.println("Breaked out of simulation loop at index: " + i);
                }
                break;
            }
            double score = solutions[i].score();
            if (score > maxScore) {
                best = solutions[i];
                maxScore = score;
            }
            double score2 = solutions2[i].score();
            if (score2 > maxScore2) {
                best2 = solutions2[i];
                maxScore2 = score2;
            }
            lastLoopTime = System.currentTimeMillis();
        }
        System.err.println("time: "+ ( System.currentTimeMillis() - startTime));
        pod1.playy(best.moves1.get(0));
        pod2.playy(best2.moves1.get(0));
        System.err.println("moving:"+best.moves1.get(0));
        System.err.println("x:" + pod1.position.x + " y:" + pod1.position.y + " vx:" +pod1.vx + " vy:" +pod1.vy+ " angle" + pod1.angle);
        Point res = pod1.toResult();
        Point res2 = pod2.toResult();
        Result result = new Result((int) res.x, (int) res.y, best.moves1.get(0).thrust);
        Result result2 = new Result((int) res2.x, (int) res2.y, best2.moves1.get(0).thrust);
        printMove(result);
        printMove(result2);
    }


    public int clamp(double x, int min, int max){
        if(x > max){
            return max;
        }
        if(x < min){
            return min;
        }
        return (int) x;
    }
    private void printMove(Result result) {
        System.out.println(result.x + " " + result.y + " " + result.thrust);
    }

    public Solutionn[] generatePopulation(int depth, int solutionNumber, Pod pod) {
        Solutionn[] solutions = new Solutionn[solutionNumber];
        for (int i = 0; i < solutionNumber; i++) {
            Solutionn ssss = new Solutionn (
                    new ArrayList<Move>(),
                    pod
            );
            solutions[i] = ssss;
            for (int j = 0; j < depth; j++) {
                ssss.moves1.add(generatedMoves.get(rrandom(0, generatedMoves.size() - 1)));
            }
        }

        return solutions;
    }
    int rrandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max - min) + min;
    }

}