
package acyclicity_checker;

/**
 *
 * @author THARUSHA
 */
import java.util.ArrayList;
import java.util.List;


 // A simple directed graph using adjacency lists.
 
public class DirectedGraph {

    private final int numberOfVertices;
    private final ArrayList<ArrayList<Integer>> adjacencyList;
    private final boolean[] active;

    public DirectedGraph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        this.adjacencyList = new ArrayList<ArrayList<Integer>>();
        this.active = new boolean[numberOfVertices];

        for (int i = 0; i < numberOfVertices; i++) {
            adjacencyList.add(new ArrayList<Integer>());
            active[i] = true;
        }
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void addEdge(int from, int to) {
        adjacencyList.get(from).add(to);
    }

    public List<Integer> getNeighbours(int vertex) {
        return adjacencyList.get(vertex);
    }

    public boolean isActive(int vertex) {
        return active[vertex];
    }

    public void removeVertex(int vertex) {
        active[vertex] = false;
    }

    public boolean hasActiveVertices() {
        for (int i = 0; i < active.length; i++) {
            if (active[i]) {
                return true;
            }
        }
        return false;
    }

    
     // Counts outgoing edges from vertex to still-active vertices only.
     
    public int countActiveOutgoingEdges(int vertex) {
        if (!active[vertex]) {
            return 0;
        }

        int count = 0;
        for (int neighbour : adjacencyList.get(vertex)) {
            if (active[neighbour]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Finds one sink among the active vertices.
     * A sink is a vertex with no outgoing edges to active vertices.
     * 
     * Returns:
     * - vertex number if found
     * - -1 if no sink exists
     */
    public int findSink() {
        for (int vertex = 0; vertex < numberOfVertices; vertex++) {
            if (active[vertex] && countActiveOutgoingEdges(vertex) == 0) {
                return vertex;
            }
        }
        return -1;
    }

    
     // Convenience method for debug output.
     
    public String activeVerticesAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;

        for (int i = 0; i < numberOfVertices; i++) {
            if (active[i]) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(i);
                first = false;
            }
        }

        sb.append("]");
        return sb.toString();
    }
}