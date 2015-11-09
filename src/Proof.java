import java.util.Scanner;

public class Proof {
	public static Scanner scanner;
	public static void main(String args[]) {
		scanner = new Scanner(System.in);
		Graph g = new Graph();
		Graph subg = g.subGraph();
		Graph iso = subg.isomorphism();
		System.out.println();
	}
}
