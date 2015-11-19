import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class Proof {
	public static Scanner scanner;
	public static Graph g, gprime, g2;
	public static TreeSet<Integer> sub;
	public static HashMap<Integer, Integer> iso;


	public static void main(String args[]) throws Exception {

		scanner = new Scanner(System.in);
		g2 = new Graph();
		sub = g2.readSubGraph();
		iso = g2.readIsomorphism();
		gprime = g2.subGraph(sub);
		g = gprime.isomorphism(iso);

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
			System.out.println("Client connected from " + socket.getInetAddress().toString());
			new ProofThread(socket).start();
		}
	}
}

class ProofThread extends Thread {
	Socket s;

	public ProofThread(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			oos.writeObject(Proof.g);
			oos.writeObject(Proof.g2);
			oos.flush();

			int roundNum = ois.readInt();

			HashMap<Integer, Integer> randomIso[] = new HashMap[roundNum];
			TreeSet<Integer> subPrime[] = new TreeSet[roundNum];
			HashMap<Integer, Integer> isoPrime[] = new HashMap[roundNum];
			Graph gr[] = new Graph[roundNum];
			Graph grprime[] = new Graph[roundNum];
			ArrayList<byte[]> grhash = new ArrayList<byte[]>(roundNum);
			ArrayList<byte[]> grprimehash = new ArrayList<byte[]>(roundNum);

			int factorial = 1;
			for (int i = 1; i <= Proof.g2.vertex.size(); i++) {
				factorial *= i;
			}

			if (roundNum >= factorial) {
				System.out.println("Too many round for this graph. Disconnect");
				return;
			}

			for (int i = 0; i < roundNum; i++) {
				HashMap<Integer, Integer> random = Proof.g2.generateRandomIsomorphism();
				boolean exists = false;
				for (HashMap<Integer, Integer> h : randomIso)
					if ((h != null) && (h.equals(random))) exists = true;
				if (exists) {
					i--;
					continue;
				}
				randomIso[i] = random;
				subPrime[i] = Proof.g2.calculateModifiedSubgraph(Proof.sub, random);
				isoPrime[i] = Proof.g2.calculateCorrespondingIsomorphism(Proof.iso, random);
				gr[i] = Proof.g2.isomorphism(randomIso[i]);
				grprime[i] = gr[i].subGraph(subPrime[i]);
				assert grprime[i].isomorphism(isoPrime[i]).equals(Proof.g);
				grhash.add(gr[i].hash());
				grprimehash.add(grprime[i].hash());
			}

			oos.writeObject(grhash);
			oos.writeObject(grprimehash);
			oos.flush();

			boolean[] challenges = (boolean[]) ois.readObject(); //True=random, False=corrisponding

			ArrayList<HashMap<Integer, Integer>> changes = new ArrayList<HashMap<Integer, Integer>>(roundNum);
			ArrayList<Graph> graphs = new ArrayList<Graph>(roundNum);
			for (int i = 0; i < roundNum; i++) {
				if (challenges[i]) {
					changes.add(randomIso[i]);
					graphs.add(gr[i]);
				} else {
					changes.add(isoPrime[i]);
					graphs.add(grprime[i]);
				}
			}

			oos.writeObject(changes);
			oos.writeObject(graphs);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}