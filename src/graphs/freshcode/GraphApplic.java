package graphs.freshcode;

import java.io.IOException;

import java.util.*;

import java.util.*; 

public class GraphApplic extends Graph {

	public GraphApplic(long s) {
		super(s);
	}

	// PASS LEVEL
	
	public Integer surfNoJump(Integer v, Integer n) {
		// PRE: v is vertex to start surf; n >= 0
		// POST: surfs the graph randomly for n moves, 
		//       choosing adjacent vertex according to distribution of edge weights
		//       modifies # visits in vertices
		//       returns last visited vertex at end of surfing

		// TODO
		if(n==0) {
			return v;
		}
		getVertex(v).setMarked(); 
		getVertex(v).visits++;
		Integer adjList = getVertex(v).getPseudoRandomLink();
		return surfNoJump(adjList,n-1);
		
	}
	
	public Integer surfWithJump(Integer v, Integer n, Double jumpThreshold) {
		// PRE: v is vertex to start surf; n >= 0; 0 <= jumpThreshold <= 1.0
		// POST: surfs the graph randomly for n moves, 
		//       choosing adjacent vertex according to distribution of edge weights if random number is below jumpThreshold, 
		//       choosing any vertex uniformly randomly otherwise;
		//       modifies # visits in vertices
		//       returns last visited vertex at end of surfing

		// TODO
		
//		for(int i = 0 ; i < n; i++) {
//			Double random = getVertex(getFirstVertexID()).getPseudoRandomDouble();
//			Double randvertex = getVertex(v).getPseudoRandomDouble() * numVertices();
//			if(random<jumpThreshold) {
//				v = getVertex(v).getPseudoRandomLink();
//				getVertex(v).visits++;
//			}else {
//				
//				v=randvertex.intValue();
//				getVertex(v).visits++;
//			}
//			
//		}
		while(n!=0) {
			Double random = getVertex(getFirstVertexID()).getPseudoRandomDouble();
			Double randvertex = getVertex(v).getPseudoRandomDouble() * numVertices();
			getVertex(v).visits++;
			if(random<jumpThreshold) {
				v = getVertex(v).getPseudoRandomLink();
				
			}else {
				
				v=randvertex.intValue();
				
			}
			n--;
		}
//		System.out.println(getVertex(v).visits);
		return v;
		
	}
	
	public ArrayList<Integer> topN(Integer N) {
		// PRE: none
		// POST: returns N vertices with highest number of visits, in order;
		//       returns all vertices if <N in the graph;
		//       returns vertices ranked 1,..,N,N+1,..,N+k if these k have the
		//         same number of visits as vertex N
		
		// TODO
		
		ArrayList<Vertex> list=new ArrayList<Vertex>();
		for (int i=0; i < numVertices(); i++) {
			list.add(getVertex(i));
		}
		
		list.sort(new Comparator<Vertex>() {
			public int compare(Vertex o1, Vertex o2) {
				// TODO Auto-generated method stub
				if(o1.visits< o2.visits) {
					return 1;
				}
				if(o1.visits> o2.visits){
					return -1;
				}
				else return 0;
			}
				
		});
		
		ArrayList<Integer> topNum = new ArrayList<Integer>();
		
		for ( int i = 0 ; i< N ;i++) {
			topNum.add(list.get(i).getID());
		}
		return topNum;
		
	}
	
	// CREDIT LEVEL
	
	public void setVertexWeights () {
		// PRE: -
		// POST: set weights of each vertex v to be v.visits divided by
		//         the total of visits for all vertices

		// TODO
		
		Double totalvisit = 0.0;
		
		for(int i = 0 ; i< numVertices();i++) {
			totalvisit+=getVertex(i).visits*1.0;
		}
	
		for(int i = 0 ; i< numVertices();i++) {
			getVertex(i).weights= (getVertex(i).visits/totalvisit) ;
		}
	
		
	}
	
