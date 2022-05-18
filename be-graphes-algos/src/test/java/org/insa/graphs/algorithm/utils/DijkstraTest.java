package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.ArcInspectorFactory;
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
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;

public class DijkstraTest {
	private static Graph graph;
	
	private static Node[] nodes = new Node[9];
	
	@SuppressWarnings("unused")
	private static Arc aB, aD, aF, aG, bA, bC, bG, bI, cA, cB, dA, dC, dE, eA, fA, fG, gF, gH;
	
	private static ShortestPathData shortest, fastest, bikes, inExistant, empty;
	
	@BeforeClass
	
	public static void initAll() throws IOException {
		//Create RoadInformation and their restrictions
		EnumMap<AccessMode, AccessRestriction> carsOnly = new EnumMap<>(AccessMode.class);
		EnumMap<AccessMode, AccessRestriction> carsAndBikes = new EnumMap<>(AccessMode.class);
		EnumMap<AccessMode, AccessRestriction> bikesOnly = new EnumMap<>(AccessMode.class);
		
		for (AccessMode mode : AccessRestrictions.AccessMode.ALL) {
			carsOnly.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);
			carsAndBikes.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);
			bikesOnly.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);
		}
		carsOnly.put(AccessRestrictions.AccessMode.MOTORCAR, AccessRestrictions.AccessRestriction.ALLOWED);
		carsAndBikes.put(AccessRestrictions.AccessMode.MOTORCAR, AccessRestrictions.AccessRestriction.ALLOWED);
		carsAndBikes.put(AccessRestrictions.AccessMode.BICYCLE, AccessRestrictions.AccessRestriction.ALLOWED);
		bikesOnly.put(AccessRestrictions.AccessMode.BICYCLE, AccessRestrictions.AccessRestriction.ALLOWED);
		
		RoadInformation highWay = new RoadInformation(RoadType.MOTORWAY,new AccessRestrictions(carsOnly), true, 130, "");
		RoadInformation cityRoad = new RoadInformation(RoadType.MOTORWAY,new AccessRestrictions(carsAndBikes), true, 70, "");
		RoadInformation bikeRoad = new RoadInformation(RoadType.MOTORWAY,new AccessRestrictions(bikesOnly), true, 30, "");
		
		//Create Nodes
		for (int i = 0; i < nodes.length; ++i) {
			nodes[i] = new Node(i, null);
		}
		
		//Create Arcs
		aB = Node.linkNodes(nodes[0], nodes[1], 20, cityRoad, null);
		aF = Node.linkNodes(nodes[0], nodes[5], 35, highWay, null);
		aG = Node.linkNodes(nodes[0], nodes[6], 45, cityRoad, null);
		bA = Node.linkNodes(nodes[1], nodes[0], 20, cityRoad, null);
		bG = Node.linkNodes(nodes[1], nodes[6], 60, highWay, null);
		bI = Node.linkNodes(nodes[1], nodes[8], 15, bikeRoad, null);
		cA = Node.linkNodes(nodes[2], nodes[0], 40, highWay, null);
		cB = Node.linkNodes(nodes[2], nodes[1], 35, cityRoad, null);
		dA = Node.linkNodes(nodes[3], nodes[0], 65, cityRoad, null);
		dC = Node.linkNodes(nodes[3], nodes[2], 30, highWay, null);
		eA = Node.linkNodes(nodes[4], nodes[0], 35, cityRoad, null);
		fA = Node.linkNodes(nodes[5], nodes[0], 35, highWay, null);
		fG = Node.linkNodes(nodes[5], nodes[6], 20, cityRoad, null);
		gH = Node.linkNodes(nodes[6], nodes[7], 40, highWay, null);
		
		//create graph
		graph = new Graph("ID", "", Arrays.asList(nodes), null);
		
		//Create data for the algorithms to use
		shortest = new ShortestPathData(graph, nodes[3], nodes[7], ArcInspectorFactory.getAllFilters().get(0));
		fastest = new ShortestPathData(graph, nodes[3], nodes[7], ArcInspectorFactory.getAllFilters().get(2));
		bikes = new ShortestPathData(graph, nodes[0], nodes[8], ArcInspectorFactory.getAllFilters().get(3));
		inExistant = new ShortestPathData(graph, nodes[5], nodes[4], ArcInspectorFactory.getAllFilters().get(2));
		empty = new ShortestPathData(graph, nodes[0], nodes[0], ArcInspectorFactory.getAllFilters().get(2));
	}
	
	DijkstraAlgorithm dijkstra;
	Path expected, calculated;
	
	@Test
	public void testShortestPath() {
		dijkstra = new DijkstraAlgorithm(shortest);
		expected = Path.createShortestPathFromNodes(graph, 
				Arrays.asList(new Node[] { nodes[3], nodes[0], nodes[6], nodes[7]} ));
		calculated = dijkstra.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getLength() == expected.getLength());
		//assertEquals(expected, calculated); Does not work!
	}
	@Test	
	public void testFastestPath() {
		dijkstra = new DijkstraAlgorithm(fastest);
		expected = Path.createFastestPathFromNodes(graph, 
				Arrays.asList(new Node[] { nodes[3], nodes[2], nodes[0], nodes[5], nodes[6], nodes[7]} ));
		calculated = dijkstra.run().getPath();
		assertTrue(calculated.isValid());
		assertTrue(calculated.size() == expected.size());
		assertTrue(calculated.getMinimumTravelTime() == expected.getMinimumTravelTime());
		//assertEquals(expected, calculated);
	}
	@Test
	public void testImpossiblePath() {
		//Should not work since we have to pass a road only allowed for bikes while driving a car
		dijkstra = new DijkstraAlgorithm(bikes);
		calculated = dijkstra.run().getPath();
		expected = null;
		assertTrue(calculated == expected);
		//assertEquals(expected, calculated);
	}
	@Test
	public void testInexistantPath() {
		//Should not work since there is no arc with e (nodes[4] as destination
		dijkstra = new DijkstraAlgorithm(inExistant);
		calculated = dijkstra.run().getPath();
		expected = null;
		assertTrue(calculated == expected);
		//assertEquals(expected, calculated);
	}
	@Test
	public void testEmptyPath() {
		//Should not work since we have to pass a road only allowed for bikes while driving a car
		dijkstra = new DijkstraAlgorithm(bikes);
		calculated = dijkstra.run().getPath();
		expected = null;
		assertTrue(calculated == expected);
		//assertEquals(expected, calculated);
	}
	
	
	
}


