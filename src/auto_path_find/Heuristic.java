
public interface Heuristic {
	public void reset(TerrainMap map);
	//Before A* algorithm begins, the program invokes reset() for each heuristic, and passes in the map data.
	//The heuristic class should store this map. It may also perform preliminary analysis on the map.
	
	public float compute(int currX, int currY);
	//Compute the heuristic cost from current cell to goal.
	//currX and currY represent the row and column of current cell.
}
