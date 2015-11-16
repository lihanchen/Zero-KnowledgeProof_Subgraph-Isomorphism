import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Proof {
	public static Scanner scanner;
	public static void main(String args[])throws Exception {

		String serverAddress = "127.0.0.1";
		Socket s = new Socket(serverAddress, 8080);
		BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		ObjectOutputStream  oos = new ObjectOutputStream(s.getOutputStream());



		scanner = new Scanner(System.in);
		Graph g = new Graph();
		HashSet<Integer> sub = g.readSubGraph();
		HashMap<Integer, Integer> iso = g.readIsomorphism();
		HashMap<Integer, Integer> randomIso = g.generateRandomIsomorphism();
		HashSet<Integer> subPrime = g.calculateModifiedSubgraph(sub, randomIso);
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
		if(Integer.parseInt(challenge)==1){
			oos.writeObject(q.toString());
			oos.writeObject(randomIso);
		}else{
			oos.writeObject(subgraphPrime.toString());
			oos.writeObject(isoPrime);
		}

		//get the return result:
		String success = input.readLine();
		System.out.println("success?: "+success);

		System.out.println("Graph G: " + g.toString());
		System.out.println("Graph random iso: "+randomIso);
		System.out.println("Graph Q: "+q.toString());
		System.out.println("Subgraph: "+subPrime);
		System.out.println("Graph Qprime: "+subgraphPrime.toString());
		System.out.println("Graph iso prime: "+isoPrime);
		System.out.println("Graph G2: "+g2.toString());
	}

}
