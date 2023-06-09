package acevedor.codinggame.podrace;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class MathUtils {
    public static double random(double min, double max) {
        return min + (max - min) * ThreadLocalRandom.current().nextDouble();
    }

    public static int rrandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    public static boolean randomBoolean() {
        return java.util.concurrent.ThreadLocalRandom.current().nextBoolean();
    }

    public static int clamp(double x, int min, int max){
        if(x > max){
            return max;
        }
        if(x < min){
            return min;
        }
        return (int) x;
    }

    public static double truncate(double d) {
        if (d == 0) {
            return d;
        }else if (d > 0) {
            return floor(d);
        } else {
            return ceil(d);
        }
    }
    public static double speed(double vx, double vy) {
        return Math.sqrt((vx * vx) + (vy * vy));
    }
}
