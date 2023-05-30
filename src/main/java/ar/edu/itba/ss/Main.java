package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static final String FILENAME = "animation-1-step.txt";
    public static final String POSITIONS_FILENAME = "positions-1-step.txt";
    public final static String RESOURCES_PATH_SYSTEM = "src/main/java/resources/";

    public static List<Particle> particles = new ArrayList<>();
    public static Map<Long, List<Particle>> contacts = new HashMap<>();

    public static void initializeRoom() {
        for(int i = 0; i < Utils.particleCount;) {
            double x = (Math.random() * (20 - Utils.maxRadius)) + Utils.maxRadius;
            double y = (Math.random() * (20 - Utils.maxRadius)) + Utils.maxRadius;
            Particle p = new Particle(x, y, Utils.minRadius, 0);
            if(!isOverlapping(p)) {
                particles.add(p);
                i++;
            }
        }
    }

    public static boolean isOverlapping(Particle p1) {
        for(Particle p2: particles) {
            if(Math.abs(Utils.getDistance(p1, p2)) < 2 * Utils.minRadius)
                return true;
        }
        return false;
    }


    public static void main(String[] args) throws IOException {
        initializeRoom();
        FileWriter fileWriter = new FileWriter(FILENAME, true);
        fileWriter.write(Utils.particleCount + "\n");
        for(Particle p: particles) {
            fileWriter.write(p.getX() + "\t");
            fileWriter.write(p.getY() + "\t");
            fileWriter.write(p.getRadius() + "\t");
            fileWriter.write("He" + "\n");
        }
        fileWriter.close();

//        while (particles.size() > 0) {
//
//        }
    }

    private static void findContacts(Particle particle) {
        for (Particle other : particles) {
            if (Utils.getDistance(particle, other) < Utils.minRadius + Utils.maxRadius)
                contacts.get(particle.id).add(other);
        }
    }

}
