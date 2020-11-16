
public class FifthHeuristic implements Heuristic{
	TerrainMap map;
	int sumOfHardness[][];
	
	@Override
	public void reset(TerrainMap map) {
		// TODO Auto-generated method stub
		this.map=map;
		sumOfHardness = new int[map.height][map.width];
		for(int i = 0;i < map.height;++i) {
			int rowSum = 0;
			for(int j = 0;j < map.width;++j) {
				if(map.data[i][j] == 0) {
					rowSum += 2;
				} else if(map.data[i][j] == 2 || map.data[i][j] == 4) {
					rowSum += 1;
				}
				sumOfHardness[i][j] = rowSum;
				if(i > 0) {sumOfHardness[i][j] += sumOfHardness[i - 1][j];}
			}
		}
		return;
	}

	@Override
	public float compute(int currX, int currY) {
		int top = currX - 7, bottom = currX + 7, left = currY - 7, right = currY + 7;
		if(top < 0) {top = 0;}
		if(bottom >= map.height) {bottom = map.height - 1;}
		if(left < 0) {left = 0;}
		if(right >= map.width) {right = map.width - 1;}
		float sum = sumOfHardness[bottom][right];
		if(left > 0) sum -= sumOfHardness[bottom][left - 1];
		if(top > 0)	sum -= sumOfHardness[top - 1][right];
		if(left > 0 && top > 0) sum += sumOfHardness[top - 1][left - 1];
		sum /= 20;
		return (float)(Math.abs(map.goalX-currX)+Math.abs(map.goalY-currY)) + sum;
	}

}
