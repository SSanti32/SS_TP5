package ar.edu.itba.ss;

public class Utils {
    public static Double particleMass = 0.165; // kg
    public static Double particleRadius = 2.85; // cm
    public static Double topEpsilon = 0.03;
    public static Double bottomEpsilon = 0.02;
    public static double[] getDeltaV(double vx1, double vy1, double vx2,
                                     double vy2) {
        double[] deltaV = new double[2];
        deltaV[0] = vx2 - vx1;
        deltaV[1] = vy2 - vy1;
        return deltaV;
    }
    public static double[] getDeltaR(double x1, double y1, double x2,
                                     double y2) {
        double[] deltaR = new double[2];
        deltaR[0] = x2 - x1;
        deltaR[1] = y2 - y1;
        return deltaR;
    }
    public static double getScalarProduct(double[] v1, double[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1];
    }

    public static Particle createParticle(double relativeParticleX,
                                          double relativeParticleY,
                                          double sign, int colorR, int colorG,
                                          int colorB, String symbol) {
        double hypothenuse = 2 * (Utils.particleRadius + Utils.topEpsilon);
        double moveInX = hypothenuse * Math.cos(Math.toRadians(30));
        double moveInY = hypothenuse * Math.sin(Math.toRadians(30));
        return new Particle(relativeParticleX + moveInX,
                relativeParticleY + moveInY * sign, 0, 0,
                Utils.particleMass, Utils.particleRadius, colorR, colorG,
                colorB, symbol);
    }
}
