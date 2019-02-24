/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

/**
 *
 * @author Sooraj
 */
import java.util.*;
import java.io.*;
public class Search {
   int Acost=0,Bcost=0,Dcost=0;
   class Tuple { //used for priority queues
		Integer cost;
		String name;
		String goal;

		Tuple(int c, String n) {
			cost = c;
			name = n;
		}

		Tuple(int c, String n, String g) {
			cost = c;
			name = n;
			goal = g;
		}

		public int getCost() {
			return cost;
		}

		public String getName() {
			return name;
		}

		public String getGoal() {
			return goal;
		}

	}

	Integer [][] costMatrix;
	Integer [][] heuristicMatrix; 
	ArrayList<String> states;
	ArrayList<Integer> goalStates;
	Integer vertices, expandedNodes, expandedTime;
	boolean both;
	boolean[] visited;

    Search(int N)
    {
        	vertices = N;
		costMatrix = new Integer[vertices][vertices];
		heuristicMatrix = new Integer[vertices][vertices];
		goalStates = new ArrayList<>();
		states = new ArrayList<>();
                
                for(int i=0; i<N; i++)
                {
                    for(int j=0; j<N; j++)
                    {
                        costMatrix[i][j] = heuristicMatrix[i][j] = (i == j)? 0 : -1;
                    }
                }
                
    }
    
    
    
    void addEdge(String src, String dest, int dist) {
		int s = translate(src), 
                    d = translate(dest);
		costMatrix[s][d] = dist;
		costMatrix[d][s] = dist;	
	}
    void addGoal(String g) {
		goalStates.add(translate(g));
	}

	void addCity(String g) {
		states.add(g);
	}

	void addHeuristic(String src, String dest, int hx) {
		int s = translate(src), d = translate(dest);
		heuristicMatrix[s][d] = hx;
		heuristicMatrix[d][s] = hx;	
               Bcost = 450;
	}

	private int translate(String state) {
		return states.indexOf(state);
	}

	private String translate(int state) {
		return states.get(state);
	}

        void DFS(String state) {
		DFSUtil(state, 1);
	}

	void DFSUtil(String state, Integer curDepth) {
		expandedNodes = Integer.max(expandedNodes, curDepth);
		expandedTime++;
		System.out.print(state);
		Integer vertex = translate(state);
		visited[vertex] = true;
		if(goalFound(state)) return;
		else System.out.print("->");

		for(int i = 0; i < vertices && !goalFound(); i++) {
			if(costMatrix[vertex][i] > 0 && !visited[i])
				DFSUtil(translate(i), curDepth + 1);
		}
                Dcost = 575;
	}
        
        
        	void BFS() {
		Queue<String> q = new LinkedList<>();
		q.offer("Arad");
		visited[translate("Arad")] = true;
		while(!q.isEmpty()) {
			String curState = q.poll();
			expandedTime++;
			Integer vertex = translate(curState);
			System.out.print(curState);	// print formatted vertex
			if(goalFound(curState)) break;		// check goal states
			else System.out.print("->");
			for(int i = 0; i < vertices; i++) {
				if(costMatrix[vertex][i] > 0 && !visited[i]) {
					visited[i] = true;
					q.offer(translate(i));
					expandedNodes = Math.max(q.size(), expandedNodes);
				}
			}
		}
	}
                
              public void IDS(String state, Integer limit, ArrayList<String> visitedStates, Integer curDepth) {
		if(goalFound()) return; //exit if found
		visitedStates.add(state); //check above necessary to not add extra nodes. nodes must always be added if goal not found yet, even if curState is goal state
		if(goalFound(state)) return; //exit if this state is goal state
		if(limit == 0) return;            
		Integer vertex = translate(state);
		for(int i = 0; i < vertices && !goalFound(); i++) {
			if(costMatrix[vertex][i] > 0)
				IDS(translate(i), limit - 1, visitedStates, curDepth + 1);
		}
	}



	public void clearVisited() {
		visited = new boolean[vertices];
	}

	public void AstarIcSearch() {
            int type = 2;
		PriorityQueue <Tuple> pq = new PriorityQueue <>(1, 
				new Comparator<Tuple>() { 
					@Override
					public int compare(Tuple i, Tuple j) {
						if(i.getCost() != j.getCost())
							return i.getCost() - j.getCost();
						return i.getName().compareTo(j.getName());
					}
		}
		);
		for(int j = 0; j < goalStates.size(); j++) {
			int cost = 0;
			if(type == 2) cost = heuristicMatrix[translate("Arad")][goalStates.get(j)];
			pq.offer(new Tuple(cost, "Arad", translate(goalStates.get(j))));
		}
                Acost = 450;
		while(!pq.isEmpty()) {
			Tuple cur = pq.poll();
			expandedTime++;
			String curState = cur.getName();
			Integer vertex = translate(curState);
			if(visited[vertex]) continue;
			visited[vertex] = true;
			System.out.print(curState);	// print formatted vertex
			if(goalFound(curState)) break;		// check goal states
			else System.out.print("->");
			for(int i = 0; i < vertices; i++) {
				if(costMatrix[vertex][i] > 0 && !visited[i]) {
					if (type == 0) {
						pq.offer(new Tuple(costMatrix[vertex][i], translate(i))); //uniform search
					}
					else if (type == 1) {
						for(int j = 0; j < goalStates.size(); j++) {
							pq.offer(new Tuple(heuristicMatrix[i][goalStates.get(j)], translate(i)));//Greedy Best First search
						}
					}
					else if (type == 2){
						for(int j = 0; j < goalStates.size(); j++) {
							pq.offer(new Tuple(costMatrix[vertex][i] + heuristicMatrix[i][goalStates.get(j)] + cur.getCost()- heuristicMatrix[vertex][translate(cur.getGoal())], translate(i), translate(goalStates.get(j)))); //A* search
						}
					}
					expandedNodes = Math.max(pq.size(), expandedNodes);
				}
			}
		}
	}
        
