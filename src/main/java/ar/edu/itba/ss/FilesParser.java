package ar.edu.itba.ss;

import java.io.*;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FilesParser {
    public final static String RESOURCES_PATH = "src/main/resources/";

    public static void readFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            // TODO: Read file
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    private static void writeLineOutputFile(BufferedWriter writer,
                                            double value)
            throws IOException {
        writer.append(String.valueOf(value))
                .append("\n");
    }

    public static void writeOutputFile(File filePath, double value) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath, true))) {
            writeLineOutputFile(writer, value);
        } catch (Exception e) {
            System.err.println("Error while writing output file");
        }
    }

    public static void deleteFileContent(File fileName) {
        try {
            RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
            raf.setLength(0);
            raf.close();
            System.out.println("File cleaned: " + fileName.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAnimationFile(String fileFullPath, int time,
                                          List<Particle> ballsList,
                                          List<Particle> holesList) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fileFullPath, true))) {
            writeAnimationFileLines(time, writer, ballsList, holesList);
        } catch (Exception e) {
            System.err.println("Error while writing animation file");
        }
    }

    private static void writeCollectionToFileLines(BufferedWriter writer,
                                                   List<Particle> list)
            throws IOException {
        for (Particle ball : list) {
            // TODO: Check correct format for Ovito (moving balls)
            writer.append(String.valueOf(ball.getX()))
                    .append("\t")
                    .append(String.valueOf(ball.getY()))
                    .append("\t")
                    .append(String.valueOf(ball.getVx()))
                    .append("\t")
                    .append(String.valueOf(ball.getVy()))
                    .append("\t")
                    .append(String.valueOf(ball.getRadius()))
                    .append("\t")
                    .append(String.valueOf(ball.getMass()))
                    .append("\t")
                    .append(String.valueOf(ball.getSymbol()))
                    .append("\n");
        }
    }

    private static void writeAnimationFileLines(int time, BufferedWriter writer,
                                                List<Particle> ballsList,
                                                List<Particle> holesList)
            throws IOException {
        int totalParticles = ballsList.size() + holesList.size();
        writer.append(String.valueOf(totalParticles))
                .append("\n")
                .append("Generation: ")
                .append(String.valueOf(time))
                .append("\n");
        writeCollectionToFileLines(writer, holesList);
        writeCollectionToFileLines(writer, ballsList);
    }

    public static void writePositionsFile(String filename, Map<Long, List<double[]>> ballsPositions) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filename, true))) {
            for (Map.Entry<Long, List<double[]>> entry : ballsPositions.entrySet()) {
//                writer.append(String.valueOf(entry.getKey()))
//                        .append("\t");
                for (double[] position : entry.getValue()) {
                    writer.append(String.valueOf(position[0]))
                            .append("\t")
                            .append(String.valueOf(position[1]))
                            .append("\t");
                }
                writer.append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error while writing animation file");
        }
    }

    public static void writeTimeFile(double time, String fileName) throws IOException {
        File timeFile = new File(fileName);
        FileWriter fileWriter = new FileWriter(timeFile, true);
        fileWriter.write(time + "\n");
        fileWriter.close();
    }

    public static void writeInitialPositions(File filePath, List<Particle> balls) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath, true))) {
            for (Particle ball : balls) {
                writer.append(String.valueOf(ball.getId()))
                        .append(": ")
                        .append(String.valueOf(ball.getX()))
                        .append("\t")
                        .append(String.valueOf(ball.getY()))
                        .append("\n");
            }
            writer.append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Funcion para crear un archivo en el directorio especificado
    public static File creatingFile(String fileName, String directoryPath) {
        System.out.println("Creating file: " + fileName);
        // check if directory exists
        File directoryFile = new File(directoryPath);
        return new File(directoryFile + File.separator + fileName);
    }
}
