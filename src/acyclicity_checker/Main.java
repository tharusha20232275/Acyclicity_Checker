/**
 * Student Name: T.D. Kodithuwakku
 * Student ID: IIT Id: 20232275 / UOW Id: w2084406
 */
package acyclicity_checker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class:
 * - finds the input file
 * - reads the graph
 * - runs sink elimination
 * - runs DFS cycle detection if needed
 * - prints a final summery
 */
public class Main {
     //Tries to locate the file.
     
    public static File resolveInputFile(String fileName) {
        File[] candidates = new File[] {
            new File(fileName),
            new File("benchmarks", fileName),
            new File("benchmarks" + File.separator + "acyclic", fileName),
            new File("benchmarks" + File.separator + "cyclic", fileName),
            new File("acyclic", fileName),
            new File("cyclic", fileName)
        };

        for (File f : candidates) {
            if (f.exists() && f.isFile()) {
                return f;
            }
        }

        return null;
    }

    /**
     * Reads graph from file.
     * Format:
     * first line = number of vertices
     * remaining lines = edge pairs (from-to)
     */
    public static DirectedGraph readGraphFromFile(String fileName) throws FileNotFoundException {
        File inputFile = resolveInputFile(fileName);

        if (inputFile == null) {
            throw new FileNotFoundException(
                "File not found.\n" +
                "Tried:\n" +
                "- " + fileName + "\n" +
                "- benchmarks/" + fileName + "\n" +
                "- benchmarks/acyclic/" + fileName + "\n" +
                "- benchmarks/cyclic/" + fileName + "\n" +
                "- acyclic/" + fileName + "\n" +
                "- cyclic/" + fileName + "\n"
            );
        }

        Scanner scanner = new Scanner(inputFile);

        int numberOfVertices = scanner.nextInt();
        DirectedGraph graph = new DirectedGraph(numberOfVertices);

        while (scanner.hasNextInt()) {
            int from = scanner.nextInt();
            if (!scanner.hasNextInt()) {
                break;
            }
            int to = scanner.nextInt();
            graph.addEdge(from, to);
        }

        scanner.close();
        return graph;
    }

    private static void printRecap(boolean acyclic, ArrayList<Integer> removedSinks,
                                   ArrayList<Integer> cycle, double elapsed) {
        System.out.println("=== Process Summery ===");

        System.out.println("Graph status: " + (acyclic ? "ACYCLIC \n 'YES' " : "CYCLIC \n 'No' "));

        System.out.println("Sinks removed (" + removedSinks.size() + "): ");
        if (removedSinks.isEmpty()) {
            System.out.println("  none");
        } else {
            System.out.println("  " + removedSinks);
        }

        if (acyclic) {
            System.out.println("Cycle found: none");
        } else {
            System.out.println("Cycle found:");
            if (cycle.isEmpty()) {
                System.out.println("  could not reconstruct cycle");
            } else {
                System.out.println("  " + AcyclicityChecker.formatCycle(cycle));
            }
        }

        System.out.printf("Execution time: %.3f seconds%n", elapsed);
        System.out.println("================");
    }

    public static void main(String[] args) {
        String fileName;
        if (args.length > 0) {
            fileName = args[0];
        } else {
            fileName = "cyclic_test1.txt";
        }

        boolean verbose = true;
        if (args.length > 1) {
            verbose = Boolean.parseBoolean(args[1]);
        }

        try {
            DirectedGraph graph = readGraphFromFile(fileName);

            System.out.println("Input file: " + fileName);
            System.out.println("Vertices: " + graph.getNumberOfVertices());
            System.out.println();

            long start = System.currentTimeMillis();

            ArrayList<Integer> removedSinks = new ArrayList<Integer>();
            boolean acyclic = AcyclicityChecker.eliminateSinks(graph, verbose, removedSinks);

            ArrayList<Integer> cycle = new ArrayList<Integer>();
            if (!acyclic) {
                cycle = AcyclicityChecker.findCycle(graph, verbose);
            }

            long end = System.currentTimeMillis();
            double elapsed = (end - start) / 1000.0;

            System.out.println();
            printRecap(acyclic, removedSinks, cycle, elapsed);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}