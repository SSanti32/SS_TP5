package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static final String FILENAME = "animation-1-step.txt";
    public static final String POSITIONS_FILENAME = "positions-1-step.txt";
    public final static String RESOURCES_PATH_SYSTEM = "src/main/java/resources/";

    private static double currentTime = 0;
    public static List<Particle> particles = new ArrayList<>();
    public static List<Particle> particlesToRemove = new ArrayList<>();

    public static void initializeRoom() {
        for(int i = 0; i < Utils.particleCount;) {
            double x = (Math.random() * (20 - Utils.maxRadius)) + Utils.maxRadius;
            double y = (Math.random() * (20 - Utils.maxRadius)) + Utils.maxRadius;
            Particle p = new Particle(x, y, Utils.maxRadius, 0);
            if(!isOverlapping(p)) {
                particles.add(p);
                i++;
            }
        }
    }

    public static boolean isOverlapping(Particle p1) {
        for(Particle p2: particles) {
            if(Math.abs(Utils.getParticleDistance(p1, p2)) < 2 * Utils.minRadius)
                return true;
        }
        return false;
    }


    public static void main(String[] args) throws IOException {
        initializeRoom();
        FileWriter fileWriter = new FileWriter(FILENAME, true);
        fileWriter.write(Utils.particleCount + "\n");
        fileWriter.write("Comment" + "\n");
        for(Particle p: particles) {
            fileWriter.write(p.getX() + "\t");
            fileWriter.write(p.getY() + "\t");
            fileWriter.write(p.getRadius() + "\t");
            fileWriter.write("He" + "\n");
        }
        simulate(fileWriter);
        fileWriter.close();
    }

    private static void simulate(FileWriter fileWriter) throws IOException {
        int toPrint = 1;
        while (particles.size() > 0 && currentTime < 100) {

            // Find contacts and calculate escape velocity
            for(Particle p1 : particles) {
                double eij_x = 0;
                double eij_y = 0;
                for (Particle p2 : particles) {
                    if (p1.equals(p2) && Utils.getParticleDistance(p1, p2) >= p1.getRadius() + p2.getRadius()) {
                        continue;
                    }
                    double[] currentEij = p1.getEscapeVelocityDirAndSense(p2);
                    eij_x += currentEij[0];
                    eij_y += currentEij[1];
                }

                // Interaction with walls
                if (!p1.hasLeftBox) {
                    // Left wall
                    if (p1.getX() - p1.getRadius() <= 0) {
                        eij_x += p1.getX() / Math.sqrt(Math.pow(p1.getX(), 2) + Math.pow(p1.getY(), 2));
                    }

                    // Right wall
                    if (p1.getX() + p1.getRadius() >= Utils.wallLength) {
                        eij_x += (p1.getX() - Utils.wallLength) / Math.sqrt(Math.pow(Utils.wallLength - p1.getX(), 2) + Math.pow(p1.getY(), 2));
                    }

                    // Bottom wall
                    if (p1.getY() - p1.getRadius() <= 0) {
                        if (p1.getX() >= Utils.x_e1 + 0.1 && p1.getX() <= Utils.x_e2 - 0.1) {
                            p1.setHasLeftBox(true);
                        } else {
                            eij_y += p1.getY() / Math.sqrt(Math.pow(p1.getX(), 2) + Math.pow(p1.getY(), 2));
                        }
                    }

                    // Top wall
                    if (p1.getY() + p1.getRadius() >= Utils.wallLength) {
                        eij_y += (p1.getY() - Utils.wallLength) / Math.sqrt(Math.pow(p1.getX(), 2) + Math.pow(Utils.wallLength - p1.getY(), 2));
                    }
                }

                double distanceEij = Math.sqrt(Math.pow(eij_x, 2) + Math.pow(eij_y, 2));
                p1.setVe(new double[]{eij_x / distanceEij, eij_y / distanceEij});
                if (eij_x != 0 || eij_y != 0) {
                    p1.setInContact(true);
                }
            }

            // Adjust radii
            for (Particle p : particles) {
                if (p.isInContact()) {
                    p.setRadius(Utils.minRadius);
                } else {
                    p.updateRadius();
                }
            }

            // Update positions and velocities
            for (Particle p : particles) {
                // Compute direction and sense of vd considering current positions and target locations
                // Compute magnitude of vd depending on the radius
                if (p.isInContact()) {
                    p.setV(new double[]{p.getVe()[0] * Utils.maxDesiredSpeed, p.getVe()[1] * Utils.maxDesiredSpeed});
                } else {
                    p.calculateDesiredVelocity();
                    p.setV(new double[]{p.getVd()[0], p.getVd()[1]});
                }

                // Update position
                p.setX(p.getX() + p.getV()[0] * Utils.step);
                p.setY(p.getY() + p.getV()[1] * Utils.step);
            }

            // Check if particles have left the room
            for (Particle p : particles) {
                if (p.hasLeftBox && p.getY() - p.radius <= Utils.targetY
                        && p.getX() >= Utils.secondTargetX1
                        && p.getX() <= Utils.secondTargetX2) {
                    p.setInContact(false);
                    particlesToRemove.add(p);
                }
            }

            // Remove particles that have left the room
            particles.removeAll(particlesToRemove);

            // Update time
            currentTime += Utils.step;
            if (toPrint % 10 == 0) {
                fileWriter.write(particles.size() + "\n");
                fileWriter.write("Comment" + "\n");
                for(Particle p: particles) {
                    fileWriter.write(p.getX() + "\t");
                    fileWriter.write(p.getY() + "\t");
                    fileWriter.write(p.getRadius() + "\t");
                    fileWriter.write("He" + "\n");
                }
            }
            toPrint++;
        }
    }

}
