import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MapGen {
	//static TerrainMap result = new TerrainMap();
	static Random randEngine;
	public static boolean judgeHighwayPoint(TerrainMap map, int x,int y) {
		if(map.data[x][y] == 3 || map.data[x][y] == 4) return false;
		return true;
	}
	
	static class Point {
		int x, y; 
		Point() {x = -1;y = -1;}
		Point(int x_, int y_) {x = x_; y = y_;}
		public boolean equals(Object other) {return this.x == ((Point)other).x && this.y == ((Point)other).y;}
	}
	
	/*
	public static Point getRandomPointHighWay(TerrainMap map) {
		Point temp=new Point();
		int x = randEngine.nextInt(map.height);
		int y = randEngine.nextInt(map.width);
		while(true) {
			if(judgeHighwayPoint(map, x,y)) {
				temp.x=x;
				temp.y=y;
				return temp;
			}
		}
	}
	
	public static boolean addToLeft20(Point p,TerrainMap map,int x, int y) {
		for(int j=1;j<20;j++) {
			if(judgeHighwayPoint(map,x-j,y)) map.data[x-j][y]+=2; else return false;
		}
		p.x-=20;
		return true;
	}
	
	public static boolean addToRight20(Point p,TerrainMap map,int x, int y) {
		for(int j=1;j<20;j++) {
			if(judgeHighwayPoint(map,x-j,y)) map.data[x-j][y]+=2; else return false;
		}
		p.x+=20;
		return true;
	}
	
	public static boolean addToTop20(Point p,TerrainMap map,int x, int y) {
		for(int j=1;j<20;j++) {
			if(judgeHighwayPoint(map,x-j,y)) map.data[x-j][y]+=2; else return false;
		}
		p.y+=20;
		return true;
	}
	
	public static boolean addToBottom20(Point p,TerrainMap map,int x, int y) {
		for(int j=1;j<20;j++) {
			if(judgeHighwayPoint(map,x-j,y))
				map.data[x-j][y]+=2;
			else
				return false;
		}
		p.y-=20;
		return true;
	}
	
	public static boolean addToHorizontal(TerrainMap result,Point p,String str) {
		if(p.x>=result.width-20) {
			if(addToLeft20(p,result,p.x,p.y))
				str="Left";
			else
				return false;
		}
		else if(p.x<=20)
		{
			if(addToRight20(p,result,p.x,p.y))
				str="Right";
			else
				return false;
		}else {
			if(randEngine.nextInt(2)==1) {  
				if(addToLeft20(p,result,p.x,p.y))
					str="Left";
				else
					return false;
			}
			else
			{
				if(addToRight20(p,result,p.x,p.y))
					str="Right";
				else
					return false;
			}
		}
		return true;
	}
	public static boolean addToVertical(TerrainMap result, Point p, String str) {
	if(p.y>=result.height-20) {
		if(addToBottom20(p,result,p.x,p.y))
			str="Bottom";
		else
			return false;
	}
	else if(p.y<=20) {
		if(addToTop20(p,result,p.x,p.y))
			str="Top";
		else
			return false;
	}
	else {
		if(randEngine.nextInt(2)==1)  
		{
			if(addToBottom20(p,result,p.x,p.y))
				str="Bottom";
			else
				return false;
		}
		else
		{
			if(addToTop20(p,result,p.x,p.y))
				str="Top";
			else
				return false;
		}
	}
	return true;
	}
	*/
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage: MapGen [output filename]");
			return;
		}
		try {
			FileWriter writer = new FileWriter(args[0]);
			TerrainMap result = new TerrainMap();

			randEngine = new Random();
			//Place hard-to-traverse regions
			for(int i = 0;i < 8;++i) {
				int p,q;
				result.hardReg[i][0] = p = randEngine.nextInt(result.height);
				result.hardReg[i][1] = q = randEngine.nextInt(result.width);
				for(int j = p - 15;j <= p + 15;++j) {
					if(j < 0 || j >= result.height) {continue;}
					for(int k = q - 15;k <= q + 15;++k) {
						if(k < 0 || k >= result.width) {continue;}
						if(randEngine.nextInt(2) == 1) {
							result.data[j][k] = 2;
						}
					}
				}
			}

			//TODO: Place highways
			while(true) {
				int i;
				for(i = 0;i < 4;i++) {
					int attempt;
					ArrayList<Point> currentHighway = new ArrayList<Point>();
					for(attempt = 0;attempt < 10;++attempt) {
						Point p = new Point();
						int currentDirection = -1;
						//0: Going from left to right
						//1: Going from right to left
						//2: Going from top to bottom
						//3: Going from bottom to top
						int startEdge = randEngine.nextInt(4);
						if(startEdge % 2 == 0) { // Start from top or bottom row
							if(startEdge / 2 == 1) {p.x = 0;currentDirection = 2;} else {p.x = result.height - 1;currentDirection = 3;}
							p.y = randEngine.nextInt(result.width);
						} else {
							if(startEdge / 2 == 1) {p.y = 0;currentDirection = 0;} else {p.y = result.width - 1;currentDirection = 1;}
							p.x = randEngine.nextInt(result.height);
						}
						if(result.data[p.x][p.y] >= 3) {continue;}

						currentHighway.clear();
						currentHighway.add(new Point(p.x, p.y));
						int stepDelta[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
						boolean hitBoundary = false, failed = false;
						while(true) {
							for(int j = 0;j < 20;++j) {
								p.x += stepDelta[currentDirection][0];
								p.y += stepDelta[currentDirection][1];
								if(p.x < 0 || p.x >= result.height || p.y < 0 || p.y >= result.width) {
									hitBoundary = true;break;
								} else {
									if(result.data[p.x][p.y] >= 3 || currentHighway.contains(p)) {
										failed = true;break;
									} else {
										currentHighway.add(new Point(p.x, p.y));
									}
								}
							}
							if(hitBoundary || failed) {break;}
							int dice = randEngine.nextInt(10);
							if(currentDirection >= 2) {
								if(dice > 5 && dice < 8) {
									currentDirection = 0;
								} else if(dice >= 8) {
									currentDirection = 1;
								}
							} else {
								if(dice > 5 && dice < 8) {
									currentDirection = 2;
								} else if(dice >= 8) {
									currentDirection = 3;
								}
							}
						}
						if(currentHighway.size() < 100) {failed = true;}
						if(!failed) {
							for(int j = 0;j < currentHighway.size();++j) {
								Point m = currentHighway.get(j);
								result.data[m.x][m.y] += 2;
							}
							break;
						}
					}
					if(attempt == 10) {break;}
				}
				if(i < 4) {
					for(int x = 0;x < result.height;++x) {
						for(int y = 0;y < result.width;++y) {
							if(result.data[x][y] >= 3) {result.data[x][y] -= 2;}
						}
					}
				} else break;
			}
			
			/*
			int failNum=0;   //use to judge if the point is already a highway point
			String dir="null";
			TerrainMap resultCopyInit=result;
			for(int i=0;i<4;i++)
			{
				if(failNum>100)
				{
					result=resultCopyInit;
					i=0;
					failNum=0;
				}
				TerrainMap resultCopy=result;
				Point p;
				int highwayLen=1;
				p=getRandomPointHighWay(result, i);
				if(randEngine.nextInt(2)==1)    //if move horizontal
				{
						if(!addToHorizontal(result,p,dir)) {
							failNum++;
							result=resultCopy;
							i--;
							continue;
						}
						else
							highwayLen+=20;
				}
				else      //if move vertical
					    if(!addToVertical(result,p,dir)) {
					    	failNum++;
					    	result=resultCopy;
					    	i--;
					    	continue;
					    }
					    else
					    	highwayLen+=20;
				while(randEngine.nextInt(10)<=6) {
					switch(dir) {
					case  "Top":
						if(p.y>=result.height-20 && (highwayLen+result.height-p.y<100)) {
							failNum++;
							result=resultCopy;
							i--;
							continue;
						}
						else if(p.y>=result.height-20 && (highwayLen+result.height-p.y>=100))
						{
							for(int j=p.y;j<=result.height;j++)
							{
								if(highwayLen<100) {
									p.y++;
									result.data[p.x][p.y]+=2;
								}
								else
									continue;
							}
						}
						else if(!(addToTop20(p,result,p.x,p.y)))
						{
							failNum++;
							result=resultCopy;
							i--;
							continue;
						}
						break;	
					}
				case  "Bottom":
					if(p.y<=20 && (highwayLen+p.y<100)) {
						failNum++;
						result=resultCopy;
						i--;
						continue;
					}
					else if(p.y<=20 && (highwayLen+p.y>=100)) {
						for(int j=p.y;j<=result.height;j++)
						{
							if(highwayLen<100) {
								p.y--;
								result.data[p.x][p.y]+=2;
							}
							else
								continue;
						}
					}
					else if(!(addToBottom20(p,result,p.x,p.y)))
					{
						failNum++;
						result=resultCopy;
						i--;
						continue;
					}
					break;
				case  "Left":
					if(p.x<=20 && (highwayLen+p.x<100) ) {
						failNum++;
						result=resultCopy;
						i--;
						continue;
					}
					else if(p.x<=20 && (highwayLen+p.x<100)) {
						if(highwayLen<100)
						{
							p.x--;
							result.data[p.x][p.y]+=2;
						}
						else
							continue;
					}
					else if(!(addToLeft20(p,result,p.x,p.y)))
					{
						failNum++;
						result=resultCopy;
						i--;
						continue;
					}
					break;
				case  "Right":
					if(p.x>=result.width-20 && (highwayLen+result.width-p.x<100)) {
						failNum++;
						result=resultCopy;
						i--;
						continue;
					}
					else if(p.x>=result.width-20 && (highwayLen+result.width-p.x<100)) {
						if(highwayLen<100)
						{
							p.x++;
							result.data[p.x][p.y]+=2;
						}
						else
							continue;
					}
					else if(!(addToLeft20(p,result,p.x,p.y)))
					{
						failNum++;
						result=resultCopy;
						i--;
						continue;
					}
					break;
				}

			}
			*/
			int errNum=0;
			//TODO: Place blocked cells
			for(int i=0;i<3840;i++)
			{
				errNum=0;
				while(true)
				{
					int x = randEngine.nextInt(result.height);
					int y = randEngine.nextInt(result.width);
					if(result.data[x][y]==1 || result.data[x][y]==2) {	result.data[x][y]=0; break;}	
					errNum+=1;
					if(errNum>100)	{errNum=0;	continue;}
				}
			}
			
			
			//TODO: Place start and goal position
			//insert start
			while(true)
			{
				int sx=randEngine.nextInt(40);
				int sy=randEngine.nextInt(40);
				if(sx>20)	sx=result.height-40+sx;
				if(sy>20)	sy=result.width-40+sy;
				if(result.data[sx][sy]!=0)	{result.startX=sx;	result.startY=sy;	break;}
			}
			
			//insert goal
			while(true)
			{
				int gx=randEngine.nextInt(40);
				int gy=randEngine.nextInt(40);
				if(gx>20)	gx=result.height-40+gx;
				if(gy>20)	gy=result.width-40+gy;
				if(result.data[gx][gy]!=0 && (Math.pow((result.startX-gx),2)+Math.pow((result.startY-gy),2))>=10000) 
				{
					result.goalX=gx;	
					result.goalY=gy;
					break;
				}
			}
			writer.write(result.toString());
			writer.close();
		} catch(IOException e) {
			System.out.println("IOException occurred");
			e.printStackTrace();
		}
	}
}
