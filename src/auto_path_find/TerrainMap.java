import java.util.Scanner;

public class TerrainMap {
	int width, height;
	int startX, startY, goalX, goalY;
	int hardReg[][]; //Center position of hard-to-traverse regions
	char data[][];
	/*
	 * data is interpreted as follows:
	 * 0 - blocked
	 * 1 - regular unblocked
	 * 2 - hard to traverse
	 * 3 - regular unblocked with highway
	 * 4 - hard to traverse with highway
	 * Note that these are integers, not char '0', '1', etc.
	 */
	
	
	public TerrainMap() {
		width = 160;
		height = 120;
		startX = 0; startY = 0;
		goalX = 0;goalY = 0;
		hardReg = new int[8][2];
		data = new char[height][width];
		for(int i = 0;i < height;++i) {
			for(int j = 0;j < width;++j) {
				data[i][j] = 1; //Initialize every cell to regular unblocked
			}
		}
	}
	
	public String toString() {
		String result = "";
		result += startX + " " + startY + "\n";
		result += goalX + " " + goalY + "\n";
		for(int i = 0;i < 8;++i) {
			result += hardReg[i][0] + " " + hardReg[i][1] + "\n";
		}
		for(int i = 0;i < height;++i) {
			for(int j = 0;j < width;++j) {
				switch(data[i][j]) {
				case 0:
					result += '0';
					break;
				case 1:
					result += '1';
					break;
				case 2:
					result += '2';
					break;
				case 3:
					result += 'a';
					break;
				case 4:
					result += 'b';
					break;
				default:
					throw new RuntimeException("Invalid map");
				}
			}
			result += '\n';
		}
		return result;
	}
	
	public void fromString(String s) {
		Scanner scan = new Scanner(s);
		startX = scan.nextInt();
		startY = scan.nextInt();
		goalX = scan.nextInt();
		goalY = scan.nextInt();
		for(int i = 0;i < 8;++i) {
			hardReg[i][0] = scan.nextInt();
			hardReg[i][1] = scan.nextInt();
		}
		for(int i = 0;i < height;++i) {
			String line = scan.next();
			for(int j = 0;j < width;++j) {
				char t = line.charAt(j);
				switch(t) {
				case '0':
					data[i][j] = 0;
					break;
				case '1':
					data[i][j] = 1;
					break;
				case '2':
					data[i][j] = 2;
					break;
				case 'a':
					data[i][j] = 3;
					break;
				case 'b':
					data[i][j] = 4;
					break;
				}
			}
		}
		scan.close();
	}
	
	public float getCost(int row, int column, int direction) {
		int stepDelta[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
		int newX = row + stepDelta[direction][0];
		int newY = column + stepDelta[direction][1];
		if(data[row][column] == 0 || data[newX][newY] == 0) {return -1.0f;}
		boolean bothHighway = data[row][column] >= 3 && data[row][column] >= 3;
		boolean oneHard = data[row][column] % 2 == 0 || data[newX][newY] % 2 == 0;
		float result = 0.0f;
		if(oneHard) {
			boolean bothHard = data[row][column] % 2 == 0 && data[newX][newY] % 2 == 0;
			if(bothHard) {
				if(direction < 4) {result = 2.0f;} else {result = (float)Math.sqrt(8.0f);}
			} else {
				if(direction < 4) {result = 1.5f;} else {result = ((float)Math.sqrt(2.0f) + (float)Math.sqrt(8.0f)) / 2;}
			}
		} else {
			if(direction < 4) {result = 1.0f;} else {result = (float)Math.sqrt(2.0f);}
		}
		if(bothHighway) {result /= 4;}
		return result;
	}
}
