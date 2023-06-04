package ar.edu.itba.ss;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;


@Getter
@Setter
public class Particle {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    long id;
    double radius;
    double velocity;
    double[] v, vd, ve;
    double[] target;
    double x, y;
    double speed, escapeSpeed, maxSpeed, desiredSpeed;
    boolean hasLeftBox;
    boolean inContact;


    public Particle(double x, double y, double radius, double velocity) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocity = velocity;
        this.escapeSpeed = Utils.maxDesiredSpeed;
        this.speed = calculateDesiredSpeed(Utils.maxSpeed, radius, Utils.minRadius, Utils.maxRadius, Utils.beta);
        this.hasLeftBox = false;
        this.inContact = false;
        calculateTarget(Utils.x_e1 + 0.2 * Utils.targetLength, Utils.x_e1 + 0.8 * Utils.targetLength);
        calculateDesiredVelocity();
    }

    public void calculateDesiredVelocity() {
        double desiredSpeed = calculateDesiredSpeed(Utils.maxSpeed, this.radius, Utils.minRadius, Utils.maxRadius, Utils.beta);
        double distance = Math.sqrt(Math.pow(this.target[0], 2) + Math.pow(this.target[1], 2));
        double[] versor = new double[] {this.target[0] / distance, this.target[1] / distance};

        this.vd = new double[] {desiredSpeed * versor[0], desiredSpeed * versor[1]};
    }

    private double calculateDesiredSpeed(double maxSpeed, double radius, double minRadius, double maxRadius, double beta) {
        return maxSpeed * Math.pow((radius - minRadius)/(maxRadius - minRadius), beta);
    }

    public double[] updatePosition(double prevX, double prevY, double velocity, double deltaT) {
        double x = prevX + velocity * deltaT;
        double y = prevY + velocity * deltaT;
        return new double[]{x, y};
    }


    public void updateRadius() {
        this.radius += Utils.maxRadius / (Utils.tao / Utils.step);
    }

//    public double[] calculateEscapeVelocity(List<Particle> otherParticles) {
//        double[] e = new double[] {0, 0};
//        double[] auxE;
//        for (Particle p : otherParticles) {
//            auxE = getEscapeVelocityDirAndSense(p);
//            e[0] += auxE[0];
//            e[1] += auxE[1];
//        }
//
//        if (Math.abs(this.x) <= 2 * Utils.maxRadius)
//            e[0] += (this.x / Math.abs(this.x));
//        if (Math.abs(this.y) <= 2 * Utils.maxRadius)
//            e[1] += (this.y / Math.abs(this.y));
//        if (Math.abs(this.x - Utils.wallLength) <= 2 * Utils.maxRadius)
//            e[0] += (this.x - Utils.wallLength) / Math.abs(this.x - Utils.wallLength);
//        if (Math.abs(this.y - Utils.wallLength) <= 2 * Utils.maxRadius)
//            e[1] += (this.y - Utils.wallLength) / Math.abs(this.y - Utils.wallLength);
//
//        double ve_x = escapeSpeed * (e[0] / Math.abs(e[0]));
//        double ve_y = escapeSpeed * (e[1] / Math.abs(e[1]));
//
//        return new double[] {ve_x, ve_y};
//    }

    public double[] getEscapeVelocityDirAndSense(Particle other) {
        double distance = Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        double e_x = (this.x - other.x) / distance;
        double e_y = (this.y - other.y) / distance;

        return new double[] {e_x, e_y};
    }



    // TODO: Check target x value
//    private double[] getTargetVelocityDirAndSense() {
//        double e_x = (this.x - Utils.wallLength / 2) / Math.abs(this.x - Utils.wallLength / 2);
//        double e_y = (this.x - Utils.targetY) / Math.abs(this.x - Utils.targetY);
//
//        return new double[] {e_x, e_y};
//    }

    public void calculateTarget(double start, double end) {
        double targetX = x;
        double targetY = Utils.targetY;

        if (x < start || x > end)
            targetX = getRandomTargetPosition(start, end);
        if (hasLeftBox)
            targetY = Utils.secondTargetY;

        target = new double[] {targetX, targetY};
    }

    private double getRandomTargetPosition(double start, double end) {
        return start + Math.random() * (end - start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
