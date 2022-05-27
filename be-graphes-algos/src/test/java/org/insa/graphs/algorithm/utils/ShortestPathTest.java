package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.AccessRestrictions;
import org.insa.graphs.model.AccessRestrictions.AccessMode;
import org.insa.graphs.model.AccessRestrictions.AccessRestriction;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;

/**
 * 
 * @author harus_udy5
 * Avec des paths prefaits avec l'algorithm de Bellman-Ford sur la carte de Cuba, on fait des tests.
 * 
 * Les algorithms Dijkstra et AStar sont lancés et leur resultats sont comparés avec celles de Bellman-Ford.
 *
 */

public class ShortestPathTest {
	final static String map = "C:\\Users\\harus_udy5\\BE-Graphes\\cuba.mapgr";
    final static String shortestAllRoad = "C:\\Users\\harus_udy5\\BE-Graphes\\shortestPathAllRoads.path";
    final static String shortestOnlyCar = "C:\\Users\\harus_udy5\\BE-Graphes\\shortestPathOnlyRoads.path";
    final static String fastestAllRoad = "C:\\Users\\harus_udy5\\BE-Graphes\\fastestPathAllRoads.path";
    final static String fastestOnlyCar = "C:\\Users\\harus_udy5\\BE-Graphes\\fastestPathOnlyCars.path";
    final static String fastestPedestrian = "C:\\Users\\harus_udy5\\BE-Graphes\\fastestPedestrianPath.path";
    
	static Graph graph;
	
	static	Path shortestAllRoadPath;
	
	static Path shortestCarPath;
	
	static Path fastestAllRoadPath;
	
	static Path fastestCarPath;
	
	static Path fastestPedestrianPath;
	
	private static ShortestPathData shortestAllRoads, shortestOnlyCars, fastestAllRoads, fastestOnlyCars, fastestPedestrians, inExistant, empty;
	
	@BeforeClass
	
	public static void initAll() throws IOException {
		
        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(map))));
        graph = reader.read();
        
        
        PathReader pathReader = new BinaryPathReader(
        		new DataInputStream(new BufferedInputStream(new FileInputStream(shortestAllRoad))));
        shortestAllRoadPath = pathReader.readPath(graph);
        
        pathReader = new BinaryPathReader(
        		new DataInputStream(new BufferedInputStream(new FileInputStream(shortestOnlyCar))));
        shortestCarPath = pathReader.readPath(graph);
        
        pathReader = new BinaryPathReader(
        		new DataInputStream(new BufferedInputStream(new FileInputStream(fastestAllRoad))));
        fastestAllRoadPath = pathReader.readPath(graph);
        
        pathReader = new BinaryPathReader(
        		new DataInputStream(new BufferedInputStream(new FileInputStream(fastestOnlyCar))));
        fastestCarPath = pathReader.readPath(graph);
        
        pathReader = new BinaryPathReader(
        		new DataInputStream(new BufferedInputStream(new FileInputStream(fastestPedestrian))));
        fastestPedestrianPath = pathReader.readPath(graph);
        
        shortestAllRoads = new ShortestPathData(graph, shortestAllRoadPath.getOrigin(), shortestAllRoadPath.getDestination(), ArcInspectorFactory.getAllFilters().get(0));
        shortestOnlyCars = new ShortestPathData(graph, shortestCarPath.getOrigin(), shortestCarPath.getDestination(), ArcInspectorFactory.getAllFilters().get(1));
        fastestAllRoads = new ShortestPathData(graph, fastestAllRoadPath.getOrigin(), fastestAllRoadPath.getDestination(), ArcInspectorFactory.getAllFilters().get(2));
        fastestOnlyCars = new ShortestPathData(graph, fastestAllRoadPath.getOrigin(), fastestAllRoadPath.getDestination(), ArcInspectorFactory.getAllFilters().get(3));
        fastestPedestrians = new ShortestPathData(graph, fastestPedestrianPath.getOrigin(), fastestPedestrianPath.getDestination(), ArcInspectorFactory.getAllFilters().get(4));
        empty = new ShortestPathData(graph, shortestAllRoadPath.getOrigin(), shortestAllRoadPath.getOrigin(), ArcInspectorFactory.getAllFilters().get(0));
        inExistant = new ShortestPathData(graph, graph.get(1), graph.get(65802), ArcInspectorFactory.getAllFilters().get(0));
        
	}
	
	DijkstraAlgorithm dijkstra;
	AStarAlgorithm aStar;
	Path expected, calculated;
	
	@Test
	public void testShortestAllRoadsPath() {
		dijkstra = new DijkstraAlgorithm(shortestAllRoads);
		aStar = new AStarAlgorithm(shortestAllRoads);
		expected = shortestAllRoadPath;
		calculated = dijkstra.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getLength() == expected.getLength());
		calculated = aStar.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getLength() == expected.getLength());
		//assertEquals(expected, calculated); Does not work!
	}
	@Test
	public void testShortestOnlyCarsPath() {
		dijkstra = new DijkstraAlgorithm(shortestOnlyCars);
		aStar = new AStarAlgorithm(shortestOnlyCars);
		expected = shortestCarPath;
		calculated = dijkstra.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getLength() == expected.getLength());
		calculated = aStar.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getLength() == expected.getLength());
		//assertEquals(expected, calculated); Does not work!
	}
	@Test	
	public void testFastestAllRoadsPath() {
		dijkstra = new DijkstraAlgorithm(fastestAllRoads);
		aStar = new AStarAlgorithm(fastestAllRoads);
		expected = fastestAllRoadPath;
		calculated = dijkstra.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getMinimumTravelTime() == expected.getMinimumTravelTime());
		calculated = aStar.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getMinimumTravelTime() == expected.getMinimumTravelTime());
		//assertEquals(expected, calculated);
	}
	@Test	
	public void testFastestOnlyCarsPath() {
		dijkstra = new DijkstraAlgorithm(fastestOnlyCars);
		aStar = new AStarAlgorithm(fastestOnlyCars);
		expected = fastestCarPath;
		calculated = dijkstra.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getMinimumTravelTime() == expected.getMinimumTravelTime());
		calculated = aStar.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getMinimumTravelTime() == expected.getMinimumTravelTime());
		//assertEquals(expected, calculated);
	}
	@Test
	public void testFastestPedestrianPath() {
		dijkstra = new DijkstraAlgorithm(fastestPedestrians);
		aStar = new AStarAlgorithm(fastestPedestrians);
		expected = fastestPedestrianPath;
		calculated = dijkstra.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getMinimumTravelTime() == expected.getMinimumTravelTime());
		calculated = aStar.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getMinimumTravelTime() == expected.getMinimumTravelTime());
	}
	@Test
	public void testEmptyPath() {
		//Should not work since we have to pass a road only allowed for bikes while driving a car
		dijkstra = new DijkstraAlgorithm(empty);
		aStar = new AStarAlgorithm(empty);
		calculated = dijkstra.run().getPath();
		expected = null;
		assertTrue(calculated == expected);
		calculated = aStar.run().getPath();
		assertTrue(calculated == expected);
		//assertEquals(expected, calculated);
	}
	@Test
	public void testInexistantPath() {
		//Should not work since there is no arc with e (nodes[4] as destination
		dijkstra = new DijkstraAlgorithm(inExistant);
		aStar = new AStarAlgorithm(inExistant);
		calculated = dijkstra.run().getPath();
		expected = null;
		assertTrue(calculated == expected);
		calculated = aStar.run().getPath();
		assertTrue(calculated == expected);
		//assertEquals(expected, calculated);
	}
	
	
	
	
}


