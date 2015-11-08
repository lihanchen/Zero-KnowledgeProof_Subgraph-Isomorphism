import java.util.*;

public class Graph {
	int size;
	boolean edge[][];
	Scanner scanner;

	public Graph(Scanner scanner) {
		this.scanner = scanner;
		LinkedList<Boolean> ll = new LinkedList<Boolean>();
		int i = 0;
		try {
			while (true) {
				ll.add(scanner.nextInt() == 1);
				i++;
			}
		} catch (NoSuchElementException e) {
			scanner.nextLine();
		}

		size = (int) Math.sqrt(i);
		edge = new boolean[size][size];
		Iterator<Boolean> iterator = ll.iterator();
		for (i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				this.edge[i][j] = iterator.next();
	}

	public Graph(int size) {
		this.size = size;
		edge = new boolean[size][size];
	}

	public Graph(int size, boolean edge[][]) {
		this.size = size;
		edge = new boolean[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				this.edge[i][j] = edge[i][j];
	}

	public Graph subGraph() {
		HashSet<Integer> remainingSet = new HashSet<Integer>();
		for (String num : scanner.nextLine().split(" "))
			remainingSet.add(Integer.parseInt(num));
		return subGraph(remainingSet);
	}

	public Graph subGraph(HashSet<Integer> remainingSet) {
		int newSize = remainingSet.size();
		Graph ret = new Graph(newSize);
		LinkedList<Boolean> ll = new LinkedList<Boolean>();
		for (int i = 0; i < size; i++)
			if (remainingSet.contains(i))
				for (int j = 0; j < size; j++)
					if (remainingSet.contains(i))
						ll.add(edge[i][j]);

		Iterator<Boolean> iterator = ll.iterator();
		for (int i = 0; i < newSize; i++)
			for (int j = 0; j < newSize; j++)
				ret.edge[i][j] = iterator.next();
		return ret;
	}


}
