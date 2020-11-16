
public class SeqAStarSearcher {
	//TODO: Implement the sequential A* search algorithm.
	//Both uniform-cost search and normal A* seearch are special cases of weighted A* search.
	//Uniform-cost search corresponds to w = 0
	//Normal A* search corresponds to w = 1
	//The search() function returns true if a path is found, and false if a path is not found.
	//If search() returns true, the path can be reconstructed from map.direction[][].
		
	TerrainMap map; //Use map.closed[][] to keep track of closed cells.
	BinaryHeap fringe[]; //Use fringe to keep track of the fringe (open) list.
	float w1, w2;
	Heuristic heur[];
	float cost[][][];
	boolean closed[][][];
	char direction[][][];
	int solutionIndex;
	/*
	 * If a cell is closed or in the fringe, direction[][] is used to record the direction from which we entered this cell.
	 * This is used to reconstruct the path after the A* search.
	 * 0 - Entered from left
	 * 1 - Entered from right
	 * 2 - Entered from top
	 * 3 - Entered from bottom
	 * 4 - Entered from top left
	 * 5 - Entered from top right
	 * 6 - Entered from bottom left
	 * 7 - Entered from bottom right
	 */
	
	void expandNode(int row, int column, int heurIdx) {
		closed[row][column][heurIdx] = true;
		int stepDelta[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
		for(int i = 0;i < 8;++i) {
			int newX = row + stepDelta[i][0];
			int newY = column + stepDelta[i][1];
			if(newX < 0 || newX >= map.height || newY < 0 || newY >= map.width) {continue;}
			if(closed[newX][newY][heurIdx]) {continue;}
			float stepCost = map.getCost(row, column, i);
			if(stepCost >= 0.0f && (cost[newX][newY][heurIdx] < 0.0f || cost[row][column][heurIdx] + stepCost < cost[newX][newY][heurIdx])) {
				cost[newX][newY][heurIdx] = cost[row][column][heurIdx] + stepCost;
				direction[newX][newY][heurIdx] = (char)i;
				fringe[heurIdx].remove(newX, newY);
				fringe[heurIdx].insert(newX, newY, cost[newX][newY][heurIdx] + heur[heurIdx].compute(newX, newY) * w1);
			}
		}
	}
		
	boolean search() {
		cost = new float[map.height][map.width][heur.length];
		closed = new boolean[map.height][map.width][heur.length];
		direction = new char[map.height][map.width][heur.length];
		for(int i = 0;i < map.height;++i) {
			for(int j = 0;j < map.width;++j) {
				for(int k = 0;k < heur.length;++k) {
					cost[i][j][k] = -1.0f;
					closed[i][j][k] = false;
					direction[i][j][k] = (char)127;
				}
			}
		}
		fringe = new BinaryHeap[heur.length];
		for(int i = 0;i < heur.length;++i) {
			fringe[i] = new BinaryHeap();
			heur[i].reset(map);
			cost[map.startX][map.startY][i] = 0.0f;
			fringe[i].insert(map.startX, map.startY, heur[i].compute(map.startX, map.goalX));
		}
		solutionIndex = -1;
		
		while(true) {
			BinaryHeap.Entry anchor = fringe[0].minkey();
			if(anchor == null) {break;}
			boolean found = false;
			for(int i = 1;i < heur.length;++i) {
				BinaryHeap.Entry ent = fringe[i].minkey();
				if(ent != null && ent.cost <= w2 * anchor.cost) {
					found = true;
					if(cost[map.goalX][map.goalY][i] >= 0.0f && cost[map.goalX][map.goalY][i] <= ent.cost) {
						solutionIndex = i;
						return true;
					} else {
						fringe[i].pop();
						expandNode(ent.row, ent.col, i);
					}
				}
			}
			if(!found) {
				if(cost[map.goalX][map.goalY][0] >= 0.0f && cost[map.goalX][map.goalY][0] <= anchor.cost) {
					solutionIndex = 0;
					return true;
				} else {
					fringe[0].pop();
					expandNode(anchor.row, anchor.col, 0);
				}
			}
		}
		
		return solutionIndex >= 0;
	}
}
