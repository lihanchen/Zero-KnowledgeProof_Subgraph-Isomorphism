import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Proof {
	public static Scanner scanner;
	public static void main(String args[]) {
		scanner = new Scanner(System.in);
		Graph g = new Graph();
		HashSet<Integer> sub = g.readSubGraph();
		HashMap<Integer, Integer> iso = g.readIsomorphism();
		HashMap<Integer, Integer> randomIso = g.generateRandomIsomorphism();
		HashSet<Integer> subPrime = g.calculateModifiedSubgraph(sub, randomIso);
		HashMap<Integer, Integer> isoPrime = g.calculateCorrespondingIsomorphism(iso, randomIso);
		System.out.println();
	}
}
