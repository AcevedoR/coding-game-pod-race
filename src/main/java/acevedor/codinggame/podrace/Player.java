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
    boolean boostAvailable = true;
    int turn = 1;
    int lastCheckpointTurn = -100;
    Pod pod;
    int lastCheckpointX;
    int lastCheckpointY;
    int firstCheckpointX;
    int firstCheckpointY;
    int slowingTimeAngle = 0;
    int slowingTimeAfterCp = 0;
    int oldx;
    int oldy;
    int maxThrust = 100;
    int solutionNumber = 40000;
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

    Result play(int x, int y, int nextCheckpointX, int nextCheckpointY, int nextCheckpointAngle) {
        // PLAY
        Point target = new Point(nextCheckpointX, nextCheckpointY);

        if (turn == 1) {
            pod = Pod.of(x, y, nextCheckpointX, nextCheckpointY);
            gs.pod = pod;
            oldx = x;
            oldy = y;
        }
        pod.udpate(x, y, nextCheckpointX, nextCheckpointY, oldx, oldy);
        oldx = x;
        oldy = y;
        // CP
        if(!gs.areCheckpointsInitialized) {

            if (!gs.checkpointsList.exists(nextCheckpointX, nextCheckpointY)) {
                if (gs.checkpointsList.size() == 0) {
                    firstCheckpointX = nextCheckpointX;
                    firstCheckpointY = nextCheckpointY;
                }
                gs.checkpointsList.add(new Point(nextCheckpointX, nextCheckpointY));
                System.err.println("new checkpoint");
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

//        System.err.println("angle:"+pod.angle + " currentCPAngle:"+nextCheckpointAngle + " x:"+pod.position.x+" y:" + pod.position.y + " ");


        double nextCheckpointDist = pod.position.distance(target);
        double vx = nextCheckpointX;
        double vy = nextCheckpointY;

        if(turn==1) {
            System.out.println(nextCheckpointX + " " + nextCheckpointY + " BOOST");
            turn++;

            return new Result(nextCheckpointX, nextCheckpointY, 100);
        } else {
            turn++;

            return moveWithSimulation();
        }
//        if (gs.areCheckpointsInitialized) {
//            return moveWithSimulation();
//        } else {
//            return moveSimply(nextCheckpointAngle, nextCheckpointDist, (int) vx, (int) vy);
//        }

    }

    private Result moveWithSimulation() {
        Solutionn[] solutions = generatePopulation(5, solutionNumber);
        Solutionn best = null;
        double maxScore = -9999999;
        System.err.println("x:" + pod.position.x + " y:" + pod.position.y + " vx:" +pod.vx + " vy:" +pod.vy+ " angle" + pod.angle);

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
                    System.err.print("is better: "+maxScore+" - " + best.moves1.get(0));
                }
            }
            if(debug) {
                System.err.println("solution:" + i + " score:" + score + " " + solutions[i].moves1.get(0));
            }
        }
        System.err.println("time: "+ ( System.currentTimeMillis() - startTime));
        pod.playy(best.moves1.get(0));
        System.err.println("x:" + pod.position.x + " y:" + pod.position.y + " vx:" +pod.vx + " vy:" +pod.vy+ " angle" + pod.angle);
        Point res = pod.toResult();
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
        } else {
            thrust = maxThrust;
            int slowDist = 1000;
            if (nextCheckpointDist < slowDist) {
                double ratio = (1.0 * (slowDist - nextCheckpointDist)) / slowDist;
                ratio = Math.pow(ratio, 1);
                thrust = 60 + (int) (40 * ratio);
            }
            if(nextCheckpointAngle > 90) {
                int angleRatio = clamp((1 - nextCheckpointAngle * 90), 0, 1);
                thrust = thrust * angleRatio;
            }
            thrust = clamp(thrust, 0, maxThrust);
        }

        Result result = new Result(vx, vy, thrust);
        printMove(result);
        return result;
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