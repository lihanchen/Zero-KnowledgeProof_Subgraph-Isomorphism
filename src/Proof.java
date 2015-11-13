import java.util.Scanner;

public class Proof {
	public static Scanner scanner;
	public static void main(String args[]) {
		scanner = new Scanner(System.in);
		Graph g = new Graph();
		System.out.println(g);
		System.out.println(g.hash());
		Graph subg = g.subGraph(g.readSubGraph());
		System.out.println(subg);
		System.out.println(subg.hash());
		Graph iso = subg.isomorphism(g.readIsomorphism());
		System.out.println(iso);
		System.out.println(iso.hash());
		System.out.println();
	}
}
