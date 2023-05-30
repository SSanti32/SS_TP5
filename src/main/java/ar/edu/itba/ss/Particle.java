package ar.edu.itba.ss;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class Particle {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    long id;
    double minRadius, radius, maxRadius;
    double velocity, escapeVel;
    double x, y;
    double speed, escapeSpeed, maxSpeed;
    double beta;

    double deltaT = minRadius / (2 * Math.max(maxSpeed, escapeSpeed));
    double tao = 0.5; // check value

    boolean inContact = false; //ESTO DEBERIA SER UNA FUNCION
    public Particle(double x, double y, double radius, double velocity) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocity = velocity;
        this.speed = desiredSpeed(Utils.maxSpeed, radius, Utils.minRadius, Utils.maxRadius, Utils.beta);
    }

    public boolean sameStartPosition(Particle p) {
        double distance = Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
        return distance <= (this.radius + p.radius);
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public static double desiredSpeed(double maxSpeed, double radius, double minRadius, double maxRadius, double beta) {
        return maxSpeed * Math.pow((radius- minRadius)/(maxRadius - minRadius), beta);
    }

    public static double[] updatePosition(double prevX, double prevY, double velocity, double deltaT) {
        double x = prevX + velocity * deltaT;
        double y = prevY + velocity * deltaT;
        return new double[]{x, y};
    }


    public double deltaR() {
//        this.radius += maxRadius / (tao / deltaT);
        return (maxRadius / (tao / deltaT));
    }

    public double[] calculateEscapeVelocity(List<Particle> otherParticles) {
        double[] e = new double[] {0, 0};
        double[] auxE;
        for (Particle p : otherParticles) {
            auxE = getEscapeVelocityDirAndSense(p);
            e[0] += auxE[0];
            e[1] += auxE[1];
        }
        double ve_x = escapeSpeed * (e[0] / Math.abs(e[0]));
        double ve_y = escapeSpeed * (e[1] / Math.abs(e[1]));

        return new double[] {ve_x, ve_y};
    }

    // TODO: Add wall calculations
    private double[] getEscapeVelocityDirAndSense(Particle other) {
        double e_x = (this.x - other.x) / Math.abs(this.x - other.x);
        double e_y = (this.y - other.y) / Math.abs(this.y - other.y);

        return new double[] {e_x, e_y};
    }

    private double[] getEscapeVelocityDirAndSenseForWall(Particle other) {

        double e_x = (this.x - other.x) / Math.abs(this.x - other.x);
        double e_y = (this.y - other.y) / Math.abs(this.y - other.y);

        return new double[] {e_x, e_y};
    }

    // TODO: Check target x value
    private double[] getTargetVelocityDirAndSense() {
        double e_x = (this.x - Utils.wallLength / 2) / Math.abs(this.x - Utils.wallLength / 2);
        double e_y = (this.x - Utils.targetY) / Math.abs(this.x - Utils.targetY);

        return new double[] {e_x, e_y};
    }
}
