package acevedor.codinggame.podrace;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class MathUtils {
    public static double truncate(double d) {
        if (d == 0) {
            return d;
        }else if (d > 0) {
            return floor(d);
        } else {
            return ceil(d);
        }
    }

}
