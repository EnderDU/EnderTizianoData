// Authors: Ender Peyzner, Tiziano Pessi 
// Date: March 12, 2025
// Purpose: Made tests to ensure the dijkstra algorithm works correctly


package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;
import java.io.FileNotFoundException;

import java.util.LinkedList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {


    /* Returns the Graph loaded from the file with filename fn. */
    private Graph loadBasicGraph(String fn) {
        Graph result = null;
        try {
          result = ShortestPaths.parseGraph("basic", fn);
        } catch (FileNotFoundException e) {
          fail("Could not find graph " + fn);
        }
        return result;
    }

    /* Helper method for loading DB graphs, similar to loadBasicGraph */
    private Graph loadDBGraph(String fn) {
        Graph result = null;
        try {
            result = ShortestPaths.parseGraph("db", fn);
        } catch (FileNotFoundException e) {
            fail("Could not find graph " + fn);
        }
        return result;
    }

	@Test
	public void test00Nothing() {
		Graph g = new Graph();
		Node a = g.getNode("A");
		Node b = g.getNode("B");
		g.addEdge(a, b, 1);

		// sample assertion statements:
		assertTrue(true);
		assertEquals(2 + 2, 4);
	}

    /** Minimal test case to check the path from A to B in Simple0.txt */
	@Test
	public void test01Simple0() {
		Graph g = loadBasicGraph("Simple0.txt");
		g.report();
		ShortestPaths sp = new ShortestPaths();
		Node a = g.getNode("A");
		sp.compute(a);
		Node b = g.getNode("B");
		LinkedList<Node> abPath = sp.shortestPath(b);
		assertEquals(abPath.size(), 2);
		assertEquals(abPath.getFirst(), a);
		assertEquals(abPath.getLast(), b);
		assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
	}
    
    @Test 
    public void testCompute1() {
    	Graph g = loadBasicGraph("Simple0.txt");
        
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        assertEquals(sp.toString(), "Node: A Previous/Distance: null 0.0 || Node: B Previous/Distance: A 1.0 || Node: C Previous/Distance: A 2.0 || ");
    }
    
    @Test
    public void testBrokenGraph() {
    	Graph g = new Graph();
    	Node a = g.getNode("A");
    	Node b = g.getNode("B");
    	ShortestPaths sp = new ShortestPaths();
    	sp.compute(a);
    	assertEquals(sp.toString(), "Node: A Previous/Distance: null 0.0 || ");
    	sp.compute(b);
    	assertEquals(sp.toString(), "Node: B Previous/Distance: null 0.0 || ");
    		
      
    }
    
    
    @Test
	public void testSelfLoop() {
		Graph g = new Graph();
		Node a = g.getNode("A");
		g.addEdge(a, a, 1.0);

		ShortestPaths sp = new ShortestPaths();
		sp.compute(a);
		assertEquals(sp.toString(), "Node: A Previous/Distance: null 0.0 || ");

	}
    
    @Test
	public void testParallelEdges() {
		Graph g = new Graph();
		Node a = g.getNode("A");
		Node b = g.getNode("B");

		g.addEdge(a, b, 2.0);
		g.addEdge(a, b, 1.0); // Shorter parallel edge

		ShortestPaths sp = new ShortestPaths();
		sp.compute(a);
		assertEquals(sp.toString(), "Node: A Previous/Distance: null 0.0 || Node: B Previous/Distance: A 1.0 || ");
	}
    
    @Test
    public void testLargGraph() {
    	Graph g = loadBasicGraph("Simple1.txt");
    	Node a = g.getNode("A");
    	ShortestPaths sp = new ShortestPaths();
		sp.compute(a);
		assertEquals(sp.toString(), "Node: A Previous/Distance: null 0.0 || Node: B Previous/Distance: A 1.0 || Node: C Previous/Distance: A 2.0 || Node: S Previous/Distance: D 5.0 || Node: D Previous/Distance: C 4.0 || ");
    }
    
    @Test 
    public void testBlank() {
    	Graph g = loadBasicGraph("Simple0.txt");
        
        ShortestPaths sp = new ShortestPaths();
        assertEquals(sp.toString(), "");
    }

    /* Pro tip: unless you include @Test on the line above your method header,
     * gradle test will not run it! This gets me every time. */
    
    @Test
    public void test01EmptyGraph() {
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        assertEquals(0.0, sp.shortestPathLength(a), 1e-6);
        LinkedList<Node> path = sp.shortestPath(a);
        assertEquals(1, path.size());
        assertEquals(a, path.getFirst());
    }

    @Test
    public void test02UnreachableNode() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        // B is unreachable from A
        g.addEdge(b, a, 1.0);
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        assertNull(sp.shortestPath(b));
        assertEquals(Double.POSITIVE_INFINITY, sp.shortestPathLength(b), 1e-6);
    }
    @Test
    public void test03MultiplePathsToDestination() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        // Direct path A->C (weight 5)
        g.addEdge(a, c, 5.0);
        // Path A->B->C (total weight 4)
        g.addEdge(a, b, 1.0);
        g.addEdge(b, c, 3.0);
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        LinkedList<Node> path = sp.shortestPath(c);
        assertEquals(3, path.size());
        assertEquals(a, path.getFirst());
        assertEquals(b, path.get(1));
        assertEquals(c, path.getLast());
        assertEquals(4.0, sp.shortestPathLength(c), 1e-6);
    }
    @Test
    public void test04CyclicGraph() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 1.0);
        g.addEdge(b, c, 2.0);
        g.addEdge(c, a, 3.0);
        g.addEdge(a, c, 7.0); // Longer direct path
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        assertEquals(3.0, sp.shortestPathLength(c), 1e-6);
        LinkedList<Node> path = sp.shortestPath(c);
        assertEquals(3, path.size());
    }
    @Test
    public void test05SelfLoop() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        g.addEdge(a, a, 1.0);
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        assertEquals(0.0, sp.shortestPathLength(a), 1e-6);
        LinkedList<Node> path = sp.shortestPath(a);
        assertEquals(1, path.size());
    }
    @Test
    public void test06LargeGraph() {
        Graph g = new Graph();
        // Create a line graph A->B->C->D->E
        Node[] nodes = new Node[5];
        for (int i = 0; i < 5; i++) {
            nodes[i] = g.getNode(String.valueOf((char)('A' + i)));
        }
        for (int i = 0; i < 4; i++) {
            g.addEdge(nodes[i], nodes[i + 1], 1.0);
        }
        ShortestPaths sp = new ShortestPaths();
        sp.compute(nodes[0]); // Start from A
        // Check path to E
        LinkedList<Node> path = sp.shortestPath(nodes[4]);
        assertEquals(5, path.size());
        assertEquals(4.0, sp.shortestPathLength(nodes[4]), 1e-6);
    }
    @Test
    public void test07ParallelEdges() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 2.0);
        g.addEdge(a, b, 1.0); // Shorter parallel edge
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        assertEquals(1.0, sp.shortestPathLength(b), 1e-6);
    }
    @Test
    public void test08Simple0Verification() {
        Graph g = loadBasicGraph("Simple0.txt");
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        sp.compute(a);
        assertEquals(1.0, sp.shortestPathLength(b), 1e-6);
        assertEquals(2.0, sp.shortestPathLength(c), 1e-6);
        LinkedList<Node> pathToC = sp.shortestPath(c);
        assertEquals(2, pathToC.size());
        assertEquals(a, pathToC.getFirst());
        assertEquals(c, pathToC.getLast());
    }
    @Test
    public void test09UpdateDistance() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 5.0);
        g.addEdge(a, c, 2.0);
        g.addEdge(c, b, 1.0);
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        // Should choose path A->C->B (3.0) over A->B (5.0)
        assertEquals(3.0, sp.shortestPathLength(b), 1e-6);
        LinkedList<Node> path = sp.shortestPath(b);
        assertEquals(3, path.size());
        assertEquals(c, path.get(1));
    }

    @Test
    public void test10Simple2Verification() {
        Graph g = loadBasicGraph("Simple2.txt");
        ShortestPaths sp = new ShortestPaths();
        
        // Test paths from D (appears to be a good starting point)
        Node d = g.getNode("D");
        sp.compute(d);
        
        // Test specific paths we can see in Simple2.txt
        // D->H is direct with weight 1
        assertEquals(1.0, sp.shortestPathLength(g.getNode("H")), 1e-6);
        
        // D->A is direct with weight 4
        assertEquals(4.0, sp.shortestPathLength(g.getNode("A")), 1e-6);
        
        // D->E can go through H (D->H->E = 6) or through A (D->A->E = 5)
        assertEquals(5.0, sp.shortestPathLength(g.getNode("E")), 1e-6);
        
        // Test a longer path: D->A->E->F->B
        LinkedList<Node> pathToB = sp.shortestPath(g.getNode("B"));
        assertEquals(9.0, sp.shortestPathLength(g.getNode("B")), 1e-6);  // 4 + 1 + 3 + 1 = 9
    }

    @Test
    public void test11DBFileBasicVerification() {
        Graph g = loadDBGraph("DBCrop.csv");
        assertNotNull("DBCrop.csv should load successfully", g);
        assertFalse("DB graph should not be empty", g.getNodes().isEmpty());
        
        // Get first node as origin
        Node origin = g.getNodes().values().iterator().next();
        ShortestPaths sp = new ShortestPaths();
        sp.compute(origin);
        
        // Basic verification of accessibility scores
        for (Node dest : g.getNodes().values()) {
            if (dest != origin) {
                double score = sp.shortestPathLength(dest);
                if (score != Double.POSITIVE_INFINITY) {
                    assertTrue("Accessibility score should be between 0 and 1", 
                             score >= 0.0 && score <= 1.0);
                }
            }
        }
    }

    @Test
    public void test12DBFilePathVerification() {
        Graph g = loadDBGraph("DBCrop.csv");
        ShortestPaths sp = new ShortestPaths();
        
        // Get first two nodes to test a specific path
        Node[] nodes = g.getNodes().values().toArray(new Node[0]);
        if (nodes.length >= 2) {
            Node origin = nodes[0];
            Node dest = nodes[1];
            
            sp.compute(origin);
            LinkedList<Node> path = sp.shortestPath(dest);
            
            if (path != null) {
                // Verify path properties
                assertEquals("Path should start at origin", origin, path.getFirst());
                assertEquals("Path should end at destination", dest, path.getLast());
                
                // Verify path continuity (each node should be connected to next)
                for (int i = 0; i < path.size() - 1; i++) {
                    Node current = path.get(i);
                    Node next = path.get(i + 1);
                    assertTrue("Path nodes should be connected",
                             current.getNeighbors().containsKey(next));
                }
            }
        }

    }
}
