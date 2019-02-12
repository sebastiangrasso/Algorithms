/** ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  File name     :  Pathfinder.java
 *  Purpose       :  Provides a class describing a Pathfinder solution to solve the given Maze Problem
 *  @author       :  Sebastian Grasso / Brett Derham
 *  Date          :  2019-02-11 
 *  Description   :  A Maze Pathfinding algorithm that implements an informed A* tree search.
 *                   Includes the following:
 *                   
 *                   public static ArrayList<String> solve (MazeProblem problem)
 *                   // Given a MazeProblem, which specifies the actions and transitions available in the
 *                      search, returns a solution to the problem as a sequence of actions that leads from
 *                      the initial to a goal state.
 *                   
 *                   public static int manhattan(SearchTreeNode curr, MazeState currentGoal)
 *                   // Helper method designed to calculate Manhattan Distance Heuristic.
 *                   
 *                   public static ArrayList<String> retrace(SearchTreeNode end)
 *                   // Helper method designed to obtain path to solution.
 *                   
 *                   class SearchTreeNode implements Comparable<SearchTreeNode>
 *                   // SearchTreeNode that is used in the Search algorithm to construct the Search tree.
 *                   
 *                   SearchTreeNode (MazeState state, String action, SearchTreeNode parent, int costOfMove)
 *                   // Constructs a new SearchTreeNode to be used in the Search Tree.
 *                                      
 *  Warnings      :  None
 *  Exceptions    :  None
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  Revision History
 *  ---------------
 *            Rev      Date     Modified by:  Reason for change/modification
 *           -----  ----------  ------------  -----------------------------------------------------------
 *  @version 1.0.0  2019-02-11  Authors       Finished homework assignment one
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */


package pathfinder.informed;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * Maze Pathfinding algorithm that implements an informed, A* tree search.
 */
public class Pathfinder {
    
    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
     */
    public static ArrayList<String> solve (MazeProblem problem) {
        
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue <SearchTreeNode>();
        
        SearchTreeNode root = new SearchTreeNode(problem.INITIAL_STATE, null, null, 0);
        frontier.add(root);
        
        while (frontier.isEmpty() == false) {
           
            SearchTreeNode curr = frontier.remove();            
        	
            if (problem.isGoal(curr.state)) {         	
              	ArrayList <String> sol = retrace(curr);
            	int [] test = problem.testSolution(sol);
            	if (test[0] == 1) {
            		System.out.println(curr.historyScore);
            		return sol;
            	}
            }
            
            Map<String, MazeState> moves = problem.getTransitions(curr.state);
           
            for (Map.Entry<String, MazeState> options : moves.entrySet()) {          	
        
            	int costOfMove = problem.getCost(options.getValue());
            	SearchTreeNode temp = new SearchTreeNode(options.getValue(), options.getKey(), curr, costOfMove);
            	
             	if (problem.visitedKey) {
                	int minManhattanScore= 0;            		
            		for (MazeState goal : problem.GOAL_STATE.values()) {
            			if (manhattan(temp, goal) < minManhattanScore) {
            				minManhattanScore = manhattan(temp, goal);
            			}
            		}
            	temp.manhattanScore = minManhattanScore;
            	}
            	else temp.manhattanScore = manhattan(temp, problem.KEY_STATE);
            	            	            	
            	frontier.add(temp);
            }
        }
        return null;
}
    
/**
* Helper method designed to calculate Manhattan Distance Heuristic
* 
* @param curr, currentGoal Method takes in a nodes current location and goal and calculates 
* distance between
* 
* @return Int value for the Manhattan Distance Heuristic
*/

public static int manhattan(SearchTreeNode curr, MazeState currentGoal) {
	
	int dx = Math.abs(curr.state.row - currentGoal.row);
	int dy = Math.abs(curr.state.col - currentGoal.row);
	
	return (dx+dy);
}
    
/**
* Helper method designed to obtain path to solution
* 
* @param end Method takes in and begins at the GOAL_STATE node and works through
* parent nodes to obtain actions that got us to our Goal state
* 
* @return An array of Strings that represent the actions taken to get to the goal
*/       
public static ArrayList<String> retrace(SearchTreeNode end) {
	
	ArrayList <String> steps = new ArrayList <String>();
	SearchTreeNode curr = end;
	
	while (curr.parent!= null) {
		steps.add(0, curr.action);
		curr = curr.parent;	
	}
	
	return steps;

}
    
}    

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode implements Comparable<SearchTreeNode>{
    
    MazeState state;
    String action;
    SearchTreeNode parent;
    int historyScore;
    int manhattanScore;
    int fScore;
    
    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     * 
     * @param state The MazeState (col, row) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     * @param costOfMove The cost of the current move
     */
    SearchTreeNode (MazeState state, String action, SearchTreeNode parent, int costOfMove) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        if (this.parent !=null) {
        	this.historyScore = parent.historyScore + costOfMove;
        }
        	this.manhattanScore = 0;
        this.fScore = this.historyScore + this.manhattanScore;
    }
   
    public int compareTo(SearchTreeNode a) {
    	return this.fScore - a.fScore;
    }
    
    
}
