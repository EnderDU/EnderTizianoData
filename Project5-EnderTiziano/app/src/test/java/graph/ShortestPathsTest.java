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
//    @Test
//    public void test00Nothing() {
//        Graph g = new Graph();
//        Node a = g.getNode("A");
//        Node b = g.getNode("B");
//        g.addEdge(a, b, 1);
//
//        // sample assertion statements:
//        assertTrue(true);
//        assertEquals(2+2, 4);
//    }

    /** Minimal test case to check the path from A to B in Simple0.txt */
//    @Test
//    public void test01Simple0() {
//        Graph g = loadBasicGraph("Simple0.txt");
//        g.report();
//        ShortestPaths sp = new ShortestPaths();
//        Node a = g.getNode("A");
//        sp.compute(a);
//        Node b = g.getNode("B");
//        LinkedList<Node> abPath = sp.shortestPath(b);
//        assertEquals(abPath.size(), 2);
//        assertEquals(abPath.getFirst(), a);
//        assertEquals(abPath.getLast(),  b);
//        assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
//    }
    
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
}
