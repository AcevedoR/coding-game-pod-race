package acevedor.codinggame.podrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class Player {

    static boolean boostAvailable = true;
    static int turn = 1;
    static int lastCheckpointTurn = -100;
     Pod pod;
    static int lastCheckpointX;
    static int lastCheckpointY;

    static int slowingTimeAngle = 0;
    static int slowingTimeAfterCp = 0;

    static boolean areCheckpointsInitialized = false;
    static CheckpointsList checkpoints = new CheckpointsList();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Player player = new Player();
        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
            int nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();

            player.play(x, y, nextCheckpointX, nextCheckpointY, nextCheckpointAngle, nextCheckpointDist);
        }
    }

    Result play(int x, int y, int nextCheckpointX, int nextCheckpointY, int nextCheckpointAngle, int nextCheckpointDist) {
        // PLAY
        Point target = new Point(nextCheckpointX, nextCheckpointY);

        if (turn == 1) {
            pod = Pod.of(x, y, nextCheckpointAngle);
//            pod.angle = pod.getAngle(target);
        }


        // CP
        if (!areCheckpointsInitialized && !checkpoints.exists(nextCheckpointX, nextCheckpointY)) {
            checkpoints.add(new Point(nextCheckpointX, nextCheckpointY));
        } else {
            areCheckpointsInitialized = true;
        }

        double vx = nextCheckpointX;
        double vy = nextCheckpointY;

        if (areCheckpointsInitialized) {

        }


        int thrust = 0;
        double diffAngle = pod.diffAngle(target);
        System.err.println("pod ange: " + pod.angle + " \npod getAngle " + pod.getAngle(target) + " \ndiffangle " + diffAngle + " \nnextch " + nextCheckpointAngle);
        System.err.println("pod x: " + pod.position.y + " \npod y " + pod.position.x);
        if (boostAvailable && (nextCheckpointAngle < 3 && nextCheckpointAngle > -3) && nextCheckpointDist > 5000) {
            boostAvailable = false;
            System.out.println((int) vx + " " + (int) vy + " BOOST");
            return new Result((int) vx, (int) vy, 10);
//            } else if (lastCPTakenTime > 5 && nextCheckpointDist < 2000) {
//                thrust = 30;
//                System.err.println("slow because getting close");
//                slowingTimeAngle++;
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
            thrust = 100;
            int slowDist = 2000;
            if (nextCheckpointDist < slowDist) {
                double ratio = (1.0 * (slowDist - nextCheckpointDist)) / slowDist;
                ratio = Math.pow(ratio, 1);
                thrust = 60 + (int) (40 * ratio);
            }
            System.err.println("fast");
        }

//            pod.play(new Point(nextCheckpointX, nextCheckpointY), thrust);
        System.out.println((int) vx + " " + (int) vy + " " + thrust);

        turn++;
        return new Result((int) vx,(int) vy ,thrust);
    }


}