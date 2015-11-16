
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.StringReader;
import java.security.MessageDigest;
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
		for (i = 1; i <= size; i++)
			for (int j = 1; j <= size; j++)
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

	public Graph(String graph){
		vertex = new HashSet<Integer>();
		edges = new HashSet<Pair<Integer, Integer>>();
		BufferedReader bufReader = new BufferedReader(new StringReader(graph));
		String line=null;
		try {
			while ((line = bufReader.readLine()) != null) {
				if(line.charAt(0)==' ') continue;
				vertex.add(Integer.parseInt(line.substring(0,1)));
				for(String e : line.substring(1).split(" ")){
					if(e==null) continue;
					if(e.length()<3) continue;
					edges.add(new Pair<Integer, Integer>(Integer.parseInt(e.substring(0, 1)),Integer.parseInt(e.substring(2, 3))));
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public HashSet<Integer> readSubGraph() {
		Proof.scanner.nextLine();
		HashSet<Integer> remainingSet = new HashSet<Integer>();
		for (String num : Proof.scanner.nextLine().split(" "))
			remainingSet.add(Integer.parseInt(num));
		return remainingSet;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Integer i : this.vertex) {
			sb.append(i);
			sb.append(" ");
			for (Pair<Integer, Integer> p : edges)
				if (p.getKey().equals(i)) {
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

	public HashSet<Integer> calculateModifiedSubgraph(HashSet<Integer> originalSubgraph, HashMap<Integer, Integer> iso) {
		HashSet<Integer> ret = new HashSet<Integer>(originalSubgraph.size());
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

	public boolean isEqual(Graph g){
		Iterator<Integer> iterator = this.vertex.iterator();
		while (iterator.hasNext()){
			if(!g.vertex.contains(iterator.next()))
				return false;
		}

		Iterator<Integer> iterator2 = g.vertex.iterator();
		while (iterator.hasNext()){
			if(!this.vertex.contains(iterator.next()))
				return false;
		}

		Iterator<Pair<Integer, Integer>> iterator3 = this.edges.iterator();
		while (iterator3.hasNext()) {
			Pair<Integer, Integer> next = iterator3.next();
			if (!g.edges.contains(next))
				return false;
		}

		Iterator<Pair<Integer, Integer>> iterator4 = g.edges.iterator();
		while (iterator4.hasNext()) {
			Pair<Integer, Integer> next = iterator4.next();
			if (!this.edges.contains(next))
				return false;
		}

		return true;
	}
}
