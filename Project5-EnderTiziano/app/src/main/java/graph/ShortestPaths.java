// Authors: Ender Peyzner, Tiziano Pessi 
// Date: March 12, 2025
// Purpose: Up to TO-DO 4.0. Implemented Shortest Path Length, Shortest Path, and included into Main




package graph;
import java.util.Queue;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileNotFoundException;

/** Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 *   Graph g = // create your graph
 *   ShortestPaths sp = new ShortestPaths();
 *   Node a = g.getNode("A");
 *   sp.compute(a);
 *   Node b = g.getNode("B");
 *   LinkedList<Node> abPath = sp.getShortestPath(b);
 *   double abPathLength = sp.getShortestPathLength(b);
 *   */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node,PathData> paths;

    /** Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * back pointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.*/
    public void compute(Node origin) {
        paths = new HashMap<>();

        // Priority queue orders nodes by current shortest distance from origin
        // for efficient selection of next node to process
        PriorityQueue<PQEntry> queue = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distance));

        // Initialize the origin node with distance 0
        paths.put(origin, new PathData(0.0, null));
        queue.add(new PQEntry(origin, 0.0));

        while (!queue.isEmpty()) {
            // Extract the node with the smallest distance
            PQEntry currentEntry = queue.poll();
            Node currentNode = currentEntry.node;
            double currentDistance = currentEntry.distance;

            // Skip stale entries in queue (we already found a better path)
            if (paths.get(currentNode).distance < currentDistance) {
                continue;
            }

            // For each neighbor, try to find shorter path through current node
            for (Map.Entry<Node, Double> neighborEntry : currentNode.getNeighbors().entrySet()) {
                Node neighbor = neighborEntry.getKey();
                double edgeWeight = neighborEntry.getValue();
                double newDistance = currentDistance + edgeWeight;

                // If this is a shorter path to the neighbor, update the distance and add to the queue
                if (!paths.containsKey(neighbor) || newDistance < paths.get(neighbor).distance) {
                    paths.put(neighbor, new PathData(newDistance, currentNode));
                    queue.add(new PQEntry(neighbor, newDistance));
                }
            }
        }
        
      
    }
    
    @Override
    public String toString() {
    	String output = "";
    	if(paths == null) {
    		return "";
    	}
    	for(Map.Entry<Node, PathData> entry: paths.entrySet()) {
    		output += "Node: " +entry.getKey().toString() + " Previous/Distance: " + entry.getValue().toString() + " || ";
    	}
    	return output;
    	
    }

    /** Returns the length of the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public double shortestPathLength(Node destination) {
        // Return infinity if no path exists to destination
        if (!paths.containsKey(destination)) {
            return Double.POSITIVE_INFINITY;
        }
        return paths.get(destination).distance;
    }

    /** Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public LinkedList<Node> shortestPath(Node destination) {
        // Return null if no path exists to destination
        if (!paths.containsKey(destination)) {
            return null;
        }

        LinkedList<Node> path = new LinkedList<>();
        Node current = destination;

        // Reconstruct path by following previous pointers from destination to origin
        while (current != null) {
            path.addFirst(current);
            PathData data = paths.get(current);
            current = data.previous;
        }

        return path;
    }


    /** Inner class representing data used by Dijkstra's algorithm in the
     * process of computing shortest paths from a given source node. */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /** constructor: initialize distance and previous node */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
        
        @Override
        public String toString() {
        	return this.previous + " " + this.distance;
        }
        
    }
    // Helper class for the compute function. Saves Node and current distance
    private static class PQEntry {
        Node node;
        double distance;

        PQEntry(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }


    /** Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a CSV file with
     * sidewalk data. See GraphParser, BasicParser, and DBParser for more.*/
    protected static Graph parseGraph(String fileType, String fileName) throws
        FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db")) {
            parser = new DBParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }

    public static void main(String[] args) {
        // read command line args
        String fileType = args[0];
        String fileName = args[1];
        String SidewalkOrigCode = args[2];

        String SidewalkDestCode = null;
        if (args.length == 4) {
            SidewalkDestCode = args[3];
        }

        // parse a graph with the given type and filename
        Graph graph;
        try {
            graph = parseGraph(fileType, fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file " + fileName);
            return;
        }
        graph.report();

        // Compute shortest paths from origin node specified in arguments
        ShortestPaths sp = new ShortestPaths();
        Node origin = graph.getNode(SidewalkOrigCode);
        sp.compute(origin);

        if (SidewalkDestCode == null) {
            // TODO 5: Print all reachable nodes and their shortest path lengths
            System.out.println("Shortest paths from " + SidewalkOrigCode + ":");
            for (Node node : graph.getNodes().values()) {
                double pathLength = sp.shortestPathLength(node);
                if (pathLength != Double.POSITIVE_INFINITY) {
                    System.out.println(node.toString() + ": " + pathLength);
                }
            }
        } else {
            // TODO 6: Print path from origin to destination and total length
            Node dest = graph.getNode(SidewalkDestCode);
            LinkedList<Node> path = sp.shortestPath(dest);
            if (path == null) {
                System.out.println("No path exists from " + SidewalkOrigCode + " to " + SidewalkDestCode);
            } else {
                // Print nodes in path
                for (Node node : path) {
                    System.out.print(node.toString() + " ");
                }
                // Print total path length
                System.out.println(sp.shortestPathLength(dest));
            }
        }
    }
}

