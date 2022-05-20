package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;

public class AStarAlgorithm extends DijkstraAlgorithm {
	
	Mode mode;

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public LabelStar[] createLabels(ShortestPathData data) {
		LabelStar [] ret = new LabelStar[data.getGraph().size()];
		double maxVelocity = data.getGraph().getGraphInformation().getMaximumSpeed();
		for(int i = 0; i<data.getGraph().size(); i++) {
			ret[i] = new LabelStar(data.getGraph().get(i), Double.POSITIVE_INFINITY);
			if(data.getMode() == Mode.TIME) {
				ret[i].setOiseau((ret[i].getNode().getPoint().distanceTo(data.getDestination().getPoint())*3600/(maxVelocity*1000)));
			} else {
				ret[i].setOiseau(ret[i].getNode().getPoint().distanceTo(data.getDestination().getPoint()));
			}
		}
		return ret;
	}

}
