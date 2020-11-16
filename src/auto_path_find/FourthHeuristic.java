
public class FourthHeuristic implements Heuristic{
	TerrainMap map;
	@Override
	public void reset(TerrainMap map) {
		this.map=map;
		return;
	}

	@Override
	public float compute(int currX, int currY) {
		float h = Math.abs(currX - map.goalX);
		float w = Math.abs(currY - map.goalY);
		if(h > w) {
			return w * (float)Math.sqrt(2) + (h - w);
		} else {
			return h * (float)Math.sqrt(2) + (w - h);
		}
	}
	
}
