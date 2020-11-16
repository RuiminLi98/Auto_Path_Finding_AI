import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class RunSearch {
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Usage: RunSearch [input map filename] [method choice] [weight value = 1.0] [w2 value = 1.0]");
			System.out.println("[method choice] can be -a, -b, -c, -d, -e, or -seq.");
			return;
		}
		try {
			String input = new String(Files.readAllBytes(Paths.get(args[0])));
			TerrainMap map = new TerrainMap();
			Scanner scan = new Scanner(System.in);
			map.fromString(input);
			
			if(!args[1].equals("-seq")) {
				AStarSearcher searcher = new AStarSearcher();
				searcher.map = map;
				if(args[1].equals("-a")) {
					searcher.heur = new PlainHeuristic();
				} else if(args[1].equals("-b")) {
					searcher.heur = new ManhattenHeuristic();
				} else if(args[1].equals("-c")) {
					searcher.heur = new ChebvshevHeuristic();
				} else if(args[1].equals("-d")) {
					searcher.heur = new FourthHeuristic();
				} else if(args[1].equals("-e")) {
					searcher.heur = new FifthHeuristic();
				}
				if(args.length < 3) {
					searcher.weight = 1.0f;
				} else {
					searcher.weight = Float.parseFloat(args[2]);
				}
				long startTime = System.nanoTime();
				if(searcher.search()) {
					long endTime = System.nanoTime();
					System.out.println("Solution found, cost: " + searcher.cost[map.goalX][map.goalY]);
					System.out.println("Milliseconds: " + (endTime - startTime) / 1000000);
					String output;
					if(args.length <= 4) {
						System.out.println("Write solution to file: ");
						output = scan.nextLine();
					} else {
						output = args[4];
					}
					FileWriter outputWriter = new FileWriter(output);
					int currX = map.goalX, currY = map.goalY;
					while(currX != map.startX || currY != map.startY) {
						outputWriter.write(currX + " " + currY + "\n");
						int dir = searcher.direction[currX][currY];
						int stepDelta[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
						currX -= stepDelta[dir][0];
						currY -= stepDelta[dir][1];
					}
					outputWriter.close();
				}
				if(args.length <= 5) {
					System.out.println("Enter the coordinates of a cell to view its associated cost. Enter -1 to end.");
					do {
						int inputX = scan.nextInt();
						if(inputX == -1) {break;}
						int inputY = scan.nextInt();
						if(inputY == -1) {break;}
						System.out.println("Closed: " + searcher.closed[inputX][inputY] + ", Cost: " + searcher.cost[inputX][inputY] + ", Heuristic: " + searcher.heur.compute(inputX, inputY));
					} while(true);
				}
			} else {
				SeqAStarSearcher searcher = new SeqAStarSearcher();
				searcher.map = map;
				searcher.heur = new Heuristic[5];
				searcher.heur[0] = new PlainHeuristic();
				searcher.heur[1] = new ManhattenHeuristic();
				searcher.heur[2] = new ChebvshevHeuristic();
				searcher.heur[3] = new FourthHeuristic();
				searcher.heur[4] = new FifthHeuristic();
				if(args.length >= 3) {
					searcher.w1 = Float.parseFloat(args[2]);
					if(args.length >= 4) {
						searcher.w2 = Float.parseFloat(args[3]);
					} else {
						searcher.w2 = 1.0f;
					}
				} else {
					searcher.w1 = 1.0f;
					searcher.w2 = 1.0f;
				}
				long startTime = System.nanoTime();
				if(searcher.search()) {
					long endTime = System.nanoTime();
					System.out.println("Solution found, cost: " + searcher.cost[map.goalX][map.goalY][searcher.solutionIndex]);
					System.out.println("Milliseconds: " + (endTime - startTime) / 1000000);
					String output;
					if(args.length <= 4) {
						System.out.println("Write solution to file: ");
						output = scan.nextLine();
					} else {
						output = args[4];
					}
					FileWriter outputWriter = new FileWriter(output);
					int currX = map.goalX, currY = map.goalY;
					while(currX != map.startX || currY != map.startY) {
						outputWriter.write(currX + " " + currY + "\n");
						int dir = searcher.direction[currX][currY][searcher.solutionIndex];
						int stepDelta[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
						currX -= stepDelta[dir][0];
						currY -= stepDelta[dir][1];
					}
					outputWriter.close();
				}
				if(args.length <= 5) {
				System.out.println("Enter the coordinates of a cell to view its associated cost. Enter -1 to end.");
					do {
						int inputX = scan.nextInt();
						if(inputX == -1) {break;}
						int inputY = scan.nextInt();
						if(inputY == -1) {break;}
						for(int i = 0;i < searcher.heur.length;++i) {
							System.out.println("Heuristic " + i + ": " + "Closed: " + searcher.closed[inputX][inputY][i] + ", Cost: " + searcher.cost[inputX][inputY][i] + ", Heuristic: " + searcher.heur[i].compute(inputX, inputY));
						}
					} while(true);
				}
			}
			scan.close();
		} catch(IOException e) {
			System.out.println("IOException occurred");
			e.printStackTrace();
		}
	}
}
