package ar.edu.itba.ss;

import java.util.*;

public class Main {
    private static final Random random = new Random();
    public static final List<Particle> particles = new ArrayList<>();
    public static final Map<Long, List<double[]>> particlePositions = new HashMap<>();
    public static final double k = 5;
    public static final double INTEGRATION_STEP = Math.pow(10, -k);
    public static final String FILENAME = "animation-1-step" + k + ".txt";
    public static final String POSITIONS_FILENAME = "positions-1-step" + k + ".txt";
    public final static String RESOURCES_PATH_SISTEM = "src/main/java/resources/";
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
