
public class ChebvshevHeuristic implements Heuristic{
	TerrainMap map;
	@Override
	public void reset(TerrainMap map) {
		// TODO Auto-generated method stub
			this.map=map;
	}

	@Override
	public float compute(int currX, int currY) {
		// TODO Auto-generated method stub
		return (float)Math.max(Math.abs(currX-map.goalX), Math.abs(currY-map.goalY));
	}

}
