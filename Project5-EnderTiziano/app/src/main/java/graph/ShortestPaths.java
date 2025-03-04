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

        // Priority queue to process nodes by their current shortest distance
        PriorityQueue<PQEntry> queue = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distance));

        // Initialize the origin node with distance 0
        paths.put(origin, new PathData(0.0, null));
        queue.add(new PQEntry(origin, 0.0));

        while (!queue.isEmpty()) {
            // Extract the node with the smallest distance
            PQEntry currentEntry = queue.poll();
            Node currentNode = currentEntry.node;
            double currentDistance = currentEntry.distance;

            // Skip if we've already found a shorter path to this node
            if (paths.get(currentNode).distance < currentDistance) {
                continue;
            }

            // Explore each neighbor
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

    /** Returns the length of the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public double shortestPathLength(Node destination) {
        // TODO 2 - implement this method to fetch the shortest path length
        // from the paths data computed by Dijkstra's algorithm.
        throw new UnsupportedOperationException();
    }

    /** Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public LinkedList<Node> shortestPath(Node destination) {
        // TODO 3 - implement this method to reconstruct sequence of Nodes
        // along the shortest path from the origin to destination using the
        // paths data computed by Dijkstra's algorithm.
        throw new UnsupportedOperationException();
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
    }
    
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


      // TODO 4: create a ShortestPaths object, use it to compute shortest
      // paths data from the origin node given by origCode.

      // TODO 5:
      // If destCode was not given, print each reachable node followed by the
      // length of the shortest path to it from the origin.

      // TODO 6:
      // If destCode was given, print the nodes in the path from
      // origCode to destCode, followed by the total path length
      // If no path exists, print a message saying so.
    }
}


//Testing commits