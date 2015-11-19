import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Scanner;

public class Proof {
	public static Scanner scanner;
	public static Graph g, gprime, g2;
	public static TreeSet<Integer> sub;
	public static HashMap<Integer, Integer> iso;


	public static void main(String args[]) throws Exception {

		scanner = new Scanner(System.in);
		g = new Graph();
		sub = g.readSubGraph();
		iso = g.readIsomorphism();
		gprime = g.subGraph(sub);
		g2 = gprime.isomorphism(iso);

		System.out.println("\n\n\ng:");
		g.print();
		System.out.println("g prime:");
		gprime.print();
		System.out.println("g2:");
		g2.print();

		System.out.println("\n\nStart Listening on 8080");
		ServerSocket listener = new ServerSocket(8080);
		while (true) {
			Socket socket = listener.accept();
			System.out.println("Client connected from" + socket.getInetAddress().toString());
			new ProofThread(socket).start();
		}
	}
}

class ProofThread extends Thread {
	Socket s;

	public ProofThread(Socket s) {
		this. = s;
	}

	public void run() {
		BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());


		HashMap<Integer, Integer> randomIso = g.generateRandomIsomorphism();
		TreeSet<Integer> subPrime = g.calculateModifiedSubgraph(sub, randomIso);
		HashMap<Integer, Integer> isoPrime = g.calculateCorrespondingIsomorphism(iso, randomIso);

		Graph q = g.isomorphism(randomIso);
		Graph subgraphPrime = q.subGraph(subPrime);
		Graph g2 = subgraphPrime.isomorphism(isoPrime);

		System.out.println(g.toString());
		System.out.println(subgraphPrime.toString());
		System.out.println(g2.toString());

		String hash = new String(q.hash(), StandardCharsets.UTF_8);
		System.out.println(hash);

		oos.writeObject(g.toString());
		oos.writeObject(g2.toString());
		oos.writeObject(hash);
		oos.flush();

		String challenge = input.readLine();
		System.out.println(challenge);
		if (Integer.parseInt(challenge) == 1) {
			oos.writeObject(q.toString());
			oos.writeObject(randomIso);
		} else {
			oos.writeObject(subgraphPrime.toString());
			oos.writeObject(isoPrime);
		}

		//get the return result:
		String success = input.readLine();
		System.out.println("success?: " + success);

		System.out.println("Graph G: " + g.toString());
		System.out.println("Graph random iso: " + randomIso);
		System.out.println("Graph Q: " + q.toString());
		System.out.println("Subgraph: " + subPrime);
		System.out.println("Graph Qprime: " + subgraphPrime.toString());
		System.out.println("Graph iso prime: " + isoPrime);
		System.out.println("Graph G2: " + g2.toString());
	}
}
}