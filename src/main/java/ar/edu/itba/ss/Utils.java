package ar.edu.itba.ss;

public class Utils {

    public static double wallLength = 20.0;
    // variable parameter: 1.2, 1.8, 2.4, 3.0 -> default: 1.2
    public static double targetLength = 1.2;
    public static double farTargetLength = 3;

    public static int particleCount = 200;
    public static double x_e1 = 0.5 * wallLength - 0.5 * (targetLength - 0.2);
    public static double x_e2 = 0.5 * wallLength + 0.5 * (targetLength - 0.2);

    public static double targetY = 0.0;
    public static double secondTargetX1 = wallLength / 2 - farTargetLength / 2;
    public static double secondTargetX2 = wallLength / 2 + farTargetLength / 2;
    public static double secondTargetY = -10.0;

    public static double minRadius = 0.1;
    public static double maxRadius = 0.37;
    public static double beta = 0.9;
    public static double maxDesiredSpeed = 1.0;

    // TODO: Check value of escape speed
    public static double step = minRadius / (2 * maxDesiredSpeed);
    public static double tao = 0.5;

    public static double getParticleDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