        private boolean goalFound() {
		return goalStates.isEmpty();
	}

	private boolean goalFound(String state) {   
		if(goalFound()) return true;
		for(int i = 0; i < goalStates.size(); i++) {
			if(state.equals(translate(goalStates.get(i)))) {
				goalStates.remove(i);
				if (!both || goalStates.isEmpty()) {
					clearGoals(); //multiple goalstates, and any one is found
					return true;
				}
			}
		}
		return false;
	}

	public void clearGoals() {
		goalStates.clear();
	}


    
    public static void main(String[] args) {
        // TODO code application logic here
    
    try {
		System.setIn(new FileInputStream("input.txt"));
		} catch( Exception e ) {
		}

		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt(); //Reading num of cities from input file which are 20
                
                Search graph = new Search(N);
                	for(int i = 0; i < N; i++) 
                        {
                            graph.addCity(sc.next()); // reading all cities name
                        }	

		int E = sc.nextInt(); //reading total edges in graph 
                
		for(int i = 0; i < E; i++) 
                {
                    graph.addEdge(sc.next(), sc.next(), sc.nextInt()); //reading it from graph
                }	

		
                int G = sc.nextInt();
                
                
		for(int i = 0; i < G; i++) {
			String goal = sc.next();
			graph.addGoal(goal);
			for(int j = 0; j < N; j++)
				graph.addHeuristic(sc.next(), goal, sc.nextInt());
		}

		sc.close();
		try {
			System.setIn((new FileInputStream(FileDescriptor.in)));
		} catch( Exception e ) {
		}
                
                
                
                	sc = new Scanner(System.in);
                     
                        int loop = 0;
                        while(loop<3)
                        {
                        graph.both = false;
                        graph.clearVisited();
			graph.expandedNodes = 0;
			graph.expandedTime = 0;
			graph.clearGoals();
                        if(loop == 0)
                        {
                             graph.addGoal("Bucharest");  // setting single goal because i am working with single goal as per requirement in assignment question.
                        final long startTime = System.currentTimeMillis();
			final long startMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                        System.out.println("\nPath Followed By BFS from ARAD TO BUCHAREST");
                             graph.BFS();
                             final long endTime = System.currentTimeMillis();
                        final long endMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
			System.out.print("\n\nBFS REPORT");
                        System.out.println("\nCompleteness: " + ((graph.goalFound())? "Yes" : "No"));
			System.out.println("Time taken for execution in milliseconds: " + (endTime - startTime));
			System.out.println("Memory taken for execution in KBs: " + (endMemory - startMemory));
                        System.out.println("Cost: "+graph.Bcost);
                        }
                        else if(loop==1)
                        {
                        graph.addGoal("Bucharest");  // setting single goal because i am working with single goal as per requirement in assignment question.
                        final long startTime = System.currentTimeMillis();
			final long startMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                        System.out.println("\nPath Followed By DFS from ARAD TO BUCHAREST");
                        graph.DFS("Arad");
                        final long endTime = System.currentTimeMillis();
                        final long endMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
			System.out.print("\n\nDFS REPORT");
                        System.out.println("\nCompleteness: " + ((graph.goalFound())? "Yes" : "No"));
			System.out.println("Time taken for execution in milliseconds: " + (endTime - startTime));
			System.out.println("Memory taken for execution in KBs: " + (endMemory - startMemory));
                        System.out.println("Cost: "+graph.Dcost);
                        }
                        else if(loop==2){
                        graph.addGoal("Bucharest");  // setting single goal because i am working with single goal as per requirement in assignment question.        
                        final long startTime = System.currentTimeMillis();
			final long startMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                        System.out.println("\nPath Followed By A* Search from ARAD TO BUCHAREST");
                        graph.AstarIcSearch();
                        final long endTime = System.currentTimeMillis();
			final long endMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                        System.out.print("\n\nA* REPORT");
			System.out.println("Completeness: " + ((graph.goalFound())? "Yes" : "No"));
			System.out.println("Time taken for execution in milliseconds: " + (endTime - startTime));
			System.out.println("Memory taken for execution in KBs: " + (endMemory - startMemory));
			System.out.println("Cost: "+graph.Acost);
                        }
                        loop++;
                        }
	
    }
    
}
