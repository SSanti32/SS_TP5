package ar.edu.itba.ss;

public class Utils {
    public static int N = 200;

    public static double wallLength = 20.0;
    // variable parameter: 1.2, 1.8, 2.4, 3.0 -> default: 1.2
    public static double exitWidth = 1.2;

    public static double particleCount = 200;

    public static double x_e1 = 0.5 * wallLength - 0.5 * exitWidth;
    public static double x_e2 = 0.5 * wallLength + 0.5 * exitWidth;

    public static double targetX;
    public static double targetY = 0.0;

    public static double minRadius = 0.1;
    public static double maxRadius = 0.37;
    public static double beta = 0.9;
    public static double maxSpeed = 0.95;
    public static double desiredSpeed = 2.0;

    // TODO: Check value of escape speed
    public static double step = minRadius / (2 * desiredSpeed);

    public static double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
