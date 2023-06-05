package ar.edu.itba.ss;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    Set<Particle> contacts;
    List<Wall> wallsInContact;

    public Particle(double x, double y, double radius, double velocity) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocity = velocity;
        this.escapeSpeed = Utils.maxDesiredSpeed;
        this.speed = calculateDesiredSpeed();
        this.hasLeftBox = false;
        calculateTarget();
        calculateDesiredVelocity();
    }

    public void calculateDesiredVelocity() {
        double desiredSpeed = calculateDesiredSpeed();
        double distance = Math.sqrt(Math.pow(this.x - this.target[0], 2) + Math.pow(this.y - this.target[1], 2));
        double[] versor = new double[] {(this.target[0] - this.x) / distance, (this.target[1] - this.y) / distance};

        double angle = Math.atan2(versor[1], versor[0]);
        this.v = new double[] {desiredSpeed * Math.cos(angle), desiredSpeed * Math.sin(angle)};
    }

    private double calculateDesiredSpeed() {
        return Utils.maxDesiredSpeed * Math.pow((radius - Utils.minRadius)/(Utils.maxRadius - Utils.minRadius), Utils.beta);
    }

    public void updateRadius() {
        this.radius += Utils.maxRadius / (Utils.tao / Utils.step);
    }

    public double[] getEscapeVelocityDirAndSense(Particle other) {
        double distance = Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        double e_x = (this.x - other.x) / distance;
        double e_y = (this.y - other.y) / distance;

        return new double[] {e_x, e_y};
    }

    public double[] getEscapeVelocityDirAndSense(Wall other) {
        double distance = Math.sqrt(Math.pow(this.x - other.getX(), 2) + Math.pow(this.y - other.getY(), 2));
        double e_x = (this.x - other.getX()) / distance;
        double e_y = (this.y - other.getY()) / distance;

        return new double[] {e_x, e_y};
    }


    public void calculateTarget() {
        if (x >= Utils.x_e1 && x <= Utils.x_e2)
            target = new double[] {x, Utils.targetY};
        else
            target = new double[] {getRandomTargetPosition(Utils.x_e1, Utils.x_e2), Utils.targetY};
    }

    private double getRandomTargetPosition(double start, double end) {
        return start + Math.random() * (end - start);
    }

    public void calculateTargetOutsideRoom() {
        if (x >= Utils.secondTargetX1 && x <= Utils.secondTargetX2)
            target = new double[] {x, Utils.secondTargetY};
        else
            target = new double[] {getRandomTargetPosition(Utils.secondTargetX1, Utils.secondTargetX2), Utils.secondTargetY};
    }

    public void calculateWallContacts() {
        wallsInContact = new ArrayList<>();
        if (x - radius <= 0)
            wallsInContact.add(new Wall(0, y));
        if (x + radius >= Utils.wallLength)
            wallsInContact.add(new Wall(Utils.wallLength, y));
        // In this case, take into account the exit
        if (y - radius <= 0 && (x + radius < Utils.x_e1 || x - radius > Utils.x_e2))
            wallsInContact.add(new Wall(x, 0));
        if (y + radius >= Utils.wallLength)
            wallsInContact.add(new Wall(x, Utils.wallLength));
    }

    public void calculateVelocity() {
        if (contacts.size() > 0 || wallsInContact.size() > 0) {
            calculateEscapeVelocity();
        }
        else {
            calculateDesiredVelocity();
        }
    }

    private void calculateEscapeVelocity() {
        double[] sum = new double[] {0, 0};
        for (Particle p : contacts) {
            double[] e = getEscapeVelocityDirAndSense(p);
            sum[0] += e[0];
            sum[1] += e[1];
        }
        for (Wall w : wallsInContact) {
            double[] e = getEscapeVelocityDirAndSense(w);
            sum[0] += e[0];
            sum[1] += e[1];
        }
        double norm = Math.sqrt(Math.pow(sum[0], 2) + Math.pow(sum[1], 2));
        this.v = new double[] {Utils.maxDesiredSpeed * (sum[0] / norm), Utils.maxDesiredSpeed * (sum[1] / norm)};
    }

    public void calculatePosition() {
        x = x + v[0] * Utils.step;
        y = y + v[1] * Utils.step;

        if (y - radius <= 0 && (x>= Utils.x_e1 && x <= Utils.x_e2)) {
            hasLeftBox = true;
        }
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
