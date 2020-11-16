
public class PlainHeuristic implements Heuristic {
	TerrainMap map;

	@Override
	public void reset(TerrainMap map) {
		this.map = map;
		return;
	}

	@Override
	public float compute(int currX, int currY) {
		return (float)Math.sqrt(Math.pow(currX - map.goalX, 2) + Math.pow(currY - map.goalY, 2)) / 4;
	}

}