	public void convergenceWeights(Integer dp, Double jumpThreshold) {
		// PRE: dp >= 0 representing number of decimal places
		//		0 <= jumpThreshold <= 1.0
		// POST: web is surfed until all weights are constant to dp decimal places,
		//         for at least one iteration
		
		// TODO
		Integer size = this.numVertices();
		ArrayList<Double> pweights = new ArrayList<Double>();
		Integer wconverge = 0 ;
		Integer current= surfWithJump(this.getFirstVertexID(), 1, jumpThreshold);
		for (Integer j=0 ;j < size; j++) {
			pweights.add(j,this.getVertex(j).getWeight());
		}
		Double cweight =0.0; 
		Double pweight =0.0;
		
		while(wconverge==0){
			
			setVertexWeights();
			wconverge = 1;
			
			for( Integer i = 0 ; i < size; i++) {
				
				cweight= this.getVertex(i).getWeight() * Math.pow(10,dp);
				pweight= pweights.get(i)*Math.pow(10,dp);
				
				if(cweight.intValue() != pweight.intValue()) {
					wconverge= 0;
				}
				
			}
			
			current= surfWithJump(current , 1, jumpThreshold);
			
			for(Integer j=0 ; j<size;j++) {
				
				pweights.set(j, this.getVertex(j).getWeight());
			}
			
		}
	}

	// DISTINCTION LEVEL

	public Integer surfWithJumpUntilHit(Integer v, Integer n, Double jumpThreshold) {
		// PRE: v is vertex to start surf; n >= 0; 0 <= jumpThreshold <= 1.0
		// POST: surfs the graph randomly until visit v for second time (maximum n moves), 
		//       choosing adjacent vertex according to distribution of edge weights if random number is below jumpThreshold, 
		//       choosing any vertex uniformly randomly otherwise;
		//       modifies # visits in vertices
		//       returns number of vertices visited

		// TODO
		
		Integer visit = 0;
		Integer firstvertex = v;
//		getVertex(v).setMarked();
		
		while (n!=0) { 
			Integer adjList = getVertex(v).getPseudoRandomLink();
			
			if(getVertex(v).getPseudoRandomDouble()<jumpThreshold) {		
				v = adjList;
			}else {
				Double randvertex = getVertex(v).getPseudoRandomDouble() * numVertices();
				v=randvertex.intValue();
			}
			
			if(firstvertex == v) {
				getVertex(v).visits++;
				visit++;
				break;
			}
			getVertex(v).visits++;
			
			visit++;
			n--;
			
		}
		return visit;
	}

	public Double averageHittingTime(Integer v, Integer n, Integer maxHits, Double jumpThreshold) {
		// PRE: n >= 1 is number of iterations for averaging; maxHits >= 0; 0 <= jumpThreshold <= 1.0
		// POST: returns average number of vertices visited until hitting, across n iterations,
		//       (not including those which stopped because they reached maxHits)
		//       returns 0 if all iterations reached maxVisits
		
		// TODO
	
		ArrayList<Integer> visitlist=new ArrayList<Integer>();
		for( Integer i = 0 ; i < n ;i++) {
			Integer vertexhit = surfWithJumpUntilHit(v,maxHits,jumpThreshold);
			if(vertexhit>=maxHits){
				continue;
			}else {
				visitlist.add(vertexhit);
			}
			
		}
		Double hittingtime =0.0;
		for(int i =0; i < visitlist.size();i++) {
			hittingtime+=visitlist.get(i)*1.0; 
			
		}
		

		return hittingtime/n;
		
	}

