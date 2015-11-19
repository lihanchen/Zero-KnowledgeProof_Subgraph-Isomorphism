import java.io.BufferedReader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.util.*;

public class Graph {
	TreeSet<Integer> vertex;
	TreeSet<Edge> edges;


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

		vertex = new TreeSet<Integer>();
		edges = new TreeSet<Edge>();

		int size = (int) Math.sqrt(i);
		for (i = 1; i <= size; i++) {
			vertex.add(i);
		}

		Iterator<Boolean> iterator = ll.iterator();
		for (i = 1; i <= size; i++)
			for (int j = 1; j <= size; j++)
				if (iterator.next()) edges.add(new Edge(i, j));
	}

	public Graph(TreeSet<Integer> vertex, TreeSet<Edge> edges) {
		this.vertex = vertex;
		this.edges = edges;
	}

	public Graph(Graph graph) {
		vertex = new TreeSet<Integer>();
		edges = new TreeSet<Edge>();
		for (Integer i : graph.vertex) vertex.add(i);
		for (Edge p : graph.edges) edges.add(new Edge(p.getFirst(), p.getLast()));
	}

	public Graph(String graph){
		vertex = new TreeSet<Integer>();
		edges = new TreeSet<Edge>();
		BufferedReader bufReader = new BufferedReader(new StringReader(graph));
		String line;
		try {
			while ((line = bufReader.readLine()) != null) {
				if(line.charAt(0)==' ') continue;
				vertex.add(Integer.parseInt(line.substring(0,1)));
				for(String e : line.substring(1).split(" ")){
					if(e==null) continue;
					if(e.length()<3) continue;
					edges.add(new Edge(Integer.parseInt(e.substring(0, 1)), Integer.parseInt(e.substring(2, 3))));
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public TreeSet<Integer> readSubGraph() {
		Proof.scanner.nextLine();
		TreeSet<Integer> remainingSet = new TreeSet<Integer>();
		for (String num : Proof.scanner.nextLine().split(" "))
			remainingSet.add(Integer.parseInt(num));
		return remainingSet;
	}

	public Graph subGraph(TreeSet<Integer> remainingSet) {
		Graph newGraph = new Graph(this);
		Iterator<Integer> iterator = newGraph.vertex.iterator();
		while (iterator.hasNext())
			if (!remainingSet.contains(iterator.next()))
				iterator.remove();
		Iterator<Edge> iterator2 = newGraph.edges.iterator();
		while (iterator2.hasNext()) {
			Edge next = iterator2.next();
			if ((!remainingSet.contains(next.getFirst())) || (!remainingSet.contains(next.getLast())))
				iterator2.remove();
		}
		return newGraph;
	}


	public HashMap<Integer, Integer> readIsomorphism() {
		Proof.scanner.nextLine();
		HashMap<Integer, Integer> alpha = new HashMap<Integer, Integer>();
		for (String item : Proof.scanner.nextLine().split(" ")) {
			int comma = item.indexOf(',');
			alpha.put(
					Integer.parseInt(item.substring(0, comma)),
					Integer.parseInt(item.substring(comma + 1, item.length())));
		}
		return alpha;
	}

	public Graph isomorphism(HashMap<Integer, Integer> alpha) {
		TreeSet<Integer> vertex = new TreeSet<Integer>();

		for (Integer i : this.vertex)
			if (alpha.containsKey(i))
				vertex.add(alpha.get(i));
			else
				vertex.add(i);

		TreeSet<Edge> edges = new TreeSet<Edge>();
		int first, last;
		for (Edge e : this.edges) {
			if (alpha.containsKey(e.getFirst()))
				first = alpha.get(e.getFirst());
			else
				first = e.getFirst();
			if (alpha.containsKey(e.getLast()))
				last = alpha.get(e.getLast());
			else
				last = e.getLast();
			edges.add(new Edge(first, last));
		}

		return new Graph(vertex, edges);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Integer i : this.vertex) {
			sb.append(i);
			sb.append(" ");
			for (Edge p : edges)
				if (p.getFirst().equals(i)) {
					sb.append(p);
					sb.append(" ");
				}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void print() {
		System.out.println(this);
	}

	public byte[] hash() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return md.digest(this.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<Integer, Integer> generateRandomIsomorphism() {
		int size = vertex.size();
		HashMap<Integer, Integer> ret = new HashMap<Integer, Integer>(size);
		int iso[] = new int[size + 1];
		for (int i = 1; i <= size; i++) iso[i] = i;
		for (int i = 1; i <= size; i++) {
			int swapIndex = 1 + (int) Math.round(Math.random() * (size - 1));
			System.out.println(swapIndex);
			int t = iso[i];
			iso[i] = iso[swapIndex];
			iso[swapIndex] = t;
		}
		for (int i = 1; i <= size; i++) ret.put(i, iso[i]);
		return ret;
	}

	public TreeSet<Integer> calculateModifiedSubgraph(TreeSet<Integer> originalSubgraph, HashMap<Integer, Integer> iso) {
		TreeSet<Integer> ret = new TreeSet<Integer>();
		for (Integer i : originalSubgraph)
			ret.add(iso.get(i));
		return ret;
	}

	public HashMap<Integer, Integer> calculateCorrespondingIsomorphism(HashMap<Integer, Integer> originalIsomorphism, HashMap<Integer, Integer> randomIsomorphism) {
		HashMap<Integer, Integer> ret = new HashMap<Integer, Integer>(randomIsomorphism.size());
		for (int i = 1; i <= randomIsomorphism.size(); i++) {
			if (!originalIsomorphism.containsKey(i)) continue;
			ret.put(randomIsomorphism.get(i), originalIsomorphism.get(i));
		}
		return ret;
	}

	public boolean equals(Object g) {
		if (!(g instanceof Graph)) return false;
		return this.toString().equals(g.toString());
	}
}

class Edge implements Comparable<Edge> {
	int first;
	int last;

	public Edge(int first, int last) {
		this.first = first;
		this.last = last;
	}

	public Integer getFirst() {
		return first;
	}

	public Integer getLast() {
		return last;
	}

	public int compareTo(Edge o) {
		if (first == o.first) {
			if (last < o.last) return -1;
			if (last == o.last) return 0;
			if (last > o.last) return 1;
		} else {
			if (first < o.first) return -1;
			if (first == o.first) return 0;
			if (first > o.first) return 1;
		}
		return 0;
	}

	public String toString() {
		return "" + first + "," + last;
	}
}