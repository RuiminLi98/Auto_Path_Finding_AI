
public class ManhattenHeuristic implements Heuristic{
	TerrainMap map;

	@Override
	public void reset(TerrainMap map) {
		// TODO Auto-generated method stub
		this.map=map;
		return;
	}

	@Override
	public float compute(int currX, int currY) {
		// TODO Auto-generated method stub
		return (float)(Math.abs(map.goalX-currX)+Math.abs(map.goalY-currY));
	}
	
}
