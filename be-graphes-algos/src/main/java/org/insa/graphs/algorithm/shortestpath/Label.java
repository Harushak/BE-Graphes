package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
		Node sommetCourant;
		boolean marque;
		double cout;
		Arc pere;
		
		public Label(Node s, double c) {
			this.sommetCourant = s;
			this.marque = false;
			this.cout = c;
			this.pere = null;
		}
		
		public double getCost() {
			return this.cout;
		}
		
		public double getTotalCost() {
			return this.cout;
		}

		@Override
		//public int compareTo(Label o) {
			//return (int) Math.min(this.cout, o.getCost());
		//}
		
		public int compareTo(Label o) {
			if (this.getCost()<o.getCost()) {
				return -1;
			}
			if (this.getCost()>o.getCost()) {
				return 1;
			}
			return 0;
		}
		
		public void setCost(double cost) {
			this.cout = cost;
		}
		
		public Node getNode() {
			return this.sommetCourant;
		}
		
		public void setMarked() {
			this.marque = true;
		}
		
		public boolean isMarked() {
			return this.marque;
		}
		
		public void setPrevious(Arc arc) {
			this.pere = arc;
		}
		
		public Arc getPrevious() {
			return this.pere;
		}
 
	}


