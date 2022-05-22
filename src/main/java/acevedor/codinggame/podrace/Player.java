package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class Player {

    boolean debug=false;
    static boolean boostAvailable = true;
    static int turn = 1;
    static int lastCheckpointTurn = -100;
    Pod pod;
    static int lastCheckpointX;
    static int lastCheckpointY;
    int firstCheckpointX;
    int firstCheckpointY;
    static int slowingTimeAngle = 0;
    static int slowingTimeAfterCp = 0;
    int maxThrust = 100;

    long startTime;
    public GameState gs = new GameState();

    public static Random random =  new Random();

    int amplitube = 18;
    int speedR = 7;
    List<Move> generatedMoves = new ArrayList<>();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Player player = new Player();
        player.init();

        // game loop
        while (true) {
            int x = in.nextInt();
            player.startTime = System.currentTimeMillis();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
            int nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();
            player.play(x, y, nextCheckpointX, nextCheckpointY, nextCheckpointAngle);
        }
    }

    private void init() {
        double aInc = 36/ amplitube;
        double speedInc = 100 / speedR;
        for (int a = 0; a <= amplitube; a++) {
            for (int s = 0; s <= speedR; s++) {

                double angle = -18 + aInc * a;

                if (angle > 18.0) {
                    angle = 18.0;
                }

                int speed = (int) ( s * speedInc);

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

    Result play(int x, int y, int nextCheckpointX, int nextCheckpointY, int nextCheckpointAngle) {
        // PLAY
        Point target = new Point(nextCheckpointX, nextCheckpointY);

        if (turn == 1) {
            pod = Pod.of(x, y, nextCheckpointAngle);
            gs.pod = pod;
        }
        pod.udpate(x, y, nextCheckpointX, nextCheckpointY, nextCheckpointAngle);

        // CP
        if(!gs.areCheckpointsInitialized) {

            if (!gs.checkpointsList.exists(nextCheckpointX, nextCheckpointY)) {
                if (gs.checkpointsList.size() == 0) {
                    firstCheckpointX = nextCheckpointX;
                    firstCheckpointY = nextCheckpointY;
                }
                gs.checkpointsList.add(new Point(nextCheckpointX, nextCheckpointY));
            } else {
                if( (nextCheckpointX == firstCheckpointX && nextCheckpointY == firstCheckpointY) && gs.checkpointsList.size() >1) {
                    gs.areCheckpointsInitialized = true;
                }

            }
        }else {
            if(gs.checkpointsList.currentCheckpoint.position.x != nextCheckpointX && gs.checkpointsList.currentCheckpoint.position.y != nextCheckpointY){
                gs.checkpointsList.passToNextCheckpoint();
            }
        }

        System.err.println("angle:"+pod.angle + " currentCPAngle:"+nextCheckpointAngle + " x:"+pod.position.x+" y:" + pod.position.y + " ");


        double nextCheckpointDist = pod.position.distance(target);
        double vx = nextCheckpointX;
        double vy = nextCheckpointY;

        if (gs.areCheckpointsInitialized) {
            return moveWithSimulation();
        } else {
            return moveSimply(nextCheckpointAngle, nextCheckpointDist, (int) vx, (int) vy);
        }
    }

    private Result moveWithSimulation() {
        int solutionNumber = 10000;
        Solutionn[] solutions = generatePopulation(8, solutionNumber);
        Solutionn best = null;
        double maxScore = -9999999;

        for (int i = 0; i < solutionNumber; i++) {
            if(System.currentTimeMillis() - startTime > 74){
                System.err.println("Breaked out of simulation loop at index: " + i);
                break;
            }
            double score = solutions[i].score();


            if (score > maxScore) {
                best = solutions[i];
                maxScore = score;
                if(debug) {
                    System.err.print(" is better !");
                }
            }
            if(debug) {
                System.err.println("solution:" + i + " score:" + score + " " + solutions[i].moves1.get(0));
            }
        }
        System.err.println("time: "+ ( System.currentTimeMillis() - startTime));
        Point res = pod.toResult(best.moves1.get(0));
        Result result = new Result((int) res.x, (int) res.y, best.moves1.get(0).thrust);
        printMove(result);
        return result;
    }

    private Result moveSimply(final int nextCheckpointAngle, final double nextCheckpointDist, final int vx, final int vy) {
        int thrust = 0;
        if (boostAvailable && (nextCheckpointAngle < 3 && nextCheckpointAngle > -3) && nextCheckpointDist > 4000) {
            boostAvailable = false;
            System.out.println(vx + " " + vy + " BOOST");
            return new Result(vx, vy, 100);
        } else if (nextCheckpointDist < 2000) {
            thrust = 30;
            if (debug) {
                System.err.println("slow because getting close");
            }
            slowingTimeAngle++;
//            } else if (lastCPTakenTime > 2 && lastCPTakenTime < 7) {
//                pod.position.distance(new Point(lastCheckpointX, lastCheckpointY)) < 2000 &&
//                System.err.println("fast after cp");
//                thrust = 100;
//            }else if(nextCheckpointAngle > 80 || nextCheckpointAngle < -80){
//                System.err.println("slow because angle");
//            } else if (nextCheckpointAngle > 30 || nextCheckpointAngle < -30) {
//                thrust = 70;
//                System.err.println("medium");
        } else {
            thrust = maxThrust;
            int slowDist = 2000;
            if (nextCheckpointDist < slowDist) {
                double ratio = (1.0 * (slowDist - nextCheckpointDist)) / slowDist;
                ratio = Math.pow(ratio, 1);
                thrust = 60 + (int) (40 * ratio);
            }
            if(nextCheckpointAngle > 90) {
                thrust += (nextCheckpointAngle - 90) * 0.6;
            }
            if(thrust > maxThrust){
                thrust = maxThrust;
            }
            if(thrust < 0){
                thrust = 0;
            }
            if (debug) {
                System.err.println("fast");
            }
        }

        turn++;
        Result result = new Result(vx, vy, thrust);
        printMove(result);
        return result;
    }

    private void printMove(Result result) {
        System.out.println(result.x + " " + result.y + " " + result.thrust);
    }

    public Solutionn[] generatePopulation(int depth, int solutionNumber) {
        Solutionn[] solutions = new Solutionn[solutionNumber];
        for (int i = 0; i < solutionNumber; i++) {
            Solutionn ssss = new Solutionn(
                    gs.checkpointsList,
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
        return random.nextInt(max - min) + min;
    }

}