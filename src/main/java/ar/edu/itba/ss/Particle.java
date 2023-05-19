package ar.edu.itba.ss;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
@Getter
@Setter
public class Particle {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final long id;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double ax = 0;
    private double ay = 0;
    private double r3x = 0;
    private double r3y = 0;
    private double r4x = 0;
    private double r4y = 0;
    private double r5x = 0;
    private double r5y = 0;
    private double[] forces;

    private final double radius;
    private final double mass;
    private final int colorR;
    private final int colorG;
    private final int colorB;

    private final String symbol;

    public Particle(double x, double y, double vx, double vy, double radius,
                    double mass, int colorR, int colorG, int colorB,
                    String symbol) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.forces = new double[] {0, 0};
        this.radius = radius;
        this.mass = mass;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.symbol = symbol;
    }

    private double[] calculateDeltaR2(double step) {
        double[] deltaR2 = new double[2];

        double factor = Math.pow(step, 2) / 2;

        deltaR2[0] = (this.forces[0] / this.mass - this.ax) * factor;
        deltaR2[1] = (this.forces[1] / this.mass - this.ay) * factor;

        return deltaR2;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle p = (Particle) o;
        return id == p.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
