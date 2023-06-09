package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static final String FILENAME = "animation-1-step.txt";
    public static final String POSITIONS_FILENAME = "positions-1-step.txt";
    public static final String FLOW_RATE_FILENAME = "flow-rate-d" + Utils.targetLength + "-N" + Utils.particleCount + ".txt";
    public final static String RESOURCES_PATH_SYSTEM = "src/main/java/resources/";

    private static double currentTime = 0;
    public static List<Particle> particles = new ArrayList<>();
    public static List<Particle> corners = new ArrayList<>();

    public static List<Particle> targets = new ArrayList<>();

    public static List<Particle> particlesToRemove = new ArrayList<>();

    public static Map<Double, Integer> particlesPerTime = new TreeMap<>();

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
        corners.add(new Particle(0, 0, Utils.minRadius, 0));
        corners.add(new Particle(0, Utils.wallLength, Utils.minRadius, 0));
        corners.add(new Particle(Utils.wallLength, 0, Utils.minRadius, 0));
        corners.add(new Particle(Utils.wallLength, Utils.wallLength, Utils.minRadius, 0));

        targets.add(new Particle(Utils.x_e1, Utils.targetY, Utils.minRadius, 0));
        targets.add(new Particle(Utils.x_e2, Utils.targetY, Utils.minRadius, 0));
        targets.add(new Particle(Utils.secondTargetX1, Utils.secondTargetY, Utils.minRadius, 0));
        targets.add(new Particle(Utils.secondTargetX2, Utils.secondTargetY, Utils.minRadius, 0));
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
        fileWriter.write((particles.size() + corners.size() + targets.size()) + "\n");
        fileWriter.write("Comment" + "\n");
        for(Particle p: particles) {
            fileWriter.write(p.getX() + "\t");
            fileWriter.write(p.getY() + "\t");
            fileWriter.write(p.getRadius() + "\t");
            fileWriter.write("He" + "\n");
        }
        for(Particle p: corners) {
            fileWriter.write(p.getX() + "\t");
            fileWriter.write(p.getY() + "\t");
            fileWriter.write(p.getRadius() + "\t");
            fileWriter.write("He" + "\n");
        }
        for(Particle p: targets) {
            fileWriter.write(p.getX() + "\t");
            fileWriter.write(p.getY() + "\t");
            fileWriter.write(p.getRadius() + "\t");
            fileWriter.write("Na" + "\n");
        }
        simulate(fileWriter);
        fileWriter.close();

        FileWriter flowRateWriter = new FileWriter(FLOW_RATE_FILENAME, true);
        particlesPerTime.forEach((key, value) -> {
            try {
                flowRateWriter.write(key + "\t" + value + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        flowRateWriter.close();

//        for (Map.Entry<Double, Integer> entry : particlesPerTime.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
    }

    private static void simulate(FileWriter fileWriter) throws IOException {
        int toPrint = 0;
        int toRemoveCount = 0;
        particlesPerTime.put(currentTime, particlesToRemove.size());
        while (particles.size() > 0) {
            // Find contacts
            for (Particle p1 : particles) {
                Set<Particle> contacts = new HashSet<>();
                for (Particle p2 : particles) {
                    if (p1.equals(p2) || Utils.getParticleDistance(p1, p2) > p1.getRadius() + p2.getRadius()) {
                        continue;
                    }
                    contacts.add(p2);
                }
                p1.setContacts(contacts);
                p1.calculateWallContacts();
            }

            // Adjust radii
            for (Particle p : particles) {
                if (p.getContacts().size() > 0 || p.getWallsInContact().size() > 0) {
                    p.setRadius(Utils.minRadius);
                } else {
                        p.updateRadius();
                }
            }

            // Update positions and velocities
            for (Particle p : particles) {
                p.calculateVelocity();
                p.calculatePosition();
                // Recalculate target
                if (p.hasLeftBox /*&& p.getY() - p.radius < 0*/)
                    p.calculateTargetOutsideRoom();
                else
                    p.calculateTarget();
            }

            // Check if particles have left the room
            for (Particle p : particles) {
                if (p.getY() - p.radius <= Utils.secondTargetY) {
                    particlesToRemove.add(p);
                }
            }
            if (particlesToRemove.size() != toRemoveCount) {
                toRemoveCount = particlesToRemove.size();
                particlesPerTime.put(currentTime, particlesToRemove.size());
            }
            particlesPerTime.put(currentTime, particlesToRemove.size());
            // Remove particles that have left the room
            particles.removeAll(particlesToRemove);
            // Update time
            currentTime += Utils.step;
            if (toPrint % 10 == 0) {
                fileWriter.write((particles.size() + corners.size() + targets.size()) + "\n");
                fileWriter.write("Comment" + "\n");
                for(Particle p: particles) {
                    fileWriter.write(p.getX() + "\t");
                    fileWriter.write(p.getY() + "\t");
                    fileWriter.write(Utils.minRadius + "\t");
                    fileWriter.write("He" + "\n");
                }
                for(Particle p: corners) {
                    fileWriter.write(p.getX() + "\t");
                    fileWriter.write(p.getY() + "\t");
                    fileWriter.write(p.getRadius() + "\t");
                    fileWriter.write("He" + "\n");
                }
                for(Particle p: targets) {
                    fileWriter.write(p.getX() + "\t");
                    fileWriter.write(p.getY() + "\t");
                    fileWriter.write(p.getRadius() + "\t");
                    fileWriter.write("Na" + "\n");
                }
            }
            toPrint++;

        }
        particlesPerTime.forEach((key, value) -> System.out.println(key + ":" + value));
    }

}
