import java.util.Scanner;

public class Proof {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		Graph g = new Graph(scanner);
		Graph subg = g.subGraph();
		System.out.println();
	}
}
