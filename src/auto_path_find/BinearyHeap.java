import java.util.ArrayList;

public class BinaryHeap {
	//TODO: Implement a binary heap that supports the three operations shown on page 4
	//Each vertex is represented by its row and column number
	
	public class Entry{
		int row, col;
		float cost;
		Entry(int row_, int col_, float cost_) {row = row_;col = col_;cost = cost_;}
	}
	ArrayList<Entry> data;
	int revTable[][];
	
	BinaryHeap() {
		data = new ArrayList<Entry>();
		revTable = new int[120][160];
		for(int i = 0;i < 120;++i) {
			for(int j = 0;j < 160;++j) {
				revTable[i][j] = -1;
			}
		}
	}
	
	private void siftUp(int pos) {
		Entry a, b;
		if(pos == 0) {return;}
		a = data.get(pos);
		b = data.get((pos - 1) / 2);
		if(a.cost < b.cost) {
			data.set(pos, b);
			data.set((pos - 1) / 2, a);
			revTable[b.row][b.col] = pos;
			revTable[a.row][a.col] = (pos - 1) / 2;
			siftUp((pos - 1) / 2);
		} else {
			return;
		}
	}
	
	private void siftDown(int pos) {
		Entry a, b, c;
		if(pos * 2 + 1 >= data.size()) {return;}
		a = data.get(pos);
		b = data.get(pos * 2 + 1);
		if(pos * 2 + 2 < data.size()) {
			c = data.get(pos * 2 + 2);
		} else {
			if(a.cost > b.cost) {
				data.set(pos, b);
				data.set(pos * 2 + 1, a);
				revTable[b.row][b.col] = pos;
				revTable[a.row][a.col] = pos * 2 + 1;
				siftDown(pos * 2 + 1);
				return;
			} else {
				return;
			}
		}
		if(a.cost < b.cost) {
			if(c.cost < a.cost) {
				data.set(pos, c);
				data.set(pos * 2 + 2, a);
				revTable[c.row][c.col] = pos;
				revTable[a.row][a.col] = pos * 2 + 2;
				siftDown(pos * 2 + 2);
			} else {
				return;
			}
		} else {
			if(b.cost < c.cost) {
				data.set(pos, b);
				data.set(pos * 2 + 1, a);
				revTable[b.row][b.col] = pos;
				revTable[a.row][a.col] = pos * 2 + 1;
				siftDown(pos * 2 + 1);
			} else {
				data.set(pos, c);
				data.set(pos * 2 + 2, a);
				revTable[c.row][c.col] = pos;
				revTable[a.row][a.col] = pos * 2 + 2;
				siftDown(pos * 2 + 2);
			}
		}
	}
	
	public void insert(int row, int col, float cost) {
		data.add(new Entry(row, col, cost));
		int pos = data.size() - 1;
		revTable[row][col] = pos;
		siftUp(pos);
	}
	
	public void remove(int row, int col) {
		int pos = revTable[row][col];
		revTable[row][col] = -1;
		if(pos == -1) {return;}
		if(pos == data.size() - 1) {
			data.remove(data.size() - 1);
			return;
		}
		Entry ent = data.get(data.size() - 1);
		data.set(pos, ent);
		data.remove(data.size() - 1);
		revTable[ent.row][ent.col] = pos;
		if(pos > 0 && ent.cost < data.get((pos - 1) / 2).cost) {
			siftUp(pos);
		} else {
			siftDown(pos); 
		}
	}
	
	public Entry pop() {
		if(data.size() == 0) {return null;}
		Entry result = data.get(0);
		revTable[result.row][result.col] = -1;
		Entry ent = data.get(data.size() - 1);
		data.set(0, ent);
		data.remove(data.size() - 1);
		revTable[ent.row][ent.col] = 0;
		siftDown(0);
		return result;
	}
	
	public Entry minkey() {
		if(data.size() == 0) {return null;} else {return data.get(0);}
	}
}
