package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {
	protected double oiseau;
	
	public LabelStar(Node node, double cost) {
		super(node, cost);
	}
	
	@Override
	public double getTotalCost() {
		if(this.cout == Double.POSITIVE_INFINITY) {
			return this.cout;
		}
		return this.oiseau+this.cout;
	}
	
	@Override
	public int compareTo(Label o) {
    //	System.out.print("HERE");
		if (Double.compare(this.getTotalCost(), o.getTotalCost()) == 0) {
			return Double.compare(this.oiseau,((LabelStar) o).oiseau);
		}
		return Double.compare(this.getTotalCost(), o.getTotalCost());
	}
	
	public void setOiseau(double a) {
		this.oiseau = a;
	}

}
