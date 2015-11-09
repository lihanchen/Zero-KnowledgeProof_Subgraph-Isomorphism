import javafx.util.Pair;

import java.util.*;

public class Graph {
	HashSet<Integer> vertex;
	HashSet<Pair<Integer, Integer>> edges;


	public Graph() {
		LinkedList<Boolean> ll = new LinkedList<Boolean>();
		int i = 0;
		try {
			while (true) {
				ll.add(Proof.scanner.nextInt() == 1);
				i++;
			}
		} catch (NoSuchElementException e) {
		}

		vertex = new HashSet<Integer>();
		edges = new HashSet<Pair<Integer, Integer>>();

		int size = (int) Math.sqrt(i);
		for (i = 1; i <= size; i++) {
			vertex.add(i);
		}

		Iterator<Boolean> iterator = ll.iterator();
		for (i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (iterator.next()) edges.add(new Pair<Integer, Integer>(i, j));
	}

	public Graph(HashSet<Integer> vertex, HashSet<Pair<Integer, Integer>> edges) {
		this.vertex = vertex;
		this.edges = edges;
	}

	public Graph(Graph graph) {
		vertex = new HashSet<Integer>();
		edges = new HashSet<Pair<Integer, Integer>>();
		for (Integer i : graph.vertex) vertex.add(i);
		for (Pair<Integer, Integer> p : graph.edges) edges.add(new Pair<Integer, Integer>(p.getKey(), p.getValue()));
	}

	public Graph subGraph() {
		Proof.scanner.nextLine();
		HashSet<Integer> remainingSet = new HashSet<Integer>();
		for (String num : Proof.scanner.nextLine().split(" "))
			remainingSet.add(Integer.parseInt(num));
		return subGraph(remainingSet);
	}

	public Graph subGraph(HashSet<Integer> remainingSet) {
		Graph newGraph = new Graph(this);
		Iterator<Integer> iterator = newGraph.vertex.iterator();
		while (iterator.hasNext())
			if (!remainingSet.contains(iterator.next()))
				iterator.remove();
		Iterator<Pair<Integer, Integer>> iterator2 = newGraph.edges.iterator();
		while (iterator2.hasNext()) {
			Pair<Integer, Integer> next = iterator2.next();
			if ((!remainingSet.contains(next.getKey())) || (!remainingSet.contains(next.getValue())))
				iterator2.remove();
		}
		return newGraph;
	}

	public Graph isomorphism() {
		Proof.scanner.nextLine();
		HashMap<Integer, Integer> alpha = new HashMap<Integer, Integer>();
		for (String item : Proof.scanner.nextLine().split(" ")) {
			int comma = item.indexOf(',');
			alpha.put(
					Integer.parseInt(item.substring(0, comma)),
					Integer.parseInt(item.substring(comma + 1, item.length())));
		}
		return isomorphism(alpha);
	}

	public Graph isomorphism(HashMap<Integer, Integer> alpha) {
		HashSet<Integer> vertex = new HashSet<Integer>();

		for (Integer i : this.vertex)
			if (alpha.containsKey(i))
				vertex.add(alpha.get(i));
			else
				vertex.add(i);

		HashSet<Pair<Integer, Integer>> edges = new HashSet<Pair<Integer, Integer>>();
		int first, last;
		for (Pair<Integer, Integer> e : this.edges) {
			if (alpha.containsKey(e.getKey()))
				first = alpha.get(e.getKey());
			else
				first = e.getKey();
			if (alpha.containsKey(e.getValue()))
				last = alpha.get(e.getValue());
			else
				last = e.getValue();
			edges.add(new Pair<Integer, Integer>(first, last));
		}

		return new Graph(vertex, edges);
	}
}
