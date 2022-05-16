package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public Label[] createLabels(ShortestPathData data) {
		Label[] ret = new Label[data.getGraph().size()];
		for (int i=0; i<data.getGraph().size(); i++) {
			ret[i] = new Label(data.getGraph().get(i), Double.POSITIVE_INFINITY);
		}
		return ret;
	}

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
       
        
        BinaryHeap<Label> heapLabels = new BinaryHeap<Label>();
        Label[] labels = createLabels(data);
        //HashMap<Node, Label> nodeLabels = new HashMap<Node, Label>();
        
        labels[data.getOrigin().getId()].setCost(0);
        
        heapLabels.insert(labels[data.getOrigin().getId()]);
        notifyOriginProcessed(data.getOrigin());
        
        while(true) {
        	Label currentLabel = heapLabels.deleteMin(); //Origin at first iteration, afterwards is always least cost
        	currentLabel.setMarked();
        	notifyNodeMarked(currentLabel.getNode());
        	
        	if(data.getDestination() == currentLabel.getNode() ) {
        		break;
        	}
        	
        	for(Arc arc : currentLabel.getNode().getSuccessors()) {
        		if(!data.isAllowed(arc)) {
        			continue;
        		}
        		Label next = labels[arc.getDestination().getId()];
        		if(!next.isMarked()) {
        			double newCost = currentLabel.getCost()+data.getCost(arc);
        			if(next.getCost()>newCost) {
        				if(next.getCost() == Double.POSITIVE_INFINITY) {
        					notifyNodeReached(arc.getDestination());
        				} else {
        					heapLabels.remove(next);
        				}
        				next.setCost(newCost);
        				next.setPrevious(arc);
        				heapLabels.insert(next);
        			}
        		}
        	}
        	//If there are no labels in heap then there was no allowed arc from previous label(node)
        	if (heapLabels.isEmpty()) {
        		return new ShortestPathSolution(data, Status.INFEASIBLE);
        	}
        }
        
        notifyDestinationReached(data.getDestination());
        ArrayList<Arc> arcsInPath = new ArrayList<Arc>();
        Arc arc = labels[data.getDestination().getId()].getPrevious();
        while(arc!=null) {
        	arcsInPath.add(arc);
        	arc = labels[arc.getOrigin().getId()].getPrevious();
        }
        
        Collections.reverse(arcsInPath);
        Path solutionPath=new Path(graph, arcsInPath);
        ShortestPathSolution solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
        
        return solution;
    }

}
