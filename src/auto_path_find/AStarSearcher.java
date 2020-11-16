
public class AStarSearcher {
	//TODO: Implement the weighted A* search algorithm.
	//Both uniform-cost search and normal A* search are special cases of weighted A* search.
	//Uniform-cost search corresponds to w = 0
	//Normal A* search corresponds to w = 1
	//The search() function returns true if a path is found, and false if a path is not found.
	//If search() returns true, the path can be reconstructed from direction[][].
	
	TerrainMap map;
	boolean closed[][]; //records whether a cell is closed during an A* search.
	BinaryHeap fringe; //Use fringe to keep track of the fringe (open) list.
	float weight;
	Heuristic heur;
	float cost[][];
	char direction[][];
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
	
	boolean search() {
		//Initialize
		fringe = new BinaryHeap();
		closed = new boolean[map.height][map.width];
		cost = new float[map.height][map.width];
		direction = new char[map.height][map.width];
		for(int i = 0;i < map.height;++i) {
			for(int j = 0;j < map.width;++j) {
				closed[i][j] = false;
				cost[i][j] = -1.0f;
				direction[i][j] = (char)127;
			}
		}
		heur.reset(map);
		cost[map.startX][map.startY] = 0.0f;
		fringe.insert(map.startX, map.startY, 0.0f);
		while(true) {
			BinaryHeap.Entry ent = fringe.pop();
			if(ent == null) {break;}
			if(cost[map.goalX][map.goalY] >= 0.0f && cost[map.goalX][map.goalY] <= ent.cost) {break;}
			int x = ent.row, y = ent.col;
			closed[x][y]= true;
			int stepDelta[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
			for(int i = 0;i < 8;++i) {
				int newX = x + stepDelta[i][0];
				int newY = y + stepDelta[i][1];
				if(newX < 0 || newX >= map.height || newY < 0 || newY >= map.width) {continue;}
				if(closed[newX][newY]) {continue;}
				float stepCost = map.getCost(x, y, i);
				if(stepCost >= 0.0f && (cost[newX][newY] < 0.0f || cost[x][y] + stepCost < cost[newX][newY])) {
					cost[newX][newY] = cost[x][y] + stepCost;
					direction[newX][newY] = (char)i;
					fringe.remove(newX, newY);
					fringe.insert(newX, newY, cost[newX][newY] + heur.compute(newX, newY) * weight);
				}
			}
		}
		
		return !(cost[map.goalX][map.goalY] < 0.0f);
	}
}