	public Integer surfWithJumpUntilCover(Integer v, Integer n, Double jumpThreshold) {
		// PRE: v is vertex to start surf; n >= 0; 0 <= jumpThreshold <= 1.0
		// POST: surfs the graph randomly until all vertices visited (with maximum n vertices visited), 
		//       choosing adjacent vertex according to distribution of edge weights if random number is below jumpThreshold, 
		//       choosing any vertex uniformly randomly otherwise;
		//       modifies # visits in vertices
		//       returns number of vertices visited
		
		Integer time = 1;
//		Integer count = 1;
		for(int i = 0 ; i < numVertices() ;i++) {
			if(getVertex(i).isMarked()) {
				getVertex(i).setUnmarked();;
			}
		}
		
		getVertex(v).setMarked();
		getVertex(v).visits++;
		int totalvertice = numVertices()-1;
		
		while (n!=0 && totalvertice!=0) { 
			
			
			Integer adjList = getVertex(v).getPseudoRandomLink();
			if(getVertex(v).getPseudoRandomDouble()<jumpThreshold) {		
				v = adjList;
			}else {
				Double randvertex = getVertex(v).getPseudoRandomDouble() * numVertices();
				v=randvertex.intValue();
			}
			
			if(!getVertex(v).isMarked()) {
				totalvertice--;
				getVertex(v).setMarked();
			}
			
			getVertex(v).visits++;
			time++;
			
			
			n--;
			
		}
		
		return time;
	}
	
	public Double averageCoverTime(Integer n, Integer maxVisits, Double jumpThreshold) {
		// PRE: n >= 1 is number of iterations for averaging; maxVisits >= 0; 0 <= jumpThreshold <= 1.0
		// POST: returns average number of vertices visited until cover, across n iterations,
		//         (not including those which stopped because they reached maxVisits)
		//         randomly selecting start vertex each iteration
		//       returns 0 if all iterations reached maxVisits
		
		// TODO
	
		ArrayList<Integer> visitlist=new ArrayList<Integer>();
		for( Integer i = 0 ; i < n ;i++) {
			Double randvertex = getVertex(0).getPseudoRandomDouble() * numVertices();
			Integer v=randvertex.intValue();;
			Integer vertextime = surfWithJumpUntilCover(v,maxVisits,jumpThreshold);
			if(vertextime>=maxVisits){
				continue;
			}else {
				visitlist.add(vertextime);
			}
			
		}
		Double avgtime =0.0;
		for(int i =0; i < visitlist.size();i++) {
			avgtime+=visitlist.get(i)*1.0; 
			
		}
		
		return avgtime/n;
		
	}

	public Integer boostVertex(Integer v, Integer dp) {
		// PRE: v is a vertex in the graph
		// POST: returns a vertex v2 (!= v) such that when edge (v,v2,1.0) is added to the graph,
		//         the weight of v is larger than when edge (v,v3,1.0) is added to the graph
		//         (for any v3), when the graph is surfed to convergence
		//       if there is no such vertex v2 (i.e. v is already connected to all other vertices),
		//         return v
		//       edges are only added if they do not already exist in the graph
		
		// TODO
//		Integer 
		
//	    TreeMap<Integer, Double> change_in_weight = new TreeMap<Integer, Double>();
	    Integer highestedge = Integer.MIN_VALUE;
	    Double highestweight = Double.MIN_VALUE;
		for( Integer i = 0 ; i < numVertices();i++) {
			if(!getVertex(v).adjs.containsKey(i)) {
				add_edge(v,i);
				convergenceWeights(dp,0.9);
				if(getVertex(v).weights >highestweight ) {
					highestweight = getVertex(v).weights ;
					highestedge=i;
				}
//				change_in_weight.put(i, getVertex(v).getWeight());
				remove_edge(v,i);
			}
		}
		
		return highestedge;
	}
	
	
	public static void main(String[] args) {
		GraphApplic g = new GraphApplic(1);

		try {
			g.readWeightedFromFileWSeedAndSetDirected("/Users/nipun/comp(algo&data)/assignment-sample-graphs/new.txt");
			  // change this path to wherever you store your data files
			g.print();
			System.out.print("weight of edge (1,2): ");
			System.out.println(g.getVertex(1).getAdjs().get(2));			
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
			
		
//			g.surfWithJump(g.getFirstVertexID(), 10000, 0.9);
		
		
			g.surfWithJump(g.getFirstVertexID(), 10000, 0.5);
//			g.setVertexWeights();
//			System.out.println(g.averageCoverTime(10, 10, 0.9));
//			g.setVertexWeights();
			for(int i = 0 ; i < g.numVertices();i++) {
				g.getVertex(i).print();;
			}
			
			
		
	}
}

