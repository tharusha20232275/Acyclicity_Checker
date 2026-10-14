
package acyclicity_checker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this Contains the two main algorithms:
 * 1) sink elimination for checking acyclicity
 * 2) DFS cycle detection for outputting a cycle
 */
public class AcyclicityChecker {

    /**
     * Runs the sink elimination algorithm.
     *
     * removedSinks stores the order in which sinks were removed.
     *
     * Returns true if the graph becomes empty.
     * Returns false if program get stuck with vertices remaining but no sink.
     */
    public static boolean eliminateSinks(DirectedGraph graph, boolean verbose, ArrayList<Integer> removedSinks) {
        if (verbose) {
            System.out.println("=== PROCESS: Sink elimination ===");
            System.out.println("Active vertices initially: " + graph.activeVerticesAsString());
            System.out.println();
        }

        while (true) {
            int sink = graph.findSink();

            if (sink == -1) {
                if (verbose) {
                    System.out.println("No sink found among the remaining active vertices.");
                    System.out.println("Therfore the graph cannot be reduced any further.");
                    System.out.println("Remaining active vertices: " + graph.activeVerticesAsString());
                    System.out.println();
                }
                break;
            }

            removedSinks.add(sink);

            if (verbose) {
                System.out.println("Found sink: " + sink);
                System.out.println("Removing sink: " + sink);
                System.out.println();
            }

            graph.removeVertex(sink);

            if (verbose) {
                System.out.println("Active vertices now: " + graph.activeVerticesAsString());
                System.out.println();
            }
        }

        return !graph.hasActiveVertices();
    }

    /**
     * Finds and returns one cycle in the remaining active graph.
     *
     * Uses DFS with:
     * - 0 = unvisited
     * - 1 = visiting
     * - 2 = finished
     */
    public static ArrayList<Integer> findCycle(DirectedGraph graph, boolean verbose) {
        int n = graph.getNumberOfVertices();
        int[] state = new int[n];
        int[] parent = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }

        ArrayList<Integer> cycle = new ArrayList<Integer>();

        if (verbose) {
            System.out.println("=== PROCESS: DFS cycle search ===");
        }

        for (int v = 0; v < n; v++) {
            if (graph.isActive(v) && state[v] == 0) {
                if (dfsFindCycle(graph, v, state, parent, cycle, verbose)) {
                    return cycle;
                }
            }
        }

        return cycle;
    }

    private static boolean dfsFindCycle(DirectedGraph graph, int u, int[] state, int[] parent,
                                        ArrayList<Integer> cycle, boolean verbose) {
        state[u] = 1; // visiting

        if (verbose) {
            System.out.println("Visiting vertex " + u);
        }

        for (int v : graph.getNeighbours(u)) {
            if (!graph.isActive(v)) {
                continue;
            }

            if (state[v] == 0) {
                parent[v] = u;
                if (verbose) {
                    System.out.println("  Tree edge: " + u + " -> " + v);
                }

                if (dfsFindCycle(graph, v, state, parent, cycle, verbose)) {
                    return true;
                }
            } else if (state[v] == 1) {
                if (verbose) {
                    System.out.println("  Back edge found: " + u + " -> " + v);
                    System.out.println("  This is a cycle.");
                }

                buildCycle(u, v, parent, cycle);
                return true;
            } else {
                if (verbose) {
                    System.out.println("  Ignoring edge to finished vertex: " + u + " -> " + v);
                }
            }
        }

        state[u] = 2; // finished

        if (verbose) {
            System.out.println("Finished vertex " + u);
        }

        return false;
    }

    /**
     * Builds a cycle from a back edge u -> v, where v is an ancestor of u.
     * The cycle becomes something like:
     * v -> ... -> u -> v
     */
    private static void buildCycle(int from, int to, int[] parent, ArrayList<Integer> cycle) {
        ArrayList<Integer> path = new ArrayList<Integer>();

        path.add(from);
        int current = parent[from];

        while (current != to && current != -1) {
            path.add(current);
            current = parent[current];
        }

        path.add(to);

        Collections.reverse(path);

        // Close the cycle
        path.add(to);

        cycle.addAll(path);
    }

    
     //printing for a cycle.
     
    public static String formatCycle(List<Integer> cycle) {
        if (cycle.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cycle.size(); i++) {
            if (i > 0) {
                sb.append(" -> ");
            }
            sb.append(cycle.get(i));
        }
        return sb.toString();
    }
}